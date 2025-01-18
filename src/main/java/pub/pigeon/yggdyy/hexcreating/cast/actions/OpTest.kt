package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.misc.MediaConstants

class OpTest: ConstMediaAction {
    override val argc: Int
        get() = 0
    override val mediaCost: Long
        get() = MediaConstants.DUST_UNIT

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        return listOf(DoubleIota(200.0))
    }
    companion object {
        const val NAME = "test"
        const val STROKE = "qaded"
        val DIR = HexDir.SOUTH_WEST
    }
}