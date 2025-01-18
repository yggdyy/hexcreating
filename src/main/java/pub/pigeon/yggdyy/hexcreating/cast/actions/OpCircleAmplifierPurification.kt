package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import net.minecraft.text.Text
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.CircleAmplifierSenderBlockEntity

class OpCircleAmplifierPurification: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 0, Text.translatable("mishaps.need_vector"))
        if(env.world.getBlockState(pos).isOf(ModBlocks.CIRCLE_AMPLIFIER_SENDER)) {
            return listOf((env.world.getBlockEntity(pos) as CircleAmplifierSenderBlockEntity).receiversPosToListIota())
        } else {
            return listOf(NullIota())
        }
    }

    companion object {
        const val NAME = "circle_amplifier_purification"
        const val STROKE = "qadedawde"
        val DIR = HexDir.SOUTH_WEST
    }
}