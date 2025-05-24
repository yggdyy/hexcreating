package pub.pigeon.yggdyy.hexcreating.listeners;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ModEventListeners {
    public static void init() {
        ServerLivingEntityEvents.AFTER_DEATH.register(new AfterDeathEventListener());
        ServerLifecycleEvents.SERVER_STARTED.register(new ServerStartedEventListener());
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(new EndDataPackReloadListener());
        ClientTickEvents.END_WORLD_TICK.register(new EndClientWorldTickEventListener());
    }
}
