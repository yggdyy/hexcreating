package pub.pigeon.yggdyy.hexcreating.api.iotanet

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.utils.putCompound
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import kotlin.random.Random

class IotaPack(val id: Long, val iota: Iota, var headInfo: MutableSet<String> = mutableSetOf()) {

    companion object {
        @JvmStatic
        fun createNew(pIota: Iota): IotaPack {
            return IotaPack(Random.nextLong(), pIota)
        }

        @JvmStatic
        fun createNew(pIota: Iota, pHeadInfo: Set<String>): IotaPack {
            return IotaPack(Random.nextLong(), pIota, pHeadInfo.toMutableSet())
        }

        @JvmStatic
        fun toNbt(pIotaPack: IotaPack): NbtCompound {
            var ret = NbtCompound()
            ret.putLong("id", pIotaPack.id)
            ret.putCompound("iota", IotaType.serialize(pIotaPack.iota))
            ret.putInt("head_info_length", pIotaPack.headInfo.count())
            for((index, s) in pIotaPack.headInfo.withIndex()) {
                ret.putString("head_info_$index", s)
            }
            return ret
        }

        //make sure your NBT are legal!!!!
        //I haven't made any legal check!!!
        @JvmStatic
        fun fromNbt(pTag: NbtCompound, world: ServerWorld): IotaPack? {
            if(!(
                    pTag.contains("id") && pTag.contains("iota") && pTag.contains("head_info_length")
                    )) return null;
            val id = pTag.getLong("id")
            val iota = IotaType.deserialize(pTag.getCompound("iota"), world)
            val length = pTag.getInt("head_info_length")
            var headInfo = mutableSetOf<String>()
            for(i in 0 until length) {
                headInfo.add(pTag.getString("head_info_$i"))
            }
            return IotaPack(id, iota, headInfo)
        }
    }

    public fun addHeadInfo(pStr: String): Boolean {
        if(!this.headInfo.contains(pStr)) {
            this.headInfo.add(pStr)
            return true
        }
        return false
    }

    public fun addHeadInfo(pStrs: Collection<String>): Int {
        var ret: Int = 0
        for(s in pStrs) {
            if(!this.headInfo.contains(s)) {
                this.headInfo.add(s)
                ++ret
            }
        }
        return ret
    }

    public fun deleteHeadInfo(pStr: String): Boolean {
        if(this.headInfo.contains(pStr)) {
            this.headInfo.remove(pStr)
            return true
        }
        return false
    }

    public fun deleteHeadInfo(pStrs: Collection<String>): Int {
        var ret: Int = 0
        for(s in pStrs) {
            if(this.headInfo.contains(s)) {
                this.headInfo.remove(s)
                ++ret
            }
        }
        return ret
    }

    public fun hasHeadInfo(pStr: String): Boolean {
        return this.headInfo.contains(pStr)
    }

    public fun hasHeadInfo(pStrs: Collection<String>): Int {
        var ret = 0;
        for(s in pStrs) {
            if(this.headInfo.contains(s)) ++ret;
        }
        return ret;
    }

    override fun equals(other: Any?): Boolean {
        val o: IotaPack = other as? IotaPack ?: return false
        return o.id == this.id
    }

    public fun toNbt(): NbtCompound {
        return IotaPack.toNbt(this)
    }

    public fun copyWithSameId(): IotaPack {
        var ret = mutableSetOf<String>()
        ret.addAll(this.headInfo);
        return IotaPack(this.id, this.iota, ret);
    }

    public fun copyWithDifferentId(): IotaPack {
        var ret = mutableSetOf<String>()
        ret.addAll(this.headInfo);
        return IotaPack.createNew(this.iota, ret)
    }
}