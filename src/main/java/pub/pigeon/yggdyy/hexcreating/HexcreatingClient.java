package pub.pigeon.yggdyy.hexcreating;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.client.render.IotaWireConnectorRenderer;
import pub.pigeon.yggdyy.hexcreating.client.render.IotaWriterRenderer;
import pub.pigeon.yggdyy.hexcreating.fluid.ModFluids;

public class HexcreatingClient implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("hexcreating_client");
    @Override
    public void onInitializeClient() {
        //hexcreating:iota_writer
        BlockEntityRendererFactories.register(ModBlockEntities.IOTA_WRITER, IotaWriterRenderer::new);

        //hexcreating:iota_wire_connector
        BlockEntityRendererFactories.register(ModBlockEntities.IOTA_WIRE_CONNECTOR, IotaWireConnectorRenderer::new);

        //hexcreating:media
        FluidRenderHandlerRegistry.INSTANCE.register(ModFluids.STILL_MEDIA, ModFluids.FLOWING_MEDIA, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0xb38ef3
        ));
        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluids.STILL_MEDIA, ModFluids.FLOWING_MEDIA);
    }
}
