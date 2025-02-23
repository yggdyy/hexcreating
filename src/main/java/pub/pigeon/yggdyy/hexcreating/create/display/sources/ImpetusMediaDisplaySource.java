package pub.pigeon.yggdyy.hexcreating.create.display.sources;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ImpetusMediaDisplaySource extends NumericSingleLineDisplaySource{
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if(context.getSourceBlockEntity() instanceof BlockEntityAbstractImpetus be) {
            return Text.literal(be.getMedia() + "");
        } else {
            return Text.empty();
        }
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
