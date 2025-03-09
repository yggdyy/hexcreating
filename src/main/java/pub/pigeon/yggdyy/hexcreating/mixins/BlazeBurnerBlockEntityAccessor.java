package pub.pigeon.yggdyy.hexcreating.mixins;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BlazeBurnerBlockEntity.class)
public interface BlazeBurnerBlockEntityAccessor {
    @Accessor("remainingBurnTime")
    int hexcreating_getRemainingBurnTime();
    @Accessor("remainingBurnTime")
    void hexcreating_setRemainingBurnTime(int remainingBurnTime);
    @Accessor("activeFuel")
    void hexcreating_setActiveFuel(BlazeBurnerBlockEntity.FuelType fuelType);
    @Invoker("playSound")
    void hexcreating_playSound();

}
