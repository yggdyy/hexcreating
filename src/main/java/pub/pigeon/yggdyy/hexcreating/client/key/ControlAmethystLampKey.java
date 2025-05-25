package pub.pigeon.yggdyy.hexcreating.client.key;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import pub.pigeon.yggdyy.hexcreating.blocks.ModBlocks;
import pub.pigeon.yggdyy.hexcreating.client.screen.ControlAmethystLampScreen;

@Environment(EnvType.CLIENT)
public class ControlAmethystLampKey implements ModKeyBindings.KeyListener {
    @Override
    public KeyBinding getKey() {
        return ModKeyBindings.CONTROL_AMETHYST_LAMP;
    }
    @Override
    public void whileKeyDown(ClientWorld world) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client.player == null) return;
        ClientPlayerEntity player = client.player;
        BlockPos s = player.getBlockPos();
        BlockPos t = null;
        for(int i = s.getX() - getRadius(); i <= s.getX() + getRadius(); ++i) {
            for(int j = s.getY() - getRadius(); j <= s.getY() + getRadius(); ++j) {
                for(int k = s.getZ() - getRadius(); k <= s.getZ() + getRadius(); ++k) {
                    BlockPos n = new BlockPos(i, j, k);
                    if(world.getBlockState(n).isOf(ModBlocks.INSTANCE.getAMETHYST_LAMP())) {
                        if(t == null || s.toCenterPos().add(t.toCenterPos().multiply(-1)).lengthSquared() > s.toCenterPos().add(n.toCenterPos().multiply(-1)).lengthSquared()) {
                            t = n;
                        }
                    }
                }
            }
        }
        if(t != null) {
            client.setScreen(new ControlAmethystLampScreen(t));
        }
    }
    @Override
    public void whileKeyUp(ClientWorld world) {

    }
    private int getRadius() {return 8;}
}
