package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent
import at.petrak.hexcasting.api.casting.circles.ICircleComponent
import at.petrak.hexcasting.api.casting.circles.ICircleComponent.ControlFlow
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.iota.PatternIota
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex
import com.simibubi.create.content.equipment.wrench.IWrenchable
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineControllerBlockEntity
import pub.pigeon.yggdyy.hexcreating.libs.BlockTickHelper
import java.util.*


class AnalyticalEngineControllerBlock(settings: Settings) : BlockCircleComponent(settings), BlockEntityProvider, IWrenchable {
    companion object {
        const val RENDER_STAGE_BEGIN = 0;
        const val RENDER_STAGE_END = 1
        @JvmField
        val RENDER_STAGE = IntProperty.of("render_stage", RENDER_STAGE_BEGIN, RENDER_STAGE_END)

        @JvmStatic
        public fun marchRenderStage(world: World, blockPos: BlockPos, blockState: BlockState) {
            var nxtStage: Int = blockState.get(RENDER_STAGE) + 1
            if(nxtStage > RENDER_STAGE_END) nxtStage = RENDER_STAGE_BEGIN
            world.setBlockState(blockPos, blockState.with(RENDER_STAGE, nxtStage))
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        super.appendProperties(builder)
        builder.add(RENDER_STAGE)
    }
    override fun getPlacementState(pContext: ItemPlacementContext): BlockState? {
        return super.getPlacementState(pContext)?.with(RENDER_STAGE, RENDER_STAGE_BEGIN)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BlockTickHelper.checkType(type, ModBlockEntities.ANALYTICAL_ENGINE_CONTROLLER, AnalyticalEngineControllerBlockEntity::tick)
    }


    override fun createBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return AnalyticalEngineControllerBlockEntity(blockPos, blockState)
    }
    override fun getRenderType(blockState: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun onUse(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): ActionResult {
        if(world.isClient) return ActionResult.SUCCESS
        //playerEntity.sendMessage(Text.literal((world.getBlockEntity(blockPos) as AnalyticalEngineControllerBlockEntity).toString()))
        val stack: ItemStack = playerEntity.getStackInHand(hand)
        if(stack.isEmpty) {
            val e: AnalyticalEngineControllerBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineControllerBlockEntity
            e.updateStructure()
        }
        val item: ItemPackagedHex? = stack.item as? ItemPackagedHex
        if(item != null && item.hasHex(stack)) {
            val e: AnalyticalEngineControllerBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineControllerBlockEntity
            if(e.tryApplyHex(item.getHex(stack, world as ServerWorld)?.toList())) {
                e.markDirty()
                world.markDirty(blockPos)
                world.updateListeners(blockPos, blockState, blockState, 0)
                return ActionResult.SUCCESS
            } else {
                return ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }

    override fun onBreak(world: World, blockPos: BlockPos, blockState: BlockState, playerEntity: PlayerEntity?) {
        if(!world.isClient){
            val e: AnalyticalEngineControllerBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineControllerBlockEntity
            e.unboundStructure()
        }
        super.onBreak(world, blockPos, blockState, playerEntity)
    }
    override fun onSneakWrenched(state: BlockState, context: ItemUsageContext): ActionResult {
        if(!context.world.isClient) {
            val e: AnalyticalEngineControllerBlockEntity = context.world.getBlockEntity(context.blockPos) as AnalyticalEngineControllerBlockEntity
            e.unboundStructure()
        }
        return super.onSneakWrenched(state, context)
    }

    override fun acceptControlFlow(
        imageIn: CastingImage,
        env: CircleCastEnv,
        enterDir: Direction,
        pos: BlockPos,
        bs: BlockState,
        world: ServerWorld
    ): ICircleComponent.ControlFlow {
        //world.players[0].sendMessage(Text.literal("AE accepting CF"))
        val e: AnalyticalEngineControllerBlockEntity? = world?.getBlockEntity(pos) as? AnalyticalEngineControllerBlockEntity
        if(e == null || !e.canExecute()) {
            return ICircleComponent.ControlFlow.Stop()
        }

        val patterns = e.patterns
        val l = e.patterns.count()
        val vm: CastingVM = CastingVM(imageIn, env)
        for(p in patterns) {
            val pi = PatternIota(p)
            val res = vm.queueExecuteAndWrapIota(pi, world)
            if(res.resolutionType.success) continue
            else {
                return ICircleComponent.ControlFlow.Stop()
            }
        }

        val exitDirsSet = possibleExitDirections(pos, bs, world)
        exitDirsSet.remove(enterDir.opposite)
        val exitDirs = exitDirsSet.stream().map { dir: Direction? ->
            exitPositionFromDirection(
                pos,
                dir
            )
        }
        return ControlFlow.Continue(vm.image, exitDirs.toList())
    }

    override fun canEnterFromDirection(
        enterDir: Direction?,
        pos: BlockPos?,
        bs: BlockState?,
        world: ServerWorld?
    ): Boolean {
        return true
    }

    override fun possibleExitDirections(pos: BlockPos?, bs: BlockState?, world: World?): EnumSet<Direction> {
        val allDirs = EnumSet.allOf(Direction::class.java)
        return allDirs
    }

    override fun normalDir(pos: BlockPos?, bs: BlockState?, world: World?, recursionLeft: Int): Direction {
        return Direction.UP
    }

    override fun particleHeight(pos: BlockPos?, bs: BlockState?, world: World?): Float {
        return 1f
    }

    override fun getOutlineShape(
        blockState: BlockState?,
        blockView: BlockView?,
        blockPos: BlockPos?,
        shapeContext: ShapeContext?
    ): VoxelShape {
        return VoxelShapes.fullCube()
    }


}