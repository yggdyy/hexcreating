package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.utils.hasInt
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities

class AnalyticalEngineALUBlockEntity(blockPos: BlockPos, blockState: BlockState): HexBlockEntity(ModBlockEntities.ANALYTICAL_ENGINE_ALU, blockPos, blockState) {
    var controllerPos: BlockPos? = null
    val CONTROLLER_POS_KEY = "controller_pos"

    override fun saveModData(tag: NbtCompound) {
        if(controllerPos != null) {
            tag.putInt(CONTROLLER_POS_KEY + "_x", controllerPos!!.x)
            tag.putInt(CONTROLLER_POS_KEY + "_y", controllerPos!!.y)
            tag.putInt(CONTROLLER_POS_KEY + "_z", controllerPos!!.z)
        }
    }
    override fun loadModData(tag: NbtCompound) {
        if(tag.hasInt(CONTROLLER_POS_KEY + "_x")) {
            controllerPos = BlockPos(
                    tag.getInt(CONTROLLER_POS_KEY + "_x"),
                    tag.getInt(CONTROLLER_POS_KEY + "_y"),
                    tag.getInt(CONTROLLER_POS_KEY + "_z")
            )
        } else controllerPos = null;
    }
    //当在被破坏的时候调用这个提醒控制器重构多方快结构
    fun tellControllerBroken() {
        if(controllerPos != null) {
            val ce: AnalyticalEngineControllerBlockEntity? = world?.getBlockEntity(controllerPos) as? AnalyticalEngineControllerBlockEntity
            if(ce == null) {
                controllerPos = null
            } else {
                controllerPos = pos
                ce.updateStructure()
            }
            sync()
        }
    }
}