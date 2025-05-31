package pub.pigeon.yggdyy.hexcreating.patchouli.boardexplain;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.blocks.board.BoardBlock;
import pub.pigeon.yggdyy.hexcreating.blocks.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexcreating.items.ModItems;
import pub.pigeon.yggdyy.hexcreating.items.SquareItem;
import pub.pigeon.yggdyy.hexcreating.patchouli.PatchouliUtils;
import vazkii.patchouli.client.book.BookPage;

@Environment(EnvType.CLIENT)
public class BoardExplainHandler implements ClientTickEvents.EndTick, HudRenderCallback {
    private boolean shouldRender = false;
    private BookPage page = null;
    private MinecraftClient mc = null;
    @Override
    public void onEndTick(MinecraftClient client) {
        mc = client;
        if(client.world != null && client.world.getTime() % 200 == 0 && (AllPagesWithPattern.pages == null || AllPagesWithPattern.pages.size() == 0)) AllPagesWithPattern.init(client, client.world);
        if(client.getCameraEntity() != null) {
            World world = client.world;
            if(world == null) {shouldRender = false; return;}
            var player = client.getCameraEntity();
            boolean flag = false;
            for(var stack : player.getHandItems()) {
                if (!stack.isEmpty() && stack.isOf(HexItems.SCRYING_LENS)) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                shouldRender = false;
                return;
            }
            if (client.crosshairTarget == null) {shouldRender = false; return;}
            BlockPos blockPos = BlockPos.ofFloored(client.crosshairTarget.getPos());
            if(world.getBlockState(blockPos).isOf(ModBlocks.INSTANCE.getBOARD()) && world.getBlockEntity(blockPos) instanceof BoardBlockEntity be) {
                Vec3d s = player.getEyePos(), n = Vec3d.fromPolar(player.getPitch(), player.getYaw());
                BoardBlock b = (BoardBlock) ModBlocks.INSTANCE.getBOARD();
                BlockState state = world.getBlockState(blockPos);
                int slot = b.getSlot(state, blockPos, s, n);
                ItemStack stack = be.getStack(slot);
                if(!stack.isEmpty() && stack.isOf(ModItems.SQUARE)) {
                    SquareItem item = ModItems.SQUARE;
                    var nbt = item.readIotaTag(stack);
                    if(nbt != null) {
                        var data = nbt.get(HexIotaTypes.KEY_DATA);
                        if(data != null) {
                            HexPattern pattern = PatternIota.deserialize(data).getPattern();
                            BookPage page1 = AllPagesWithPattern.getPage(pattern);
                            if(page1 != null) {
                                page = page1;
                                shouldRender = true;
                                return;
                            }
                        }
                    }
                }
            }
        }
        shouldRender = false;
    }
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        if(shouldRender && page != null && mc != null) {
            if(page.sourceObject != null && page.sourceObject.has("text")) {
                String key = page.sourceObject.get("text").getAsString();
                String text = PatchouliUtils.deleteCommand(Text.translatable(key).getString());
                PatchouliUtils.drawLinedString(drawContext, text, 180, 9, 1, 35, mutableText -> mutableText.formatted(Formatting.LIGHT_PURPLE));

            }
            if(page.sourceObject != null && page.sourceObject.has("op_id")) {
                String key = "hexcasting.action." + page.sourceObject.get("op_id").getAsString();
                drawContext.drawText(
                        mc.textRenderer,
                        Text.translatable(key).copy().formatted(Formatting.DARK_PURPLE).formatted(Formatting.UNDERLINE),
                        1, 1, 0, true
                );
            }
            if(page.sourceObject != null && page.sourceObject.has("input")) {
                String key = page.sourceObject.get("input").getAsString();
                drawContext.drawText(
                        mc.textRenderer,
                        Text.literal(key).copy().formatted(Formatting.RED),
                        1, 12, 0, false
                );
            }
            if(page.sourceObject != null && page.sourceObject.has("output")) {
                String key = page.sourceObject.get("output").getAsString();
                drawContext.drawText(
                        mc.textRenderer,
                        Text.literal(key).copy().formatted(Formatting.GREEN),
                        1, 23, 0, false
                );
            }
        }
    }
}
