package pub.pigeon.yggdyy.hexcreating.items;

import at.petrak.hexcasting.api.item.MediaHolderItem;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.utils.MathUtils;
import at.petrak.hexcasting.api.utils.MediaHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class PaperReelItem extends Item implements MediaHolderItem {
    public PaperReelItem(Settings settings) {
        super(settings);
    }
    public static final TextColor HEX_COLOR = TextColor.fromRgb(0xb38ef3);
    private static final DecimalFormat PERCENTAGE = new DecimalFormat("####");
    static {
        PERCENTAGE.setRoundingMode(RoundingMode.DOWN);
    }
    private static final DecimalFormat DUST_AMOUNT = new DecimalFormat("###,###.##");
    @Override
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> list, TooltipContext tooltipContext) {
        super.appendTooltip(itemStack, world, list, tooltipContext);
        var maxMedia = getMaxMedia(itemStack);
        if (maxMedia > 0) {
            var media = getMedia(itemStack);
            var fullness = getMediaFullness(itemStack);
            var color = TextColor.fromRgb(MediaHelper.mediaBarColor(media, maxMedia));
            var mediamount = Text.literal(DUST_AMOUNT.format(media / (float) MediaConstants.DUST_UNIT));
            var percentFull = Text.literal(PERCENTAGE.format(100f * fullness) + "%");
            var maxCapacity = Text.translatable("hexcasting.tooltip.media", DUST_AMOUNT.format(maxMedia / (float) MediaConstants.DUST_UNIT));
            mediamount.styled(style -> style.withColor(HEX_COLOR));
            maxCapacity.styled(style -> style.withColor(HEX_COLOR));
            percentFull.styled(style -> style.withColor(color));
            list.add(
                    Text.translatable("hexcasting.tooltip.media_amount.advanced",
                            mediamount, maxCapacity, percentFull));
        }
    }
    @Override
    public ItemStack getDefaultStack() {
        ItemStack ret = super.getDefaultStack();
        ret.setDamage(0);
        setMedia(ret, 0);
        return ret;
    }
    public static final String MEDIA_KEY = "media";
    @Override
    public long getMedia(ItemStack stack) {
        return (stack.hasNbt() && stack.getNbt().contains(MEDIA_KEY))? stack.getNbt().getLong(MEDIA_KEY) : 0;
    }
    @Override
    public long getMaxMedia(ItemStack stack) {
        return MediaConstants.DUST_UNIT * 64L;
    }
    @Override
    public void setMedia(ItemStack stack, long media) {
        stack.getOrCreateNbt().putLong(MEDIA_KEY, MathUtils.clamp(media, 0, getMaxMedia(stack)));
    }
    @Override
    public boolean canProvideMedia(ItemStack stack) {
        return false;
    }
    @Override
    public boolean canRecharge(ItemStack stack) {
        return true;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if(world == null) return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        ItemStack stack1 = playerEntity.getStackInHand(hand), stack2 = playerEntity.getStackInHand(hand.equals(Hand.MAIN_HAND)? Hand.OFF_HAND : Hand.MAIN_HAND);
        if(stack1.getDamage() >= 128 && stack2.isOf(Items.PAPER)) {
            stack2.split(1);
            if(world.isClient) {
                stack1.setDamage(stack1.getDamage() - 128);
                stack1.setNbt(stack1.getNbt());
                playerEntity.playSound(SoundEvents.ITEM_BOOK_PAGE_TURN, 1f, 1f);
                return TypedActionResult.success(stack1.copy());
            }
        }
        return TypedActionResult.pass(stack1);
    }
}
