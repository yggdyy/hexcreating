package pub.pigeon.yggdyy.hexcreating.create.drain;

import at.petrak.hexcasting.api.item.MediaHolderItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import com.simibubi.create.foundation.utility.Pair;
import io.github.fabricators_of_create.porting_lib.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import pub.pigeon.yggdyy.hexcreating.fluids.ModFluids;

public class MediaHolderItemEmpty {
    public static boolean canItemBeEmptied(ItemStack stack) {
        if(stack.getItem() instanceof MediaHolderItem item) {
            return item.canProvideMedia(stack) && item.getMedia(stack) > 0;
        }
        return false;
    }

    private static final long MAX_FLUID_AMOUNT = 1500 * 81;
    //should only be called when canItemBeEmptied(stack) == true
    public static Pair<FluidStack, ItemStack> emptyItem(ItemStack stack, boolean simulate) {
        var item = (MediaHolderItem) stack.getItem();
        long extractMedia = Math.min(MAX_FLUID_AMOUNT / 81 * MediaConstants.DUST_UNIT, item.getMedia(stack));
        long resFluidAmount = extractMedia * 81 / MediaConstants.DUST_UNIT;
        var resFluidStack = new FluidStack(ModFluids.STILL_MEDIA, resFluidAmount);
        var resItemStack = stack.copy();
        if(!simulate)
            stack.split(stack.getCount());
        item.withdrawMedia(resItemStack, extractMedia, false);
        return Pair.of(resFluidStack, resItemStack);
    }
}
