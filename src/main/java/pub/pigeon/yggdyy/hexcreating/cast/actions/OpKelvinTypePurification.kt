package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

/*
not blaze burner -> null
no blaze -> 0
blaze no heat -> 1
blaze heat -> 3
blaze super heat -> 4
 */
class OpKelvinTypePurification: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val targetPos: BlockPos = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 0, Text.translatable("mishaps.need_vector"))
        if(!env.world.getBlockState(targetPos).isOf(AllBlocks.BLAZE_BURNER.get())) {
            return listOf(NullIota())
        }
        return listOf(
                DoubleIota(BlazeBurnerBlock.getHeatLevelOf(env.world.getBlockState(targetPos)).ordinal * 1.0)
        )
    }

    companion object {
        const val NAME = "kelvin_type_purification"
        const val STROKE = "qadedadq"
        val DIR = HexDir.SOUTH_WEST
    }
}