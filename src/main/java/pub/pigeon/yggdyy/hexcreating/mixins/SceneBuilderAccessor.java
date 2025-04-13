package pub.pigeon.yggdyy.hexcreating.mixins;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SceneBuilder.class)
public interface SceneBuilderAccessor {
    @Accessor("scene")
    PonderScene getScene();
}
