package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import com.simibubi.create.content.kinetics.KineticNetwork
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.util.math.BlockPos

class OpKineticNetworkStressUsedPurification: OpKineticBasePurification() {
    override fun run(targetPos: BlockPos, env: CastingEnvironment): List<Iota> {
        val e: KineticBlockEntity = env.world.getBlockEntity(targetPos) as KineticBlockEntity
        val kn: KineticNetwork = e.orCreateNetwork ?:
            return listOf(DoubleIota(0.0))
        return listOf( DoubleIota(kn.calculateStress() * 1.0))
    }

    companion object{
        const val NAME = "kinetic_network_stress_used_purification"
        const val STROKE = "qadedaqw"
        val DIR = HexDir.SOUTH_WEST
    }
}