package pub.pigeon.yggdyy.hexcreating.network.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import pub.pigeon.yggdyy.hexcreating.blocks.amethyst_lamp.AmethystLampPackHandler;

public class ModC2SPackHandlers {
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(AmethystLampPackHandler.ID, new AmethystLampPackHandler());
    }
}
