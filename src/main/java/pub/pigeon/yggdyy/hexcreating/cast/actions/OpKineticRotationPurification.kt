package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.util.math.BlockPos
import kotlin.math.abs

class OpKineticRotationPurification: OpKineticBasePurification() {
    override fun run(targetPos: BlockPos, env: CastingEnvironment): List<Iota> {
        val e: KineticBlockEntity = env.world.getBlockEntity(targetPos) as KineticBlockEntity
        return listOf(DoubleIota(abs( e.speed * 1.0)))
    }
    companion object {
        const val NAME = "kinetic_rotation_purification"
        const val STROKE = "qadedaqd"
        val DIR = HexDir.SOUTH_WEST
    }
}