package pub.pigeon.yggdyy.hexcreating.fluids;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class SoulFluid extends FlowableFluid {
        public static String ID_KEY = "entity_id";
        @Override
        protected BlockState toBlockState(FluidState state) {
            return Blocks.AIR.getDefaultState();
        }
        @Override
        public Item getBucketItem() {
            return null;
        }
        @Override
        protected boolean isInfinite(World world) {
            return false;
        }

        @Override
        protected int getFlowSpeed(WorldView world) {
            return 0;
        }
        @Override
        public Fluid getFlowing() {
            return ModFluids.FLOWING_SOUL;
        }
        @Override
        public Fluid getStill() {
            return ModFluids.STILL_SOUL;
        }
    @Override
    protected void beforeBreakingBlock(WorldAccess worldAccess, BlockPos blockPos, BlockState blockState) {

    }
    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return 1;
    }
    @Override
    public int getLevel(FluidState fluidState) {
        return 1;
    }
    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }
    @Override
    public int getTickRate(WorldView worldView) {
        return 0;
    }
    @Override
    protected float getBlastResistance() {
        return 100;
    }
    public static class Still extends SoulFluid {
        @Override public boolean isStill(FluidState state) { return true; }
    }
    public static class Flowing extends SoulFluid {
        @Override public boolean isStill(FluidState state) { return false; }
    }
}
