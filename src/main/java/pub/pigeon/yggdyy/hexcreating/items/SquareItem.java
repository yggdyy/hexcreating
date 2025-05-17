package pub.pigeon.yggdyy.hexcreating.items;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.utils.NBTHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;

public class SquareItem extends Item implements IotaHolderItem {
    public SquareItem(Settings settings) {
        super(settings);
    }
    private static final String IOTA_KEY = "iota";
    @Override
    public @Nullable NbtCompound readIotaTag(ItemStack stack) {
        return NBTHelper.getCompound(stack, IOTA_KEY);
    }
    @Override
    public boolean writeable(ItemStack stack) {
        return !(stack.hasNbt() && stack.getNbt().contains(IOTA_KEY));
    }
    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return (iota != null) && Iota.typesMatch(iota, new PatternIota(HexPattern.fromAngles("w", HexDir.EAST)));
    }
    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if(!canWrite(stack, iota)) return;
        stack.getOrCreateNbt().put(IOTA_KEY, IotaType.serialize(iota));
    }
}
