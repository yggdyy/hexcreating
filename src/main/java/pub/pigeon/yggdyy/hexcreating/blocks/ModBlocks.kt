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
import pub.pigeon.yggdyy.hexcreating.blocks.circle_amplifier.CircleAmplifierBlock
import pub.pigeon.yggdyy.hexcreating.blocks.circle_inputer.CircleInputerBlock
import pub.pigeon.yggdyy.hexcreating.blocks.circle_outputer.CircleOutputerBlock
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlock
import pub.pigeon.yggdyy.hexcreating.blocks.iotapackobserver.IotaPackObserverBlock
import pub.pigeon.yggdyy.hexcreating.blocks.iotareader.IotaReaderBlock
import pub.pigeon.yggdyy.hexcreating.blocks.iotawriter.IotaWriteBlock
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateBlock
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateCoreBlock
import pub.pigeon.yggdyy.hexcreating.blocks.train_gate.TrainGateFrameBlock

object ModBlocks {
    val ANALYTICAL_ENGINE_CONTROLLER: Block = register("analytical_engine_controller", AnalyticalEngineControllerBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val ANALYTICAL_ENGINE_ALU: Block = register("analytical_engine_alu", AnalyticalEngineALUBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val ANALYTICAL_ENGINE_KINETIC_INTERFACE: Block = register("analytical_engine_kinetic_interface", AnalyticalEngineKineticInterfaceBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    //val CIRCLE_AMPLIFIER_SENDER: Block = register("circle_amplifier_sender", CircleAmplifierSenderBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_BRICKS)))
    //val CIRCLE_AMPLIFIER_RECEIVER: Block = register("circle_amplifier_receiver", CircleAmplifierReceiverBlock(AbstractBlock.Settings.copy(Blocks.DEEPSLATE_BRICKS)))
    val IOTA_READER: Block = register("iota_reader", IotaReaderBlock(AbstractBlock.Settings.copy(AllBlocks.MECHANICAL_PRESS.get())))
    val IOTA_WRITER: Block = register("iota_writer", IotaWriteBlock(AbstractBlock.Settings.copy(AllBlocks.MECHANICAL_PRESS.get())))
    val IOTA_WIRE_CONNECTOR: Block = register("iota_wire_connector", IotaWireConnectorBlock(AbstractBlock.Settings.copy(Blocks.MEDIUM_AMETHYST_BUD)), false)
    val IOTA_PACK_OBSERVER: Block = register("iota_pack_observer", IotaPackObserverBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)))
    val CIRCLE_INPUTER: Block = register("circle_inputer", CircleInputerBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val CIRCLE_OUTPUTER: Block = register("circle_outputer", CircleOutputerBlock(AbstractBlock.Settings.copy(AllBlocks.BRASS_BLOCK.get())))
    val CIRCLE_AMPLIFIER: Block = register("circle_amplifier", CircleAmplifierBlock(AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)))
    val TRAIN_GATE: Block = register("train_gate", TrainGateBlock(AbstractBlock.Settings.copy(Blocks.NETHER_PORTAL)), false)
    val TRAIN_GATE_FRAME: Block = register("train_gate_frame", TrainGateFrameBlock(AbstractBlock.Settings.copy(AllBlocks.RAILWAY_CASING.get())))
    val TRAIN_GATE_CORE: Block = register("train_gate_core", TrainGateCoreBlock(AbstractBlock.Settings.copy(TRAIN_GATE_FRAME)))

    private fun register(name: String, block: Block, withItem: Boolean = true): Block {
        val id: Identifier = Identifier(HexcreatingMain.MOD_ID, name)
        if(withItem) {
            Registry.register(Registries.ITEM, id, BlockItem(block, Item.Settings()))
        }
        return Registry.register(Registries.BLOCK, id, block);
    }

    public fun init() {}
}