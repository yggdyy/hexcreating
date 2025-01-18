package pub.pigeon.yggdyy.hexcreating;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlockEntity;
import pub.pigeon.yggdyy.hexcreating.client.render.IotaWireConnectorRenderer;
import pub.pigeon.yggdyy.hexcreating.client.render.IotaWriterRenderer;

public class HexcreatingClient implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("hexcreating_client");
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.IOTA_WRITER, IotaWriterRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.IOTA_WIRE_CONNECTOR, IotaWireConnectorRenderer::new);
        //LOGGER.info("YGGDYY_CLIENT!!!!!!!!!!");

        /*WorldRenderEvents.BEFORE_ENTITIES.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            World world = client.world;
            if (world != null) {
                // 获取自定义渲染器实例
                IotaWireConnectorRenderer renderer = new IotaWireConnectorRenderer(context);

                for (int x = 0; x < world.getWorldBorder().getSize(); x++) {
                    for (int y = 0; y < world.getBottomY(); y++) {
                        for (int z = 0; z < world.getWorldBorder().getSize(); z++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockEntity blockEntity = world.getBlockEntity(pos);
                            if (blockEntity instanceof IotaWireConnectorBlockEntity) {
                                // 获取 light 和 overlay 参数
                                int light = WorldRenderer.getLightmapCoordinates(world, pos);
                                int overlay = OverlayTexture.DEFAULT_UV;

                                // 使用自定义渲染器的 render 方法
                                renderer.render((IotaWireConnectorBlockEntity) blockEntity, context.tickDelta(), context.matrixStack(), context.consumers(), light, overlay);
                            }
                        }
                    }
                }
            }
        });*/
    }

    private int getLight(BlockPos pos, World world) {
        return WorldRenderer.getLightmapCoordinates(world, pos);
    }
}
