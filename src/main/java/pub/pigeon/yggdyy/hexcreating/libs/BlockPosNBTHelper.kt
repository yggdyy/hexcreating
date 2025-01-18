package pub.pigeon.yggdyy.hexcreating.libs

import at.petrak.hexcasting.api.utils.hasInt
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

object BlockPosNBTHelper {
    @JvmStatic
    fun writeBlockPosToNBT(tag: NbtCompound, pos: BlockPos, keyHead: String) {
        tag.putInt(keyHead + "_x", pos.x)
        tag.putInt(keyHead + "_y", pos.y)
        tag.putInt(keyHead + "_z", pos.z)
    }

    @JvmStatic
    fun readBlockPosFromNBT(tag: NbtCompound, keyHead: String): BlockPos? {
        if(
            tag.hasInt(keyHead + "_x") &&
            tag.hasInt(keyHead + "_y") &&
            tag.hasInt(keyHead + "_z")
        ) {
            return BlockPos(
                tag.getInt(keyHead + "_x"),
                tag.getInt(keyHead + "_y"),
                tag.getInt(keyHead + "_z")
            )
        }
        return null
    }

    @JvmStatic
    fun writeBlockPosListToNBT(tag: NbtCompound, posList: List<BlockPos>, keyHead: String) {
        for(i in 0 until posList.count()) {
            writeBlockPosToNBT(tag, posList[i], keyHead + "_" + i.toString())
        }
        tag.putInt(keyHead + "_length", posList.count())
    }

    @JvmStatic
    fun readBlockPosListFromNBT(tag: NbtCompound, keyHead: String): List<BlockPos> {
        if(tag.hasInt(keyHead + "_length")) {
            var ret: MutableList<BlockPos> = mutableListOf()
            for(i in 0 until tag.getInt(keyHead + "_length")) {
                val nowPos: BlockPos? = readBlockPosFromNBT(tag, keyHead + "_" + i.toString())
                if(nowPos != null) ret.add(nowPos)
            }
            return ret
        }
        return listOf()
    }
}