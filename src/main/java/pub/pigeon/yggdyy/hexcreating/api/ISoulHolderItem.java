package pub.pigeon.yggdyy.hexcreating.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface ISoulHolderItem {
    int getSoulAmount(ItemStack stack);
    boolean canChangeSoulAmount(ItemStack stack);
    void setSoulAmount(ItemStack stack, int amount);
    @Nullable
    Identifier getEntityId(ItemStack stack);
    boolean canChangeEntityId(ItemStack stack);
    void setEntityId(ItemStack stack, Identifier id);
}
