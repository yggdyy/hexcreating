package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks

import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock
import com.simibubi.create.foundation.block.IBE
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.minecraft.world.WorldAccess
import net.minecraft.world.WorldView
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineKineticInterfaceBlockEntity

class AnalyticalEngineKineticInterfaceBlock(settings: Settings) : DirectionalKineticBlock(settings), IBE<AnalyticalEngineKineticInterfaceBlockEntity>{
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

    override fun onBreak(world: World, blockPos: BlockPos, blockState: BlockState, playerEntity: PlayerEntity?) {
        if(!world.isClient) {
            val e: AnalyticalEngineKineticInterfaceBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineKineticInterfaceBlockEntity
            e.tellControllerBroken()
        }
        super.onBreak(world, blockPos, blockState, playerEntity)
    }

    //wrench break doesn't call the upper onBreak, that's so bad(
    override fun onSneakWrenched(state: BlockState, context: ItemUsageContext): ActionResult {
        val world = context.world
        val blockPos = context.blockPos
        if(!world.isClient) {
            val e: AnalyticalEngineKineticInterfaceBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineKineticInterfaceBlockEntity
            e.tellControllerBroken()
        }
        return super.onSneakWrenched(state, context)
    }

    override fun onBlockAdded(state: BlockState, worldIn: World, pos: BlockPos, oldState: BlockState, isMoving: Boolean) {
        super.onBlockAdded(state, worldIn, pos, oldState, isMoving)
        blockUpdate(state, worldIn, pos)
    }

    override fun prepare(stateIn: BlockState, worldIn: WorldAccess, pos: BlockPos, flags: Int, count: Int) {
        super.prepare(stateIn, worldIn, pos, flags, count)
        blockUpdate(stateIn, worldIn, pos)
    }

    override fun neighborUpdate(state: BlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos,
                                isMoving: Boolean) {
        blockUpdate(state, worldIn, pos)
    }

    override fun getPlacementState(context: ItemPlacementContext): BlockState? {
        val world = context.world
        val pos = context.blockPos
        val face = context.side
        val placedOn = world.getBlockState(pos.offset(face.opposite))
        val placedOnOpposite = world.getBlockState(pos.offset(face))
        if (AbstractChuteBlock.isChute(placedOn)) return defaultState.with(FACING, face.opposite)
        if (AbstractChuteBlock.isChute(placedOnOpposite)) return defaultState.with(FACING, face)
        var preferredFacing = getPreferredFacing(context)
        if (preferredFacing == null) preferredFacing = context.playerLookDirection
        return defaultState.with(FACING, if (context.player != null && context.player!!
                        .isSneaking) preferredFacing else preferredFacing.opposite).with(RENDER_STAGE, RENDER_STAGE_BEGIN)
    }

    protected fun blockUpdate(state: BlockState, worldIn: WorldAccess, pos: BlockPos) {
        if (worldIn is WrappedWorld) return
        notifyBlockEntity(worldIn, pos)
    }

    protected fun notifyBlockEntity(world: WorldAccess, pos: BlockPos) {
        //withBlockEntityDo(world, pos, Consumer<EncasedFanBlockEntity> { obj: EncasedFanBlockEntity -> obj.blockInFrontChanged() })
    }

    override fun updateAfterWrenched(newState: BlockState, context: ItemUsageContext): BlockState {
        blockUpdate(newState, context.world, context.blockPos)
        return newState
    }

    override fun getRotationAxis(state: BlockState): Direction.Axis {
        return state.get(FACING)
                .axis
    }

    override fun hasShaftTowards(world: WorldView, pos: BlockPos, state: BlockState, face: Direction): Boolean {
        return face == state.get(FACING)
    }

    override fun getBlockEntityClass(): Class<AnalyticalEngineKineticInterfaceBlockEntity> {
        return AnalyticalEngineKineticInterfaceBlockEntity::class.java
    }

    override fun getBlockEntityType(): BlockEntityType<out AnalyticalEngineKineticInterfaceBlockEntity> {
        return ModBlockEntities.ANALYTICAL_ENGINE_KINETIC_INTERFACE
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