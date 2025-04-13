package pub.pigeon.yggdyy.hexcreating.mixins;

import com.simibubi.create.foundation.ponder.element.TextWindowElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(TextWindowElement.class)
public interface TextWindowElementAccessor {
    @Accessor("textGetter")
    void setTextGetter(Supplier<String> textGetter);
}
