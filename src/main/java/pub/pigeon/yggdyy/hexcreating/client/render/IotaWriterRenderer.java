package pub.pigeon.yggdyy.hexcreating.client.render;

import at.petrak.hexcasting.common.lib.HexBlocks;
import at.petrak.hexcasting.common.lib.HexItems;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.BlockRenderView;
import pub.pigeon.yggdyy.hexcreating.HexcreatingClient;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.iotawriter.IotaWriteBlockEntity;

public class IotaWriterRenderer implements BlockEntityRenderer<IotaWriteBlockEntity> {
    public BlockEntityRendererFactory.Context context;
    public IotaWriterRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public void render(IotaWriteBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        float h = 0f, A = 0.4f;
        if(blockEntity.iotaWriteBehaviour != null && blockEntity.iotaWriteBehaviour.needWriteTime > 0) {
            h += 1f * Math.abs(blockEntity.iotaWriteBehaviour.haveWriteTime / ((float) blockEntity.iotaWriteBehaviour.needWriteTime) - 0.5f) - 0.5f;
            A += 0.5f * Math.abs(blockEntity.iotaWriteBehaviour.haveWriteTime / ((float) blockEntity.iotaWriteBehaviour.needWriteTime) - 0.5) - 0.25f;

            if(blockEntity.getWorld().getTime() % 7 == 0) {
                var p = blockEntity.getPos().toCenterPos();
                blockEntity.getWorld().addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, p.x, p.y - 0.8, p.z, 0, 0, 0);
            }
        }
        for(int i =0; i < 8; ++i) {
            int pl = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().down());
            renderRode(blockEntity, tickDelta, matrixStack, vertexConsumerProvider, pl, overlay, A, blockEntity.getDecorationRotationOmega(), i * 1f / 4f * 3.1416f, h);
        }

    }

    @Override
    public boolean rendersOutsideBoundingBox(IotaWriteBlockEntity blockEntity) {
        return true;
    }

    // x = A*sin(w*p + phi) + 0.5
    // y = A*cos(w*p + phi) + 0.5
    public void renderRode(IotaWriteBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, float A, float w, float phi, float h) {
        float wpphi = (blockEntity.getWorld().getTime() + tickDelta) * w + phi;
        float xOffSet = (float) (A * Math.sin(wpphi) + 0.5f);
        float zOffSet = (float) (A * Math.cos(wpphi) + 0.5f);
        matrixStack.push();
        matrixStack.translate(xOffSet, h + Math.sin(wpphi * 2.5f) * 0.1f, zOffSet);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        matrixStack.scale(2, 2, 2);
        MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(Items.END_ROD), ModelTransformationMode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, blockEntity.getWorld(), 0);
        matrixStack.pop();
    }
}
