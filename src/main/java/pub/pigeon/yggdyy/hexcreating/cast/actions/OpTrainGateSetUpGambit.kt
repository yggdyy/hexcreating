package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateCoreBlockEntity
import kotlin.math.abs

class OpTrainGateSetUpGambit: SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos1 = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 1, Text.translatable("mishaps.need_vector"))
        val pos2 = args.getBlockPos(1, argc) ?:
            throw MishapInvalidIota(args[0], 1, Text.translatable("mishaps.need_vector"))
        if(!env.world.getBlockState(pos1).isOf(ModBlocks.TRAIN_GATE_CORE))
            throw MishapBadBlock(pos1, Text.translatable("mishaps.need_train_gate_core"))
        if(!env.world.getBlockState(pos2).isOf(ModBlocks.TRAIN_GATE_CORE))
            throw MishapBadBlock(pos2, Text.translatable("mishaps.need_train_gate_core"))
        val dis: Long = abs(pos1.x - pos2.x) * 1L + abs(pos1.y - pos2.y) + abs(pos1.z - pos2.z)
        return SpellAction.Result(
            Spell(pos1, pos2), MediaConstants.DUST_UNIT / 10L * dis, listOf()
        )
    }
    private data class Spell(val pos1: BlockPos, val pos2: BlockPos): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val world = env.world
            val core1: TrainGateCoreBlockEntity = world.getBlockEntity(pos1) as TrainGateCoreBlockEntity
            val core2: TrainGateCoreBlockEntity = world.getBlockEntity(pos2) as TrainGateCoreBlockEntity
            core1.tryDestroyGate()
            core2.tryDestroyGate()
            if(core1.canSetUpGate() && core2.canSetUpGate()) {
                core1.setUpGate(pos2)
                core2.setUpGate(pos1)
            }
        }
    }
    companion object {
        const val NAME = "train_gate_set_up_gambit"
        const val STROKE = "aadqwqwqwqwqwqqqaq"
        val DIR = HexDir.SOUTH_WEST
    }
}