package pub.pigeon.yggdyy.hexcreating.create.display.sources;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class AkashicBookshelfIotaDisplaySource extends SingleLineDisplaySource {
    @Override
    protected MutableText provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if(context.getSourceBlockEntity() instanceof BlockEntityAkashicBookshelf be) {
            if(be.getIotaTag() == null) return Text.empty();
            var iota = IotaType.deserialize(be.getIotaTag(), (ServerWorld) context.level());
            return iota.display().copy();
        }
        return Text.empty();
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
