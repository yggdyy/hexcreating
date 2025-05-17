package pub.pigeon.yggdyy.hexcreating.items;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrintedPaperItem extends Item implements IotaHolderItem {
    public PrintedPaperItem(Settings settings) {
        super(settings);
    }
    private static final String IOTA_KEY = "iota";
    @Override
    public @Nullable NbtCompound readIotaTag(ItemStack stack) {
        return NBTHelper.getCompound(stack, IOTA_KEY);
    }
    @Override
    public boolean writeable(ItemStack stack) {
        return false;
    }
    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return false;
    }
    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if(iota == null) return;
        stack.getOrCreateNbt().put(IOTA_KEY, IotaType.serialize(iota));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        IotaHolderItem.appendHoverText(
                this,
                itemStack,
                list,
                tooltipContext
        );
    }
}
