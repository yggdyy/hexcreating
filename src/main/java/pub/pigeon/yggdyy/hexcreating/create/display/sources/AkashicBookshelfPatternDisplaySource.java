package pub.pigeon.yggdyy.hexcreating.create.display.sources;

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class AkashicBookshelfPatternDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if(context.getSourceBlockEntity() instanceof BlockEntityAkashicBookshelf be) {
            var pattern = be.getPattern();
            if(pattern == null) return Text.empty();
            //context.level().getPlayers().get(0).sendMessage(Text.literal("yggdyy_"));
            //context.level().getPlayers().get(0).sendMessage(Text.literal(pattern.anglesSignature()));
            return Text.literal(pattern.toString());
        }
        return Text.empty();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
