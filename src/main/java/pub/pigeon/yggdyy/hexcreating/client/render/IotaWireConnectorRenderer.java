package pub.pigeon.yggdyy.hexcreating.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.connector.IotaWireConnectorBlockEntity;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

public class IotaWireConnectorRenderer implements BlockEntityRenderer<IotaWireConnectorBlockEntity> {
    public BlockEntityRendererFactory.Context context;
    public IotaWireConnectorRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    public IotaWireConnectorRenderer(WorldRenderContext context) {
        
    }

    @Override
    public void render(IotaWireConnectorBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        if(blockEntity.getWorld() == null) return;

        if(!blockEntity.getWorld().getBlockState(blockEntity.getPos()).isOf(ModBlocks.INSTANCE.getIOTA_WIRE_CONNECTOR())) return;
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.UP).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(0.5f, 1.19f, 0.5f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.DOWN).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(0.5f, -0.19f, 0.5f);
            //matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.NORTH).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(0.5f, 0.5, -0.19f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.SOUTH).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(0.5f, 0.5, 1.19f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.WEST).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(-0.19f, 0.5f, 0.5f);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }
        if(blockEntity.getWorld().getBlockState(blockEntity.getPos()).get(IotaWireConnectorBlock.EAST).booleanValue()) {
            matrixStack.push();
            matrixStack.translate(1.19f, 0.5f, 0.5f);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
            matrixStack.scale(3f, 3f, 3f);
            MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(ModItems.IOTA_WIRE_CONNECTOR_TECH), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
            matrixStack.pop();
        }

        for(var c : blockEntity.getIotaWireConnectors()) {
            IotaWireRenderer.renderWires(c, tickDelta, matrixStack, vertexConsumerProvider, light, overlay);
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox(IotaWireConnectorBlockEntity blockEntity) {
        return true;
    }


}
