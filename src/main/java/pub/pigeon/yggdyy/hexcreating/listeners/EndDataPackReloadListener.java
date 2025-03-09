package pub.pigeon.yggdyy.hexcreating.listeners;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import pub.pigeon.yggdyy.hexcreating.create.basin.SoulMixingRecipes;

public class EndDataPackReloadListener implements ServerLifecycleEvents.EndDataPackReload {
    @Override
    public void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success) {
        if(success) {
            SoulMixingRecipes.generateSoulMixingRecipes(server);
        }
    }
}
