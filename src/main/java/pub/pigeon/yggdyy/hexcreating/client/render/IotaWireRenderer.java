package pub.pigeon.yggdyy.hexcreating.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationCalculator;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireConnector;
import pub.pigeon.yggdyy.hexcreating.api.iotawire.IotaWireProcessor;
import pub.pigeon.yggdyy.hexcreating.client.HexcreatingRenderLayers;

public class IotaWireRenderer {
    public static void renderWires(IotaWireConnector connector, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        Vec3d relativePos0 = connector.clickBox.getCenter();
        BlockPos blockPos0 = connector.terminal.blockEntity.getPos();
        Vec3d posMeta = new Vec3d(-blockPos0.getX(), -blockPos0.getY(), -blockPos0.getZ());
        Vec3d pos0 = (new Vec3d(blockPos0.getX(), blockPos0.getY(), blockPos0.getZ())).add(relativePos0);
        for(var p : connector.others) {
            IotaWireConnector c1 = null;
            if (MinecraftClient.getInstance().world != null) {
                c1 = IotaWireProcessor.tryGetConnector(MinecraftClient.getInstance().world, p.getLeft(), p.getRight());
            }
            if(c1 == null) continue;
            var relativePos1 = c1.clickBox.getCenter();
            var blockPos1 = c1.terminal.blockEntity.getPos();
            Vec3d pos1 = (new Vec3d(blockPos1.getX(), blockPos1.getY(), blockPos1.getZ())).add(relativePos1);
            renderWire(pos0.add(posMeta), pos1.add(posMeta), tickDelta, matrixStack, vertexConsumerProvider, light, overlay);
        }
    }

    public static void renderWire(Vec3d pos0, Vec3d pos1, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        var buffer = vertexConsumerProvider.getBuffer(HexcreatingRenderLayers.WIRE);
        matrixStack.push();
        matrixStack.translate(pos0.x, pos0.y, pos0.z);

        double eps = 0.001;
        if(pos1.add(pos0.multiply(-1)).crossProduct(new Vec3d(0, 0, 1)).length() <= eps) {
            if(pos1.add(pos0.multiply(-1)).dotProduct(new Vec3d(0, 0, 1)) < 0) {
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
            }
        } else {
            matrixStack.multiply(RotationAxis.of(pos1.add(pos0.multiply(-1)).crossProduct(new Vec3d(0, 0, 1)).toVector3f()).rotation(
                    (float) -Math.acos(
                            pos1.add(pos0.multiply(-1)).dotProduct(new Vec3d(0, 0, 1)) /
                                    (pos1.add(pos0.multiply(-1)).length())
                    )
            ));
        }

        float wireLength = (float) pos0.distanceTo(pos1);

        buffer.vertex(matrixStack.peek().getPositionMatrix(), -getWidth(), -getWidth(), 0).color(0xc890f0).light(light).next();
        buffer.vertex(matrixStack.peek().getPositionMatrix(), -getWidth(), -getWidth(), wireLength).color(0xc890f0).light(light).next();
        buffer.vertex(matrixStack.peek().getPositionMatrix(), getWidth(), getWidth(), 0).color(0xc890f0).light(light).next();
        buffer.vertex(matrixStack.peek().getPositionMatrix(), getWidth(), getWidth(), wireLength).color(0xc890f0).light(light).next();

        matrixStack.pop();
        /*matrixStack.push();
        matrixStack.translate(pos1.x, pos1.y, pos1.z);
        buffer.vertex(matrixStack.peek().getPositionMatrix(), 0, 0, 0).next();
        matrixStack.pop();*/

    }

    public static float getWidth() {
        return 0.01f;
    }
}
