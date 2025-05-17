package pub.pigeon.yggdyy.hexcreating.client.render;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.client.render.*;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.board.BoardBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.board.BoardBlockEntity;

public class BoardRenderer implements BlockEntityRenderer<BoardBlockEntity> {
    public static final Identifier SQUARE_ID = new Identifier(HexcreatingMain.MOD_ID, "block/square");
    public BlockEntityRendererFactory.Context context;
    public BakedModel square = MinecraftClient.getInstance().getBakedModelManager().getModel(SQUARE_ID);
    public BoardRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }
    @Override
    public void render(BoardBlockEntity be, float tickDelta, MatrixStack stack, VertexConsumerProvider provider, int light, int overlay) {
        if(be.isEmpty()) return;
        World world = be.getWorld();
        if(world == null || !world.getBlockState(be.getPos()).isOf(ModBlocks.INSTANCE.getBOARD())) return;
        BlockPos blockPos = be.getPos();
        BlockState state = world.getBlockState(blockPos);
        BoardBlock block = (BoardBlock) ModBlocks.INSTANCE.getBOARD();
        var surface = block.surface(state, blockPos);
        Vec3d o = surface.getFirst().add((blockPos.toCenterPos().add(-0.5, -0.5, -0.5)).multiply(-1)),
                x = surface.getSecond().multiply(0.25), y = surface.getThird().multiply(0.25),
                f = surface.getSecond().crossProduct(surface.getThird()).multiply(-1);
        Quaternionf q = (switch (state.get(Properties.HORIZONTAL_FACING)) {
            case NORTH -> RotationAxis.POSITIVE_Y.rotationDegrees(0);
            case SOUTH -> RotationAxis.POSITIVE_Y.rotationDegrees(180);
            case WEST -> RotationAxis.POSITIVE_Y.rotationDegrees(-90);
            case EAST -> RotationAxis.POSITIVE_Y.rotationDegrees(90);
            default -> new Quaternionf();
        }).mul(RotationAxis.of(f.toVector3f()).rotationDegrees(180));
        VertexConsumer vertexConsumer = provider.getBuffer(
                RenderLayers.getEntityBlockLayer(state, true)
        );
        int packedLight = Math.max(light, Math.max(
                WorldRenderer.getLightmapCoordinates(world, blockPos.up()),
                Math.max(
                        WorldRenderer.getLightmapCoordinates(world, blockPos.down()),
                        Math.max(
                                WorldRenderer.getLightmapCoordinates(world, blockPos.north()),
                                Math.max(
                                        WorldRenderer.getLightmapCoordinates(world, blockPos.south()),
                                        Math.max(
                                                WorldRenderer.getLightmapCoordinates(world, blockPos.west()),
                                                WorldRenderer.getLightmapCoordinates(world, blockPos.east())
                                        )
                                )
                        )
                )
        ));
        for(int i = 0; i < 16; ++i) {
            if(!be.getStack(i).isEmpty() && be.getStack(i).getItem() instanceof IotaHolderItem item) {
                stack.push();
                stack.translate(o.x, o.y, o.z);
                int r = i / 4, c = i % 4;
                stack.translate(x.x * (c), x.y * (c), x.z * (c));
                stack.translate(y.x * (r), y.y * (r), y.z * (r));
                stack.translate(f.x * 0.125, f.y * 0.125, f.z * 0.125);
                stack.scale(1f, 1f, 1f);
                stack.multiply(q);
                context.getRenderManager().getModelRenderer().render(
                        stack.peek(),
                        vertexConsumer,
                        state,
                        square,
                        1f, 1f, 1f,
                        packedLight,
                        overlay
                );
                stack.pop();
            }
        }
        for(int i = 0; i < 16; ++i) {
            if(!be.getStack(i).isEmpty() && be.getStack(i).getItem() instanceof IotaHolderItem item) {
                NbtCompound nbt = item.readIotaTag(be.getStack(i));
                if(nbt == null || IotaType.getTypeFromTag(nbt).equals(PatternIota.TYPE)) continue;
                String text = IotaType.getDisplay(nbt).getString();
                stack.push();
                stack.translate(o.x, o.y, o.z);
                int r = i / 4, c = i % 4;
                stack.translate(x.x * (c + 0.05), x.y * (c + 0.05), x.z * (c + 0.05));
                stack.translate(y.x * (r + 0.05), y.y * (r + 0.05), y.z * (r + 0.05));
                stack.translate(f.x * 0.13, f.y * 0.13, f.z * 0.13);
                stack.scale(1f / 37f, 1f / 37f, 1f / 37f);
                stack.multiply(q);
                context.getTextRenderer().draw(
                        text,
                        0f, 0f,
                        0xfecbe6,
                        false,
                        stack.peek().getPositionMatrix(),
                        provider,
                        TextRenderer.TextLayerType.SEE_THROUGH,
                        overlay,
                        packedLight
                );
                stack.pop();
            }
        }
        for(int i = 0; i < 16; ++i) {
            if(!be.getStack(i).isEmpty() && be.getStack(i).getItem() instanceof IotaHolderItem item) {
                NbtCompound nbt = item.readIotaTag(be.getStack(i));
                if(nbt == null || !IotaType.getTypeFromTag(nbt).typeName().getString().equals(PatternIota.TYPE.typeName().getString())) continue;
                var data = nbt.get(HexIotaTypes.KEY_DATA);
                if(data == null) continue;
                HexPattern pattern = PatternIota.deserialize(data).getPattern();
                stack.push();
                stack.translate(o.x, o.y, o.z);
                int r = i / 4, c = i % 4;
                stack.translate(x.x * (c), x.y * (c), x.z * (c));
                stack.translate(y.x * (r), y.y * (r), y.z * (r ));
                stack.translate(f.x * 0.13, f.y * 0.13, f.z * 0.13);
                stack.scale(1f / 4f, 1f / 4f, 1f / 4f);
                stack.multiply(q);
                PatternRenderer.renderPattern(
                        pattern,
                        stack,
                        //new PatternRenderer.WorldlyBits(provider, packedLight, f),
                        WorldlyPatternRenderHelpers.READABLE_SCROLL_SETTINGS,
                        PatternColors.glowyStroke(0xff_fecbe6),
                        0,
                        256
                );
                stack.pop();
            }
        }
    }
}
