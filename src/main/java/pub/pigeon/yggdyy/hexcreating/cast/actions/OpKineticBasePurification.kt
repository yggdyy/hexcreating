package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import com.simibubi.create.content.kinetics.base.KineticBlock
import com.simibubi.create.content.kinetics.base.KineticBlockEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

open class OpKineticBasePurification: ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val targetPos: BlockPos = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 0, Text.translatable("mishaps.need_vector"))
        val e: KineticBlockEntity = env.world.getBlockEntity(targetPos) as? KineticBlockEntity ?:
            return listOf(NullIota())
        return run(targetPos, env)
    }
    open fun run(targetPos: BlockPos, env: CastingEnvironment): List<Iota> {
        return listOf()
    }
}