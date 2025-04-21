package pub.pigeon.yggdyy.hexcreating

import com.simibubi.create.content.kinetics.BlockStressValues
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.cast.ModActions
import pub.pigeon.yggdyy.hexcreating.cast.ModIotaTypes
import pub.pigeon.yggdyy.hexcreating.create.ModStressValueProvider
import pub.pigeon.yggdyy.hexcreating.create.display.ModDisplayBehaviours
import pub.pigeon.yggdyy.hexcreating.create.train.ModPortalTracks
import pub.pigeon.yggdyy.hexcreating.fluids.ModFluids
import pub.pigeon.yggdyy.hexcreating.items.ModItems
import pub.pigeon.yggdyy.hexcreating.listeners.ModEventListeners

object HexcreatingMain : ModInitializer {
	const val MOD_ID = "hexcreating"
	@JvmField
	val LOGGER = LoggerFactory.getLogger(MOD_ID)
	//val CREATE_REGISTRY = CreateRegistrate.create(MOD_ID)

	override fun onInitialize() {
		ModEventListeners.init();
		ModActions.registerModActions()
		ModBlocks.init()
		//ModBlockCreate.init()
		ModBlockEntities.init()
		ModItems.init();
		ModFluids.init();
		ModDisplayBehaviours.init();
		BlockStressValues.registerProvider(MOD_ID, ModStressValueProvider())
		ModPortalTracks.init();
		ModIotaTypes.init();
	}
}