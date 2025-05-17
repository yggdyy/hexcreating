package pub.pigeon.yggdyy.hexcreating.blocks.board;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class BoardConnected {
    public List<BlockPos> blockPoses = new ArrayList<>(List.of());
    public int condition = 0;
    public World world;
    public Vec3i x, y;
    public BlockState state;
    public List<Pair<BlockPos, Integer>> slots = new ArrayList<>(List.of());
    public BoardConnected(World world, BlockPos pos, BlockState state) {
        this.world = world;
        this.state = state;
        if(!state.isOf(ModBlocks.INSTANCE.getBOARD())) return;
        var surface = ((BoardBlock) ModBlocks.INSTANCE.getBOARD()).surface(state, pos);
        this.x = new Vec3i((int)surface.getSecond().x, (int)surface.getSecond().y, (int)surface.getSecond().z);
        this.y = new Vec3i((int)surface.getThird().x, (int)surface.getThird().y, (int)surface.getThird().z);
        dfs(pos);
        blockPoses.forEach(blockPos -> {
            for(int i = 0; i < 16; ++i) { // hardcode 16 (i am a bad coder
                slots.add(new Pair<>(blockPos, i));
            }
            this.condition = Math.max(world.getReceivedRedstonePower(blockPos), this.condition);
        });
        slots.sort((pair1, pair2) -> {
            BlockPos pos1 = pair1.getLeft(), pos2 = pair2.getLeft();
            int slot1 = pair1.getRight(), slot2 = pair2.getRight();
            int i1 = (int)Vec3d.of(pos1).dotProduct(Vec3d.of(x)) * 4 + slot1 % 4,
                    j1 = (int)Vec3d.of(pos1).dotProduct(Vec3d.of(y)) * 4 + slot1 / 4,
                    i2 = (int)Vec3d.of(pos2).dotProduct(Vec3d.of(x)) * 4 + slot2 % 4,
                    j2 = (int)Vec3d.of(pos2).dotProduct(Vec3d.of(y)) * 4 + slot2 / 4;
            return (j1 == j2)? i1 - i2 : j1 - j2;
        });
    }
    private void dfs(BlockPos pos) {
        if(!blockPoses.contains(pos) && world.getBlockState(pos).isOf(ModBlocks.INSTANCE.getBOARD()) && world.getBlockState(pos).get(Properties.HORIZONTAL_FACING).equals(state.get(Properties.HORIZONTAL_FACING))) {
            blockPoses.add(pos);
            dfs(pos.add(x)); dfs(pos.add(x.multiply(-1)));
            dfs(pos.add(y)); dfs(pos.add(y.multiply(-1)));
        }
    }
    public static class Section {
        public BoardConnected boards;
        public int l, r;
        public Section(BoardConnected boards, int l, int r) {
            this.boards = boards;
            if(r < l) {
                int tmp = l;
                l = r; r = tmp;
            }
            this.l = Math.max(0, l);
            this.r = Math.min(boards.slots.size() - 1, r);
        }
        public boolean canMoveRight() {
            return r + 1 < boards.slots.size() && boards.isEmpty(r + 1);
        }
        public boolean canMoveLeft() {
            return l - 1 >= 0 && boards.isEmpty(l - 1);
        }
        public void moveRight() {
            if(!canMoveRight()) return;
            for(int i = r + 1; i >= l + 1; --i) {
                boards.getSlotHandler(i).setStack(boards.getSlotHandler(i - 1).getStack());
            }
            boards.getSlotHandler(l).setStack(ItemStack.EMPTY);
            ++l; ++r;
        }
        public void moveLeft() {
            if(!canMoveLeft()) return;
            for(int i = l - 1; i <= r - 1; ++i) {
                boards.getSlotHandler(i).setStack(boards.getSlotHandler(i + 1).getStack());
            }
            boards.getSlotHandler(r).setStack(ItemStack.EMPTY);
            --l; --r;
        }
        public List<Iota> getPatterns() {
            List<Iota> ret = new ArrayList<>(List.of());
            for(int i = l; i <= r; ++i) {
                if(boards.isEmpty(i)) continue;
                ItemStack stack = boards.getSlotHandler(i).getStack();
                if(stack.getItem() instanceof IotaHolderItem item) {
                    var iota = item.readIota(stack, (ServerWorld) boards.world);
                    if(iota != null) ret.add(iota);
                }
            }
            return ret;
        }
    }
    public static class SlotHandler {
        public BoardBlockEntity be = null;
        public int slot = 0;
        public SlotHandler(World world, BlockPos pos, int slot) {
            if(world.getBlockEntity(pos) instanceof BoardBlockEntity be) {
                this.be = be;
                this.slot = slot;
            }
        }
        public SlotHandler(World world, Pair<BlockPos, Integer> pair) {
            this(world, pair.getLeft(), pair.getRight());
        }
        public ItemStack getStack() {
            if(be == null) return ItemStack.EMPTY;
            return be.getStack(slot);
        }
        public void setStack(ItemStack stack) {
            if(be == null) return;
            be.setStack(slot, stack);
        }
    }
    public int getIndex(BlockPos pos, int slot) {
        for(int i = 0; i < slots.size(); ++i) {
            if(slots.get(i).getLeft().equals(pos) && slots.get(i).getRight().equals(slot))
                return i;
        }
        return -1;
    }
    @Nullable
    public SlotHandler getSlotHandler(int index) {
        if(index < 0 || index >= slots.size()) return null;
        return new SlotHandler(world, slots.get(index));
    }
    public boolean isEmpty(int index) {
        var handler = getSlotHandler(index);
        return handler == null || handler.getStack().isEmpty();
    }
    @Nullable
    public Section getLeftAndRight(int index) {
        if(index < 0 || index >= slots.size()) return null;
        int l = index, r = index;
        while(l >= 0 && !isEmpty(l)) --l;
        ++l;
        while(r < slots.size() && !isEmpty(r)) ++r;
        --r;
        if(l <= r) return new Section(this, l, r);
        return null;
    }
    @Nullable
    public Section getLeft(int index) {
        if(index < 0 || index >= slots.size()) return null;
        int l = index;
        while(l >= 0 && !isEmpty(l)) --l;
        ++l;
        if(l <= index) return new Section(this, l, index);
        return null;
    }
    @Nullable
    public Section getRight(int index) {
        if(index < 0 || index >= slots.size()) return null;
        int r = index;
        while(r < slots.size() && !isEmpty(r)) ++r;
        --r;
        if(index <= r) return new Section(this, index, r);
        return null;
    }
}
