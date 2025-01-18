package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInternalException
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.CircleAmplifierSenderBlockEntity

class OpCircleAmplifierGambit: SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val recPosIota: ListIota = args.get(0) as? ListIota ?:
            throw MishapInvalidIota(args[0], 1, Text.translatable("mishaps.need_list"))
        val senderPos: BlockPos = args.getBlockPos(1, argc) ?:
            throw MishapInvalidIota(args[1], 0, Text.translatable("mishaps.need_vector"))
        val recPosIotaList: List<Iota> = recPosIota.subIotas()!!.toList()
        var recPosList: MutableList<BlockPos> = mutableListOf()
        for(i in 0 until recPosIotaList.count()) {
            val nowPos: BlockPos = recPosIotaList.getBlockPos(i, recPosIotaList.count()) ?:
                throw MishapInvalidIota(args[0], 1, Text.translatable("mishaps.need_list"))
            recPosList.add(nowPos)
        }

        val se: CircleAmplifierSenderBlockEntity = env.world.getBlockEntity(senderPos) as? CircleAmplifierSenderBlockEntity
            ?: throw MishapBadBlock(senderPos, Text.translatable("mishaps.need_circle_amplifier_sender"))
        if(se.trySetReceiversPos(recPosList)) {
            return SpellAction.Result(
                Spell(senderPos, recPosList), MediaConstants.SHARD_UNIT * recPosList.count(), listOf()
            )
        } else {
            throw MishapInternalException(Exception())
        }
    }

    private data class Spell(val senderPos: BlockPos, val recPosList: List<BlockPos>): RenderedSpell {
        override fun cast(env: CastingEnvironment) {

        }
    }

    companion object {
        const val NAME = "circle_amplifier_gambit"
        const val STROKE = "qadedawdd"
        val DIR = HexDir.SOUTH_WEST
    }
}