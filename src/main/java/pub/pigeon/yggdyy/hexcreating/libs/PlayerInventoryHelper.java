package pub.pigeon.yggdyy.hexcreating.libs;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class PlayerInventoryHelper {
    public static void giveOrDrop(PlayerEntity player, ItemStack stack) {
        try (Transaction tc = Transaction.openOuter()) {
            var inv = PlayerInventoryStorage.of(player);
            inv.offerOrDrop(ItemVariant.of(stack), stack.getCount(), tc);
            tc.commit();
        }
    }
}
