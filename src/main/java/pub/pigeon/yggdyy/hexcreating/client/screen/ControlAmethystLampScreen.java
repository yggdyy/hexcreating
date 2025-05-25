package pub.pigeon.yggdyy.hexcreating.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

//this is the worst function i have written
@Environment(EnvType.CLIENT)
public class ControlAmethystLampScreen extends Screen {
    public BlockPos targetPos;
    public ControlAmethystLampScreen(BlockPos target) {
        super(Text.translatable("hexcreating.screen.control_amethyst_lamp.title"));
        targetPos = target;
    }
    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public void tick() {
    }
    @Override
    protected void init() {
        super.init();
        //addDrawable(new TexturedButtonWidget(20, 30, 32, 32, 0, 0, 0, new Identifier("hexcreating", "textures/block/amethyst_lamp/1.png"), 32, 32, buttonWidget -> {}));
        int cx = width / 2, cy = height / 2, ba = Math.min(width, height) / 8, bg = ba / 4;
        for(int i = 0; i < 16; ++i) {
            int dx = i % 4 - 2, dy = i / 4 - 2;
            int finalI = i;
            addDrawableChild(new TexturedButtonWidget(cx + (ba + bg) * dx, cy + (ba + bg) * dy, ba, ba, 0, 0, 0, new Identifier("hexcreating", "textures/block/amethyst_lamp/" + i + ".png"), ba, ba, buttonWidget -> onButtonClick(buttonWidget, finalI)));
        }
    }
    public void onButtonClick(ButtonWidget buttonWidget, int index) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(targetPos);
        buf.writeInt(index);
        ClientPlayNetworking.send(new Identifier("hexcreating", "update_amethyst_lamp"), buf);
        close();
    }
}
