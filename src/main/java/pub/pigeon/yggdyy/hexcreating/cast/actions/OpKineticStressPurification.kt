package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain
import kotlin.math.abs

//get a kinetic block's final stress contribute (e.g. a waterwheel: 256; a 8RPM fan: -16)
class OpKineticStressPurification: OpKineticBasePurification() {
    override fun run(targetPos: BlockPos, env: CastingEnvironment): List<Iota> {
        val e: KineticBlockEntity = env.world.getBlockEntity(targetPos) as KineticBlockEntity
        /*HexcreatingMain.LOGGER.info(
                e.calculateAddedStressCapacity().toString() + "\n" +
                e.calculateStressApplied().toString() + "\n" +
                e.speed.toString()
        )*/
        return listOf(DoubleIota(
                (e.calculateAddedStressCapacity() - e.calculateStressApplied()) * 1.0 * abs(e.speed)
        ))
    }
    companion object {
        const val NAME = "kinetic_stress_purification"
        const val STROKE = "qadedaqq"
        val DIR = HexDir.SOUTH_WEST
    }
}