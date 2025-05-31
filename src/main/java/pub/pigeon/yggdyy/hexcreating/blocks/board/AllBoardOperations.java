package pub.pigeon.yggdyy.hexcreating.blocks.board;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.libs.PlayerInventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class AllBoardOperations {
    @FunctionalInterface
    public interface IBoardOperation {
        //Return whether the operation has conducted successfully?
        //When an operation returned true, the following operations will not try to run.
        boolean operate(ServerWorld world, BlockPos blockPos, BlockState blockState, BoardBlockEntity be, int slot, ItemStack bStack, BoardConnected connected, int condition, PlayerEntity playerEntity, ItemStack pStack);
    }
    public static List<IBoardOperation> operations = new ArrayList<>(List.of());
    public static void add(IBoardOperation operation) {operations.add(operation);}
    public static void init() {
        //simple remove
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 0 && pStack.isEmpty() && !bStack.isEmpty()) {
                PlayerInventoryHelper.giveOrDrop(playerEntity, bStack.split(1));
                be.sync();
                return true;
            }
            return false;
        });
        //section remove
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 1 && pStack.isEmpty() && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getLeftAndRight(connected.getIndex(blockPos, slot));
                if(section != null) {
                    for(int i = section.l; i <= section.r; ++i) {
                        var nowStack = connected.getSlotHandler(i).getStack();
                        if(nowStack.isEmpty()) continue;
                        //playerEntity.giveItemStack(nowStack.copy());
                        PlayerInventoryHelper.giveOrDrop(playerEntity, nowStack.copy());
                        connected.getSlotHandler(i).setStack(ItemStack.EMPTY);
                    }
                    return true;
                }
            }
            return false;
        });
        //section left
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 2 && pStack.isEmpty() && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getLeft(connected.getIndex(blockPos, slot));
                if(section != null) {
                    section.moveLeft();
                    return true;
                }
            }
            return false;
        });
        //section right
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 3 && pStack.isEmpty() && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getRight(connected.getIndex(blockPos, slot));
                if(section != null) {
                    section.moveRight();
                    return true;
                }
            }
            return false;
        });
        //section up
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 4 && pStack.isEmpty() && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getLeft(connected.getIndex(blockPos, slot));
                if(section != null) {
                    int _s = connected.getSlotHandler(section.r).slot / 4;
                    while(section.canMoveLeft() && connected.getSlotHandler(section.r).slot / 4 == _s) section.moveLeft();
                    return true;
                }
            }
            return false;
        });
        //section down
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 5 && pStack.isEmpty() && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getRight(connected.getIndex(blockPos, slot));
                if(section != null) {
                    int _s = connected.getSlotHandler(section.l).slot / 4;
                    while(section.canMoveRight() && connected.getSlotHandler(section.l).slot / 4 == _s) section.moveRight();
                    return true;
                }
            }
            return false;
        });
        //section copy
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(pStack.isOf(ModItems.PAPER_REEL) && !bStack.isEmpty()) {
                BoardConnected.Section section = connected.getLeftAndRight(connected.getIndex(blockPos, slot));
                if(section != null) {
                    List<Iota> iotas = section.getPatterns();
                    if(iotas.size() > 0 && pStack.getMaxDamage() - pStack.getDamage() >= iotas.size() && ModItems.PAPER_REEL.getMedia(pStack) * 100L / MediaConstants.DUST_UNIT >= iotas.size()) {
                        pStack.setDamage(pStack.getDamage() + iotas.size());
                        ModItems.PAPER_REEL.setMedia(pStack, ModItems.PAPER_REEL.getMedia(pStack) - iotas.size() * MediaConstants.DUST_UNIT / 100L);
                        ItemStack copied = ModItems.PRINTED_PAPER.getDefaultStack();
                        ModItems.PRINTED_PAPER.writeDatum(copied, new ListIota(iotas));
                        //playerEntity.giveItemStack(copied);
                        PlayerInventoryHelper.giveOrDrop(playerEntity, copied);
                        return true;
                    }
                }
            }
            return false;
        });
        //simple put
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition <= 14 && be.isItemStackLegal(pStack) && bStack.isEmpty()) {
                be.setStack(slot, pStack.split(1));
                be.sync();
                return true;
            }
            return false;
        });
        //section paste
        add((world, blockPos, blockState, be, slot, bStack, connected, condition, playerEntity, pStack) -> {
            if(condition == 15) return false;
            for(var plaster : AllBoardPasters.pasters) {
                if(plaster.canHandle(pStack, (ServerWorld) world)) {
                    var res = plaster.getResult(pStack, (ServerWorld) world);
                    connected.genStorages();
                    List<Storage<ItemVariant>> storages = new ArrayList<>(connected.storages);
                    storages.add(PlayerInventoryStorage.of(playerEntity));
                    for(int step = 0; step < res.size(); ++step) {
                        var handler = connected.getSlotHandler(connected.getIndex(blockPos, slot) + step);
                        var matcher = res.get(step);
                        if(handler == null) {
                            playerEntity.sendMessage(Text.translatable("hexcreating.board.paster.no_space", step));
                            break;
                        }
                        if(handler.match(matcher)) continue;
                        if(!handler.getStack().isEmpty()) {
                            playerEntity.sendMessage(Text.translatable("hexcreating.board.paster.no_space", step));
                            break;
                        }
                        var matches = matcher.get();
                        boolean flag = false;
                        for(var storage : storages) {
                            for(var match : matches) {
                                try (Transaction tc = Transaction.openOuter()) {
                                    long amount = storage.extract(ItemVariant.of(match.getLeft()), 1L, tc);
                                    if(amount > 0) {
                                        handler.setStack(match.getRight());
                                        flag = true;
                                        tc.commit();
                                    }
                                }
                                if(flag) break;
                            }
                            if(flag) break;
                        }
                        if(!flag) {
                            playerEntity.sendMessage(Text.translatable("hexcreating.board.paster.no_item", step));
                            break;
                        }
                    }
                    return true;
                }
            }
            return false;
        });
    }
}
