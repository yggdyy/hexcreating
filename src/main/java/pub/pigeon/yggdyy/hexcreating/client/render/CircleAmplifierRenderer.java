package pub.pigeon.yggdyy.hexcreating.client.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.circle_amplifier.CircleAmplifierBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.circle_amplifier.CircleAmplifierBlockEntity;

public class CircleAmplifierRenderer implements BlockEntityRenderer<CircleAmplifierBlockEntity> {
    public BlockEntityRendererFactory.Context context;
    public CircleAmplifierRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    public static final Identifier MODEL_ID = new Identifier(HexcreatingMain.MOD_ID, "block/circle_amplifier_work");
    private static final double PI = 3.141592653589793238463;
    @Override
    public void render(CircleAmplifierBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        if(!blockEntity.hasWorld()) return;
        BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(MODEL_ID);
        if(model == null) model = MinecraftClient.getInstance().getBakedModelManager().getMissingModel();

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(
                RenderLayers.getEntityBlockLayer(blockEntity.getCachedState(), false)
        );
        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        BlockState state = blockEntity.getCachedState();
        Direction facing = state.get(CircleAmplifierBlock.FACING);
        if(facing.equals(Direction.DOWN)) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        else if(facing.equals(Direction.NORTH)) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        else if(facing.equals(Direction.SOUTH)) matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
        else if(facing.equals(Direction.WEST)) matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90));
        else if(facing.equals(Direction.EAST)) matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90));
        double omega = blockEntity.getSpeed() * PI / 600.0;
        if(facing.equals(Direction.DOWN) || facing.equals(Direction.NORTH) || facing.equals(Direction.WEST)) omega *= -1;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) ((blockEntity.getWorld().getTime() % 1200 + tickDelta) * omega)));
        matrixStack.translate(-0.5, -0.5, -0.5);

        World world = blockEntity.getWorld();
        BlockPos pos = blockEntity.getPos();
        int packedLight = Math.max(WorldRenderer.getLightmapCoordinates(world, pos.up()), light);
        context.getRenderManager().getModelRenderer()
                .render(
                        matrixStack.peek(),
                        vertexConsumer,
                        state,
                        model,
                        1f, 1f, 1f,
                        packedLight,
                        overlay
                );
        matrixStack.pop();
    }
}
