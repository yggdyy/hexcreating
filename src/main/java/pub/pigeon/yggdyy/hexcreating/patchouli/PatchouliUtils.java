package pub.pigeon.yggdyy.hexcreating.patchouli;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import pub.pigeon.yggdyy.hexcreating.mixins.BookTextParserAccessor;
import vazkii.patchouli.client.book.text.BookTextParser;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class PatchouliUtils {
    public static String deleteCommand(String str) {
        return str.replaceAll("\\$\\([^)]*\\)", "").replaceAll("/\\$", "");
    }
    public static void drawLinedString(DrawContext drawContext, String str, int lineWidth, int lineHeight, int x, int y, Function<MutableText, MutableText> formatter) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        List<OrderedText> lines = textRenderer.wrapLines(formatter.apply(Text.literal(str).copy()), lineWidth);
        for(int i = 0; i < lines.size(); ++i) {
            drawContext.drawText(
                    textRenderer,
                    lines.get(i),
                    x,
                    y + i * lineHeight,
                    0,
                    true
            );
        }
    }
}
