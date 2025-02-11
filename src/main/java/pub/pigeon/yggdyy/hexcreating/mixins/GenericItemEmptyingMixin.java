package pub.pigeon.yggdyy.hexcreating.mixins;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.foundation.utility.Pair;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pub.pigeon.yggdyy.hexcreating.recipes.drain.MediaHolderItemEmpty;

@Mixin(GenericItemEmptying.class)
public class GenericItemEmptyingMixin {
    @Inject(method = "canItemBeEmptied", at = @At("HEAD"), cancellable = true)
    private static void canItemBeEmptied(World world, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if(MediaHolderItemEmpty.canItemBeEmptied(stack))
            cir.setReturnValue(true);
    }

    @Inject(method = "emptyItem", at = @At("HEAD"), cancellable = true)
    private static void emptyItem(World world, ItemStack stack, boolean simulate, CallbackInfoReturnable<Pair<FluidStack, ItemStack>> cir) {
        if(MediaHolderItemEmpty.canItemBeEmptied(stack))
            cir.setReturnValue(MediaHolderItemEmpty.emptyItem(stack, simulate));
    }
}
