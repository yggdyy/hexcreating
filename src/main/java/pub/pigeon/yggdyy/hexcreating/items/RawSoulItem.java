package pub.pigeon.yggdyy.hexcreating.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.api.ISoulHolderItem;

import java.util.List;

public class RawSoulItem extends Item implements ISoulHolderItem {
    public static final String ID_KEY = "hexcreating:entity_id";
    public static final String AMOUNT_KEY = "hexcreating:amount";
    public RawSoulItem(Settings settings) {
        super(settings);
    }
    @Override
    public int getSoulAmount(ItemStack stack) {
        return 25;
    }
    @Override
    public boolean canChangeSoulAmount(ItemStack stack) {
        return false;
    }
    @Override
    public void setSoulAmount(ItemStack stack, int amount) {
        stack.getOrCreateNbt().putInt(AMOUNT_KEY, 25);
    }
    @Nullable
    @Override
    public Identifier getEntityId(ItemStack stack) {
        if(stack.hasNbt() && stack.getNbt().contains(ID_KEY)) {
            return Identifier.tryParse(stack.getNbt().getString(ID_KEY));
        }
        return null;
    }
    @Override
    public boolean canChangeEntityId(ItemStack stack) {
        return false;
    }
    @Override
    public void setEntityId(ItemStack stack, Identifier id) {
        stack.getOrCreateNbt().putString(ID_KEY, id.toString());
    }

    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        Identifier id = this.getEntityId(itemStack);
        if(id != null) {
            list.add(Text.translatable(id.toTranslationKey("entity")).formatted(Formatting.DARK_RED));
        }
    }
}
