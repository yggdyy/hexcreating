package pub.pigeon.yggdyy.hexcreating.blocks.train_gate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlockEntities;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;

import java.util.function.Function;

public class TrainGateCoreBlockEntity extends BlockEntity {
    public TrainGateCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TRAIN_GATE_CORE, blockPos, blockState);
    }

    @Nullable
    public BlockPos toPos = null;
    private static final String TO_POS_KEY = "hexcreating:to_pos";
    @Nullable public Direction.Axis gateAxis = null;
    private static final String GATE_AXIS_KEY = "hexcreating:axis";
    public int gateWidth = 0;
    private static final String GATE_WIDTH_KEY = "hexcreating:gate_width";
    public int gateHeight = 0;
    private static final String GATE_HEIGHT_kEY = "hexcreating:gate_height";

    @Override
    protected void writeNbt(NbtCompound nbtCompound) {
        super.writeNbt(nbtCompound);
        if(toPos != null) nbtCompound.putLong(TO_POS_KEY, toPos.asLong());
        if(gateAxis != null) nbtCompound.putString(GATE_AXIS_KEY, gateAxis.getName());
        if(gateWidth > 0 && gateHeight > 0) {
            nbtCompound.putInt(GATE_WIDTH_KEY, gateWidth);
            nbtCompound.putInt(GATE_HEIGHT_kEY, gateHeight);
        }
    }
    @Override
    public void readNbt(NbtCompound nbtCompound) {
        super.readNbt(nbtCompound);
        if(nbtCompound.contains(TO_POS_KEY)) toPos = BlockPos.fromLong(nbtCompound.getLong(TO_POS_KEY));
        if(nbtCompound.contains(GATE_AXIS_KEY)) gateAxis = Direction.Axis.fromName(nbtCompound.getString(GATE_AXIS_KEY));
        if(nbtCompound.contains(GATE_WIDTH_KEY) && nbtCompound.contains(GATE_HEIGHT_kEY)) {
            gateWidth = nbtCompound.getInt(GATE_WIDTH_KEY);
            gateHeight = nbtCompound.getInt(GATE_HEIGHT_kEY);
        }
    }

    private void forEachGatePos(Function<BlockPos, Object> fun) {
        if(gateAxis == null) return;
        else if(gateAxis.equals(Direction.Axis.X)) {
            for(int i = pos.getX() - (gateWidth - 1) / 2; i <= pos.getX() + (gateWidth - 1) / 2; ++i) {
                for(int j = pos.getY() + 1; j <= pos.getY() + gateHeight; ++j) {
                    fun.apply(new BlockPos(i, j, pos.getZ()));
                }
            }
        } else if(gateAxis.equals(Direction.Axis.Z)) {
            for(int k = pos.getZ() - (gateWidth - 1) / 2; k <= pos.getZ() + (gateWidth - 1) / 2; ++k) {
                for(int j = pos.getY() + 1; j <= pos.getY() + gateHeight; ++j) {
                    fun.apply(new BlockPos(pos.getX(), j, k));
                }
            }
        }
    }
    private void uncheckedDestroyGate() {
        forEachGatePos(blockPos -> {
            if(world.getBlockState(blockPos).isOf(ModBlocks.INSTANCE.getTRAIN_GATE()))
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
            return null;
        });
        gateAxis = null;
        gateWidth = gateHeight = 0;
        markDirty();
    }
    public boolean tryDestroyGate() {
        if(world == null || world.isClient) return false;
        if(gateAxis == null) return false;
        uncheckedDestroyGate();
        var other = world.getBlockEntity(toPos);
        if(other instanceof TrainGateCoreBlockEntity be)
            be.tryDestroyGate();
        toPos = null;
        markDirty();
        return true;
    }
    public boolean isInGate(BlockPos _pos) {
        if(gateAxis == null) return false;
        return getGateRange().contains(_pos);
    }
    private int availableHeight(Direction.Axis axis, int width) {
        if(axis.equals(Direction.Axis.X)) {
            for(int j = pos.getY() + 1; j <= pos.getY() + width + 1; ++j) {
                for (int i = pos.getX() - (width - 1) / 2; i <= pos.getX() + (width - 1) / 2; ++i) {
                    if(!world.getBlockState(new BlockPos(i, j, pos.getZ())).isOf(Blocks.AIR)) return j - pos.getY() - 1;
                }
            }
        } else if(axis.equals(Direction.Axis.Z)) {
            for(int j = pos.getY() + 1; j <= pos.getY() + width + 1; ++j) {
                for (int k = pos.getZ() - (width - 1) / 2; k <= pos.getZ() + (width - 1) / 2; ++k) {
                    if(!world.getBlockState(new BlockPos(pos.getX(), j, k)).isOf(Blocks.AIR)) return j - pos.getY() - 1;
                }
            }
        }
        return width + 1;
    }
    private int availableWidth(Direction.Axis axis) {
        if(axis.equals(Direction.Axis.X)) {
            for(int i = 1; i <= 10; ++i) {
                BlockPos pos1 = new BlockPos(pos.getX() - i, pos.getY(), pos.getZ()),
                        pos2 = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ());
                if(!(
                        world.getBlockState(pos1).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_FRAME()) &&
                                world.getBlockState(pos1.add(0, 1, 0)).isOf(Blocks.AIR) &&
                                world.getBlockState(pos2).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_FRAME()) &&
                                world.getBlockState(pos2.add(0, 1, 0)).isOf(Blocks.AIR)
                        )) return 2 * (i - 1) + 1;
            }
        } else if(axis.equals(Direction.Axis.Z)) {
            for(int k = 1; k <= 10; ++k) {
                BlockPos pos1 = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - k),
                        pos2 = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + k);
                if(!(
                        world.getBlockState(pos1).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_FRAME()) &&
                                world.getBlockState(pos1.add(0, 1, 0)).isOf(Blocks.AIR) &&
                                world.getBlockState(pos2).isOf(ModBlocks.INSTANCE.getTRAIN_GATE_FRAME()) &&
                                world.getBlockState(pos2.add(0, 1, 0)).isOf(Blocks.AIR)
                )) return 2 * (k - 1) + 1;
            }
        }
        return 0;
    }
    private Pair<Direction.Axis, Integer> availableAxisAndWidth() {
        int xW = availableWidth(Direction.Axis.X), zW = availableWidth(Direction.Axis.Z);
        return xW >= zW? new Pair<>(Direction.Axis.X, xW) : new Pair<>(Direction.Axis.Z, zW);
    }
    public boolean canSetUpGate() {
        var p = availableAxisAndWidth();
        int h = availableHeight(p.getLeft(), p.getRight());
        return h > 0;
    }

    /*
    this method is used to set up a train gate with a given other gate core's vector
    present gate will be destroyed regardless of the result
    the other gate will not be checked, this should be done before
     */
    public void setUpGate(BlockPos other) {
        if(world == null || world.isClient) return;
        tryDestroyGate();
        if(!canSetUpGate()) return;
        var p = availableAxisAndWidth();
        int h = availableHeight(p.getLeft(), p.getRight());
        gateAxis = p.getLeft();
        gateWidth = p.getRight();
        gateHeight = h;
        toPos = other;
        forEachGatePos(blockPos -> {
            world.setBlockState(blockPos, ModBlocks.INSTANCE.getTRAIN_GATE().getDefaultState().with(Properties.HORIZONTAL_AXIS, gateAxis));
            return null;
        });
        markDirty();
    }

    @Nullable
    public BlockBox getGateRange() {
        if(gateAxis == null) {
            return null;
        }else if(gateAxis.equals(Direction.Axis.X)) {
            return new BlockBox(pos.getX() - (gateWidth - 1) / 2, pos.getY() + 1, pos.getZ(), pos.getX() + (gateWidth - 1) / 2, pos.getY() + gateHeight, pos.getZ());
        } else if(gateAxis.equals(Direction.Axis.Z)) {
            return new BlockBox(pos.getX(), pos.getY() + 1, pos.getZ() - (gateWidth - 1) / 2, pos.getX(), pos.getY() + gateHeight, pos.getZ() + (gateWidth - 1) / 2);
        }
        return null;
    }
}
