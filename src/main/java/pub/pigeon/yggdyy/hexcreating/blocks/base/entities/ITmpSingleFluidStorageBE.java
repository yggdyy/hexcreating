package pub.pigeon.yggdyy.hexcreating.blocks.base.entities;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;

public interface ITmpSingleFluidStorageBE {
    default SingleVariantStorage<FluidVariant> getFluidStorage() {
        return null;
    }
}
