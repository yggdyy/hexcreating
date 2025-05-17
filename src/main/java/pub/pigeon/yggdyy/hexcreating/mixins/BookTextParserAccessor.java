package pub.pigeon.yggdyy.hexcreating.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.patchouli.client.book.text.BookTextParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Mixin(BookTextParser.class)
public interface BookTextParserAccessor {
    @Accessor
    static List<BookTextParser.CommandLookup> getCOMMAND_LOOKUPS() {
        throw new UnsupportedOperationException();
    }
}
