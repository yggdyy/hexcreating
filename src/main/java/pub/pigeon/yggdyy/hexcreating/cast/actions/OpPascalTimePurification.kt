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
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

class OpPascalTimePurification: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val targetPos: BlockPos = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 0, Text.translatable("mishaps.need_vector"))
        if(env.world.getBlockState(targetPos).isOf(AllBlocks.COPPER_BACKTANK.get()) ||
                env.world.getBlockState(targetPos).isOf(AllBlocks.NETHERITE_BACKTANK.get())) {
            val e: BacktankBlockEntity = env.world.getBlockEntity(targetPos) as BacktankBlockEntity ?:
                return listOf(NullIota())
            return listOf(DoubleIota(e.airLevel * 1.0))
        }
        return listOf(NullIota())
    }
    companion object {
        const val NAME = "pascal_time_purification"
        const val STROKE = "qadedaed"
        val DIR = HexDir.SOUTH_WEST
    }
}