package pub.pigeon.yggdyy.hexcreating.patchouli.boardexplain;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexcreating.HexcreatingClient;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class AllPagesWithPattern {
    public static List<BookPage> pages = new ArrayList<>(List.of());
    public static List<ActionRegistryEntry> actions;
    public static void init(MinecraftClient client, ClientWorld world) {
        pages = new ArrayList<>(List.of());
        List<Book> books = new ArrayList<>(BookRegistry.INSTANCE.books.values());
        //HexcreatingClient.LOGGER.info("init");
        for(var book : books) {
            //book.reloadContents(world, false);
            //HexcreatingClient.LOGGER.info("book");
            List<BookEntry> entries = new ArrayList<>(book.getContents().entries.values());
            for(var entry : entries) {
                //HexcreatingClient.LOGGER.info("entry");
                for(var page : entry.getPages()) {
                    if(page.sourceObject == null) continue;
                    //HexcreatingClient.LOGGER.info("page");
                    if(page.sourceObject.has("type") && page.sourceObject.get("type").getAsString().equals("hexcasting:pattern")) {
                        pages.add(page);
                    }
                }
            }
        }
        actions = new ArrayList<>(HexActions.REGISTRY.stream().toList());
    }
    @Nullable
    public static BookPage getPage(HexPattern pattern) {
        for(var action : actions) {
            if(action.prototype().anglesSignature().equals(pattern.anglesSignature())) {
                Identifier id = HexActions.REGISTRY.getId(action);
                if(id == null) continue;
                String idS = id.toString();
                for(var page : pages) {
                    if(page.sourceObject.has("op_id") && page.sourceObject.get("op_id").getAsString().equals(idS)) {
                        return page;
                    }
                }
            }
        }
        return null;
    }
}
