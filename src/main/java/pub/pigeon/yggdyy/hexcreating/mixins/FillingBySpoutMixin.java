package pub.pigeon.yggdyy.hexcreating.mixins;

import com.simibubi.create.content.fluids.spout.FillingBySpout;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pub.pigeon.yggdyy.hexcreating.create.spout.ChargingBySpout;

@Mixin(value = FillingBySpout.class)
public class FillingBySpoutMixin {
    //@Inject(method = "canItemBeFilled", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/GenericItemFilling;canItemBeFilled(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    @Inject(method = "canItemBeFilled", at = @At("HEAD"), cancellable = true)
    private static void canItemBeCharged(World world, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(ChargingBySpout.isItemMediaChargeable(world, stack)) {
            cir.setReturnValue(true);
        }
    }

    //@Inject(method = "getRequiredAmountForItem", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/fluids/transfer/GenericItemFilling;getRequiredAmountForItem(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lio/github/fabricators_of_create/porting_lib/fluids/FluidStack;)Z"), cancellable = true)
    @Inject(method = "getRequiredAmountForItem", at = @At("HEAD"), cancellable = true)
    private static void getRequiredMedia(World world, ItemStack stack, FluidStack fStack, CallbackInfoReturnable<Long> cir) {
        long media = ChargingBySpout.getItemRequiredMedia(world, stack, fStack);
        if(media > 0) cir.setReturnValue(media);
    }

    @Inject(method = "fillItem", at = @At("HEAD"), cancellable = true)
    private static void chargeItem(World world, long requiredAmount, ItemStack stack, FluidStack fStack, CallbackInfoReturnable<ItemStack> cir) {
        var res = ChargingBySpout.chargeItem(world, requiredAmount, stack, fStack);
        if(res != null) cir.setReturnValue(res);
    }
}
