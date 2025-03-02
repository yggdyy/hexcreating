package pub.pigeon.yggdyy.hexcreating.mixins;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CircleExecutionState.class)
public interface CircleExecutionStateAccessor {
    @Accessor("bounds")
    public void setBounds(Box bounds);
}
