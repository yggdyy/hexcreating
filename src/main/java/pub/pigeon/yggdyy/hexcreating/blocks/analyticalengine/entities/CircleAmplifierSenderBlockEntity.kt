package pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities

import at.petrak.hexcasting.api.block.HexBlockEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import net.minecraft.block.BlockState
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.libs.BlockPosNBTHelper

class CircleAmplifierSenderBlockEntity(blockPos: BlockPos, blockState: BlockState) : HexBlockEntity(ModBlockEntities.CIRCLE_AMPLIFIER_SENDER, blockPos, blockState) {
    public var receiversPos: MutableList<BlockPos> = mutableListOf()
    val RECEIVERS_POS_KEY = "receivers_pos"

    override fun saveModData(tag: NbtCompound) {
        BlockPosNBTHelper.writeBlockPosListToNBT(tag, receiversPos, RECEIVERS_POS_KEY)
    }
    override fun loadModData(tag: NbtCompound) {
        receiversPos = BlockPosNBTHelper.readBlockPosListFromNBT(tag, RECEIVERS_POS_KEY).toMutableList()
    }

    //only call this on server
    public fun trySetReceiversPos(newReceiversPos: List<BlockPos>): Boolean {
        for(p in newReceiversPos) {
            if (world == null || world!!.isOutOfHeightLimit(p)) {
                throw MishapBadLocation(p.toCenterPos())
                return false
            }
        }
        receiversPos = newReceiversPos.toMutableList()
        sync()
        return true
    }

    fun receiversPosToListIota(): ListIota {
        var ret: MutableList<Iota> = mutableListOf()
        for(p in receiversPos) {
            ret.add(Vec3Iota(p.toCenterPos()))
        }
        return ListIota(ret)
    }

    fun getAvailableReceiversPos(): List<BlockPos> {
        var ret = mutableListOf<BlockPos>()
        for(p in receiversPos) {
            if(world!!.getBlockState(p).isOf(ModBlocks.CIRCLE_AMPLIFIER_RECEIVER))
                ret.add(p)
        }
        return ret.toList()
    }
}