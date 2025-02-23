package pub.pigeon.yggdyy.hexcreating.create.display.sources;

import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class SlatePatternDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if(context.getSourceBlockEntity() instanceof BlockEntitySlate slate) {
            if(slate.pattern == null) return Text.empty();
            return Text.literal(slate.pattern.toString());
        }
        return Text.empty();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
