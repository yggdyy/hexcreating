package pub.pigeon.yggdyy.hexcreating.create.display;

import at.petrak.hexcasting.common.lib.HexBlocks;
import com.simibubi.create.content.redstone.displayLink.DisplayBehaviour;
import net.minecraft.util.Identifier;
import pub.pigeon.yggdyy.hexcreating.HexcreatingMain;
import pub.pigeon.yggdyy.hexcreating.create.display.sources.AkashicBookshelfIotaDisplaySource;
import pub.pigeon.yggdyy.hexcreating.create.display.sources.AkashicBookshelfPatternDisplaySource;
import pub.pigeon.yggdyy.hexcreating.create.display.sources.ImpetusMediaDisplaySource;
import pub.pigeon.yggdyy.hexcreating.create.display.sources.SlatePatternDisplaySource;

import static com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours.*;

public class ModDisplayBehaviours {
    public static DisplayBehaviour IMPETUS_MEDIA_DISPLAY_SOURCE = register(new Identifier(HexcreatingMain.MOD_ID, "impetus_media_display_source"), new ImpetusMediaDisplaySource());
    public static DisplayBehaviour AKASHIC_BOOKSHELF_PATTERN_DISPLAY_SOURCE = register(new Identifier(HexcreatingMain.MOD_ID, "akashic_bookshelf_pattern_display_source"), new AkashicBookshelfPatternDisplaySource());
    public static DisplayBehaviour AKASHIC_BOOKSHELF_IOTA_DISPLAY_SOURCE = register(new Identifier(HexcreatingMain.MOD_ID, "akashic_bookshelf_iota_display_source"), new AkashicBookshelfIotaDisplaySource());
    public static DisplayBehaviour SLATE_PATTERN_DISPLAY_SOURCE = register(new Identifier(HexcreatingMain.MOD_ID, "slate_pattern_display_source"), new SlatePatternDisplaySource());

    public static void init() {
        assignBlock(IMPETUS_MEDIA_DISPLAY_SOURCE, HexBlocks.IMPETUS_RIGHTCLICK);
        assignBlock(IMPETUS_MEDIA_DISPLAY_SOURCE, HexBlocks.IMPETUS_REDSTONE);
        assignBlock(IMPETUS_MEDIA_DISPLAY_SOURCE, HexBlocks.IMPETUS_LOOK);
        assignBlock(AKASHIC_BOOKSHELF_PATTERN_DISPLAY_SOURCE, HexBlocks.AKASHIC_BOOKSHELF);
        assignBlock(AKASHIC_BOOKSHELF_IOTA_DISPLAY_SOURCE, HexBlocks.AKASHIC_BOOKSHELF);
        assignBlock(SLATE_PATTERN_DISPLAY_SOURCE, HexBlocks.SLATE);
    }
}
