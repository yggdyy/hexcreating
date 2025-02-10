package pub.pigeon.yggdyy.hexcreating.recipes.spout;

import at.petrak.hexcasting.api.item.MediaHolderItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.fluid.MediaFluid;

public class ChargingBySpout {
    public static boolean isItemMediaChargeable(World world, ItemStack stack) {
        if(stack.getItem() instanceof MediaHolderItem item) {
            if(!item.canRecharge(stack)) return false;
            long nowMedia = item.getMedia(stack), maxMedia = item.getMaxMedia(stack);
            return nowMedia < maxMedia;
        }
        return false;
    }

    public static long getItemRequiredMedia(World world, ItemStack stack, FluidStack providedFluid) {
        if(providedFluid.getFluid() instanceof MediaFluid && isItemMediaChargeable(world, stack)) {
            var item = (MediaHolderItem) stack.getItem();
            return (long) Math.min(
                    providedFluid.getAmount(),
                    Math.ceil((double)(item.getMaxMedia(stack) - item.getMedia(stack)) * 81.0 / (double) MediaConstants.DUST_UNIT)
            );
        }
        return -1;
    }

    @Nullable
    public static ItemStack chargeItem(World world, long requiredAmount, ItemStack stack, FluidStack providedFluid) {
        if(providedFluid.getFluid() instanceof MediaFluid && isItemMediaChargeable(world, stack)) {
            var res = stack.split(1);
            var item = (MediaHolderItem) res.getItem();
            item.insertMedia(res, requiredAmount * MediaConstants.DUST_UNIT / 81, false);
            providedFluid.shrink(requiredAmount);
            return res;
        }
        return null;
    }
}
