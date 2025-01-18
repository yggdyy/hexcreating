package pub.pigeon.yggdyy.hexcreating.blocks

import com.simibubi.create.AllBlocks
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.blocks.*
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlock
import pub.pigeon.yggdyy.hexcreating.blocks.iotareader.IotaReaderBlock
import pub.pigeon.yggdyy.hexcreating.blocks.iotawriter.IotaWriteBlock

object ModBlocks {
    val ANALYTICAL_ENGINE_CONTROLLER: Block = register("analytical_engine_controller", AnalyticalEngineControllerBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val ANALYTICAL_ENGINE_ALU: Block = register("analytical_engine_alu", AnalyticalEngineALUBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val ANALYTICAL_ENGINE_KINETIC_INTERFACE: Block = register("analytical_engine_kinetic_interface", AnalyticalEngineKineticInterfaceBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val CIRCLE_AMPLIFIER_SENDER: Block = register("circle_amplifier_sender", CircleAmplifierSenderBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_BRICKS)))
    val CIRCLE_AMPLIFIER_RECEIVER: Block = register("circle_amplifier_receiver", CircleAmplifierReceiverBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_BRICKS)))
    val IOTA_READER: Block = register("iota_reader", IotaReaderBlock(AbstractBlock.Settings.copy(AllBlocks.MECHANICAL_PRESS.get())))
    val IOTA_WRITER: Block = register("iota_writer", IotaWriteBlock(AbstractBlock.Settings.copy(AllBlocks.MECHANICAL_PRESS.get())))
    val IOTA_WIRE_CONNECTOR: Block = register("iota_wire_connector", IotaWireConnectorBlock(AbstractBlock.Settings.copy(Blocks.MEDIUM_AMETHYST_BUD)), false)

    private fun register(name: String, block: Block, withItem: Boolean = true): Block {
        val id: Identifier = Identifier(HexcreatingMain.MOD_ID, name)
        if(withItem) {
            Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    public fun init() {}
}