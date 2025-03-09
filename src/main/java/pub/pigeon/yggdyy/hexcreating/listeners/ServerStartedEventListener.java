package pub.pigeon.yggdyy.hexcreating.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import pub.pigeon.yggdyy.hexcreating.create.basin.SoulMixingRecipes;

public class ServerStartedEventListener implements ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStarted(MinecraftServer server) {
        SoulMixingRecipes.generateSoulMixingRecipes(server);
    }
}
