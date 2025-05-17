package pub.pigeon.yggdyy.hexcreating.blocks.board;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;

public class BoardBlockEntity extends BlockEntity implements Inventory, SidedInventory {
    public BoardBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BOARD, blockPos, blockState);
    }

    public final DefaultedList<ItemStack> squares = DefaultedList.ofSize(16, ItemStack.EMPTY);
    public void sync() {
        markDirty();
        world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 15);
    }
    public boolean isItemStackLegal(ItemStack stack) {
        return (!stack.isEmpty()) && stack.isOf(ModItems.SQUARE);
    }
    @Override
    public int size() {
        return squares.size();
    }
    @Override
    public boolean isEmpty() {
        for(var i : squares) {
            if(!i.isEmpty())
                return false;
        }
        return true;
    }
    @Override
    public ItemStack getStack(int i) {
        return squares.get(i);
    }
    @Override
    public ItemStack removeStack(int i, int j) {
        ItemStack result = Inventories.splitStack(squares, i, j);
        sync();
        return result;
    }
    @Override
    public ItemStack removeStack(int i) {
        return Inventories.removeStack(squares, i);
    }
    @Override
    public void setStack(int i, ItemStack itemStack) {
        if(itemStack.getCount() > 1) squares.set(i, itemStack.split(1));
        else squares.set(i, itemStack);
        sync();
    }
    @Override
    public boolean canPlayerUse(PlayerEntity playerEntity) {
        return false;
    }
    @Override
    public void clear() {
        squares.clear();
        sync();
    }
    @Override
    public int[] getAvailableSlots(Direction direction) {
        return new int[0];
    }
    @Override
    public boolean canInsert(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }
    @Override
    public boolean canExtract(int i, ItemStack itemStack, Direction direction) {
        return false;
    }
    @Override
    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        squares.clear();
        Inventories.readNbt(nbtCompound, squares);
    }
    @Override
    protected void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        Inventories.writeNbt(nbtCompound, squares);
    }
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
