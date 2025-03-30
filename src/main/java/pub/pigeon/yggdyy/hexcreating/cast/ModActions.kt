package pub.pigeon.yggdyy.hexcreating.cast

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain
import pub.pigeon.yggdyy.hexcreating.cast.actions.*

object ModActions {
    public fun registerModActions() {
        register(OpKelvinHeatGambit.NAME, OpKelvinHeatGambit.STROKE, OpKelvinHeatGambit.DIR, OpKelvinHeatGambit())
        register(OpKelvinSuperHeatGambit.NAME, OpKelvinSuperHeatGambit.STROKE, OpKelvinSuperHeatGambit.DIR, OpKelvinSuperHeatGambit())
        register(OpKelvinTimePurification.NAME, OpKelvinTimePurification.STROKE, OpKelvinTimePurification.DIR, OpKelvinTimePurification())
        register(OpKelvinTypePurification.NAME, OpKelvinTypePurification.STROKE, OpKelvinTypePurification.DIR, OpKelvinTypePurification())

        register(OpPascalTimePurification.NAME, OpPascalTimePurification.STROKE, OpPascalTimePurification.DIR, OpPascalTimePurification())

        register(OpKineticRotationPurification.NAME, OpKineticRotationPurification.STROKE, OpKineticRotationPurification.DIR, OpKineticRotationPurification())
        register(OpKineticNetworkStressAllPurification.NAME, OpKineticNetworkStressAllPurification.STROKE, OpKineticNetworkStressAllPurification.DIR, OpKineticNetworkStressAllPurification())
        register(OpKineticNetworkStressUsedPurification.NAME, OpKineticNetworkStressUsedPurification.STROKE, OpKineticNetworkStressUsedPurification.DIR, OpKineticNetworkStressUsedPurification())
        register(OpKineticStressPurification.NAME, OpKineticStressPurification.STROKE, OpKineticStressPurification.DIR, OpKineticStressPurification())

        register(OpTrainGateSetUpGambit.NAME, OpTrainGateSetUpGambit.STROKE, OpTrainGateSetUpGambit.DIR, OpTrainGateSetUpGambit())
        //register(OpCircleAmplifierGambit.NAME, OpCircleAmplifierGambit.STROKE, OpCircleAmplifierGambit.DIR, OpCircleAmplifierGambit())
        //register(OpCircleAmplifierPurification.NAME, OpCircleAmplifierPurification.STROKE, OpCircleAmplifierPurification.DIR, OpCircleAmplifierPurification())
    }

    private fun register(name: String, stroke: String, dir: HexDir, action: Action) {
        Registry.register(HexActions.REGISTRY, Identifier(HexcreatingMain.MOD_ID, name), ActionRegistryEntry(HexPattern.fromAngles(stroke, dir), action))
    }
}