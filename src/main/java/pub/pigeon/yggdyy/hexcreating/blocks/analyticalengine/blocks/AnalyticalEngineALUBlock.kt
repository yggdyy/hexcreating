package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks

import com.simibubi.create.content.equipment.wrench.IWrenchable
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.item.ItemUsageContext
import net.minecraft.state.StateManager
import net.minecraft.state.property.IntProperty
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineALUBlockEntity

class AnalyticalEngineALUBlock(settings: Settings) : BlockWithEntity(settings), IWrenchable {
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

    override fun createBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return AnalyticalEngineALUBlockEntity(blockPos, blockState)
    }

    override fun getRenderType(blockState: BlockState?): BlockRenderType {
        return BlockRenderType.MODEL
    }

    /*override fun onUse(blockState: BlockState, world: World, blockPos: BlockPos, playerEntity: PlayerEntity, hand: Hand, blockHitResult: BlockHitResult): ActionResult {
        if(!world.isClient()) {
            playerEntity.sendMessage(Text.literal("这是一个测试"))
        }
        return ActionResult.SUCCESS
    }*/
    override fun onBreak(world: World, blockPos: BlockPos, blockState: BlockState, playerEntity: PlayerEntity?) {
        //playerEntity?.sendMessage(Text.literal("啊啊啊啊啊"))
        if(!world.isClient) {
            val e: AnalyticalEngineALUBlockEntity = world.getBlockEntity(blockPos) as AnalyticalEngineALUBlockEntity
            e.tellControllerBroken()
        }
        super.onBreak(world, blockPos, blockState, playerEntity)
    }

    override fun onSneakWrenched(state: BlockState, context: ItemUsageContext): ActionResult {
        if(!context.world.isClient) {
            val e: AnalyticalEngineALUBlockEntity = context.world.getBlockEntity(context.blockPos) as AnalyticalEngineALUBlockEntity
            e.tellControllerBroken()
        }
        return super.onSneakWrenched(state, context)
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