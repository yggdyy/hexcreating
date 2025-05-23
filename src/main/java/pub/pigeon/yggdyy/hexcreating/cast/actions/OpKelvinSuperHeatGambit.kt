package pub.pigeon.yggdyy.hexcreating.cast.actions

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getInt
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import pub.pigeon.yggdyy.hexcreating.mixins.BlazeBurnerBlockEntityAccessor
import kotlin.math.abs
import kotlin.math.min

class OpKelvinSuperHeatGambit: SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc) ?:
            throw MishapInvalidIota(args[0], 1, Text.translatable("mishaps.need_vector"))
        val tick = abs(args.getInt(1, argc)) ?:
            throw MishapInvalidIota(args[1], 0, Text.translatable("mishaps.need_integer"))
        if(!env.world.getBlockState(pos).isOf(AllBlocks.BLAZE_BURNER.get())) {
            throw MishapBadBlock(pos, Text.translatable("mishaps.need_blaze_burner"))
        }
        if(BlazeBurnerBlock.getHeatLevelOf(env.world.getBlockState(pos)).equals(BlazeBurnerBlock.HeatLevel.NONE) ||
                BlazeBurnerBlock.getHeatLevelOf(env.world.getBlockState(pos)).equals(BlazeBurnerBlock.HeatLevel.FADING)) {
            throw MishapBadBlock(pos, Text.translatable("mishaps.need_blaze_burner_with_blaze"))
        }
        return SpellAction.Result(
                Spell(pos, tick), MediaConstants.DUST_UNIT / 20 * tick, listOf()
        )
    }

    private data class Spell(val targetPos: BlockPos, val targetTick: Int): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            /*for( p in env.world.players) {
                p.sendMessage(Text.literal("kelvin's super heat gambit"))
            }*/

            if(targetTick <= 0) return
            val e: BlazeBurnerBlockEntity = env.world.getBlockEntity(targetPos) as BlazeBurnerBlockEntity
            if(e.isCreative ) return
            val ep: BlazeBurnerBlockEntityAccessor = e as BlazeBurnerBlockEntityAccessor
            if(e.activeFuel.equals(FuelType.SPECIAL)) {
                ep.hexcreating_setRemainingBurnTime(
                        min(BlazeBurnerBlockEntity.MAX_HEAT_CAPACITY, e.remainingBurnTime + targetTick)
                )
            } else {
                ep.hexcreating_setActiveFuel(FuelType.SPECIAL)
                ep.hexcreating_setRemainingBurnTime(
                        min(BlazeBurnerBlockEntity.MAX_HEAT_CAPACITY, targetTick)
                )
            }
            ep.hexcreating_playSound()
            //e.spawnParticleBurst(false)
            e.updateBlockState()
        }
    }

    companion object {
        const val NAME = "kelvin_super_heat_gambit"
        const val STROKE = "qadedade"
        val DIR = HexDir.SOUTH_WEST
    }
}