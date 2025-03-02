package pub.pigeon.yggdyy.hexcreating.mixins;

import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CircleCastEnv.class)
public interface CircleCastInvAccessor {
    @Accessor
    CircleExecutionState getExecState();
}
