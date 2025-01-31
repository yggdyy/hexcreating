package pub.pigeon.yggdyy.hexcreating.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineALUBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineControllerBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.AnalyticalEngineKineticInterfaceBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.analyticalengine.entities.CircleAmplifierSenderBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.iotapackobserver.IotaPackObserverBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.iotareader.IotaReaderBlockEntity;
import pub.pigeon.yggdyy.hexcreating.blocks.iotawriter.IotaWriteBlockEntity;


public class ModBlockEntities {
    public static final BlockEntityType<AnalyticalEngineControllerBlockEntity> ANALYTICAL_ENGINE_CONTROLLER = register(
            "analytical_engine_controller",
            BlockEntityType.Builder.create(AnalyticalEngineControllerBlockEntity::new, ModBlocks.INSTANCE.getANALYTICAL_ENGINE_CONTROLLER()).build(null)
    );
    public static final BlockEntityType<AnalyticalEngineALUBlockEntity> ANALYTICAL_ENGINE_ALU = register(
            "analytical_engine_alu",
            BlockEntityType.Builder.create(AnalyticalEngineALUBlockEntity::new, ModBlocks.INSTANCE.getANALYTICAL_ENGINE_ALU()).build(null)
    );
    public static final BlockEntityType<AnalyticalEngineKineticInterfaceBlockEntity> ANALYTICAL_ENGINE_KINETIC_INTERFACE = register(
            "analytical_engine_kinetic_interface",
            BlockEntityType.Builder.create(AnalyticalEngineKineticInterfaceBlockEntity::new, ModBlocks.INSTANCE.getANALYTICAL_ENGINE_KINETIC_INTERFACE()).build(null)
    );

    public static final BlockEntityType<CircleAmplifierSenderBlockEntity> CIRCLE_AMPLIFIER_SENDER = register(
            "circle_amplifier_sender",
            BlockEntityType.Builder.create(CircleAmplifierSenderBlockEntity::new, ModBlocks.INSTANCE.getCIRCLE_AMPLIFIER_SENDER()).build(null)
    );

    public static final BlockEntityType<IotaReaderBlockEntity> IOTA_READER = register(
            "iota_reader",
            BlockEntityType.Builder.create(IotaReaderBlockEntity::new, ModBlocks.INSTANCE.getIOTA_READER()).build(null)
    );

    public static final BlockEntityType<IotaWriteBlockEntity> IOTA_WRITER = register(
            "iota_writer",
            BlockEntityType.Builder.create(IotaWriteBlockEntity::new, ModBlocks.INSTANCE.getIOTA_WRITER()).build(null)
    );

    public static final BlockEntityType<IotaWireConnectorBlockEntity> IOTA_WIRE_CONNECTOR = register(
            "iota_wire_connector",
            BlockEntityType.Builder.create(IotaWireConnectorBlockEntity::new, ModBlocks.INSTANCE.getIOTA_WIRE_CONNECTOR()).build(null)
    );

    public static final BlockEntityType<IotaPackObserverBlockEntity> IOTA_PACK_OBSERVER = register(
            "iota_pack_observer",
            BlockEntityType.Builder.create(IotaPackObserverBlockEntity::new, ModBlocks.INSTANCE.getIOTA_PACK_OBSERVER()).build(null)
    );

    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(HexcreatingMain.MOD_ID, path), blockEntityType);
    }
    public static void init() {}
}