package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.CircleAmplifierSenderBlockEntity

class CircleAmplifierSenderBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun createBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return CircleAmplifierSenderBlockEntity(blockPos, blockState)
    }
}