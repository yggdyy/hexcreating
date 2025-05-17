package pub.pigeon.yggdyy.hexcreating.mixins;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Final
    @Shadow
    private MinecraftClient client;
    @Inject(at = @At(value = "HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", cancellable = true)
    public void renderSquare(ItemStack stack, ModelTransformationMode transformMode, boolean invert, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if(transformMode.equals(ModelTransformationMode.GUI) && !stack.isEmpty() && stack.isOf(ModItems.SQUARE)) {
            NbtCompound nbt = ModItems.SQUARE.readIotaTag(stack);
            if(nbt != null) {
                Text text = IotaType.getDisplay(nbt);
                matrixStack.push();
                matrixStack.scale(1f / 18f, 1f / 18f, 1f / 18f);
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
                matrixStack.translate(-6, -8, -0.5);
                client.textRenderer.draw(
                        text,
                        0f, 0f,
                        0xfecbe6,
                        false,
                        matrixStack.peek().getPositionMatrix(),
                        vertexConsumerProvider,
                        TextRenderer.TextLayerType.SEE_THROUGH,
                        overlay,
                        light
                );
                matrixStack.pop();
                ci.cancel();
            }
        }
    }
}
