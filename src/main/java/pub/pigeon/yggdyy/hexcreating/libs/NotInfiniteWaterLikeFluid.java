package pub.pigeon.yggdyy.hexcreating.libs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public abstract class NotInfiniteWaterLikeFluid extends FlowableFluid {
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }
    @Override
    protected boolean isInfinite(World world) {
        return false;
    }
    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
        @Nullable final BlockEntity blockEntity = world.getBlockEntity(pos);
        Block.dropStacks(state, world, pos, blockEntity);
    }
    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
        return false;
    }
    @Override
    protected int getFlowSpeed(WorldView worldView) {
        return 4;
    }
    @Override
    protected int getLevelDecreasePerBlock(WorldView worldView) {
        return 1;
    }
    @Override
    public int getTickRate(WorldView worldView) {
        return 5;
    }
    @Override
    protected float getBlastResistance() {
        return 100.0F;
    }
}
