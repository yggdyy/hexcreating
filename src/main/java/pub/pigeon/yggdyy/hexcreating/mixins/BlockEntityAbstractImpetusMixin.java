package pub.pigeon.yggdyy.hexcreating.mixins;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.misc.MediaConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import org.spongepowered.asm.mixin.Unique;
import pub.pigeon.yggdyy.hexcreating.blocks.base.entities.ITmpSingleFluidStorageBE;
import pub.pigeon.yggdyy.hexcreating.fluids.ModFluids;

//@Mixin(BlockEntityAbstractImpetus.class)
public abstract class BlockEntityAbstractImpetusMixin implements ITmpSingleFluidStorageBE {
    @Unique
    private final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }
        @Override
        protected long getCapacity(FluidVariant variant) {
            return FluidConstants.BUCKET / 81L;
        }
        @Override
        protected boolean canInsert(FluidVariant variant) {
            return variant.isOf(ModFluids.STILL_MEDIA);
        }
        @Override
        protected void onFinalCommit() {
            super.onFinalCommit();
            var be = (BlockEntityAbstractImpetus) (Object) BlockEntityAbstractImpetusMixin.this;
            if(be.hasWorld() && !be.getWorld().isClient) {
                long mediaToInsert = getAmount() * MediaConstants.DUST_UNIT;
                be.setMedia(Math.min(9_000_000_000_000_000_000L, be.getMedia() + mediaToInsert));
                this.amount = 0;
            }
        }
    };

    @Override
    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return fluidStorage;
    }
}
