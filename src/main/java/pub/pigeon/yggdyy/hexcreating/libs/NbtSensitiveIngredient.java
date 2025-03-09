package pub.pigeon.yggdyy.hexcreating.libs;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class NbtSensitiveIngredient extends Ingredient {
    public NbtSensitiveIngredient(Stream<? extends Entry> stream) {
        super(stream);
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        //HexcreatingMain.LOGGER.debug("AAAAAAAAAAAAAAAAAAAAAAAAAA NBT sensitive ingredient has been invoked!!!!!");
        if (itemStack == null) {
            return false;
        } else if (this.isEmpty()) {
            return itemStack.isEmpty();
        } else {
            ItemStack[] matched = this.getMatchingStacks();
            int length = matched.length;

            for(int i = 0; i < length; ++i) {
                ItemStack itemStack2 = matched[i];
                if(itemStack2.hasNbt()) {
                    if(itemStack.hasNbt() && itemStack.isOf(itemStack2.getItem()) && itemStack.getNbt().equals(itemStack2.getNbt()))
                        return true;
                } else {
                    if(itemStack.isOf(itemStack2.getItem()))
                        return true;
                }
            }

            return false;
        }
    }
}
