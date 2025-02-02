package pub.pigeon.yggdyy.hexcreating.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import pub.pigeon.yggdyy.hexcreating.libs.NotInfiniteWaterLikeFluid;

public abstract class MediaFluid extends NotInfiniteWaterLikeFluid {
    @Override
    public Fluid getFlowing() {
        return ModFluids.FLOWING_MEDIA;
    }

    @Override
    public Fluid getStill() {
        return ModFluids.STILL_MEDIA;
    }

    @Override
    public Item getBucketItem() {
        return ModFluids.MEDIA_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return ModFluids.MEDIA.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public boolean isStill(FluidState fluidState) {
        return false;
    }

    public static class Flowing extends MediaFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends MediaFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
