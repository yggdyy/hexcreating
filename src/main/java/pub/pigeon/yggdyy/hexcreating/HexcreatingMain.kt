package pub.pigeon.yggdyy.hexcreating

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks
import pub.pigeon.yggdyy.hexcreating.cast.ModActions
import pub.pigeon.yggdyy.hexcreating.items.ModItems

object HexcreatingMain : ModInitializer {
	const val MOD_ID = "hexcreating"
	@JvmField
	val LOGGER = LoggerFactory.getLogger(MOD_ID)
	//val CREATE_REGISTRY = CreateRegistrate.create(MOD_ID)

	override fun onInitialize() {
		ModActions.registerModActions()
		ModBlocks.init()
		//ModBlockCreate.init()
		ModBlockEntities.init()
		ModItems.init();
	}
}