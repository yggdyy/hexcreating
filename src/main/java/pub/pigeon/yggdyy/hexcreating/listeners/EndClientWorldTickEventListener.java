package pub.pigeon.yggdyy.hexcreating.listeners;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import pub.pigeon.yggdyy.hexcreating.client.key.ModKeyBindings;

public class EndClientWorldTickEventListener implements ClientTickEvents.EndWorldTick {
    @Override
    public void onEndTick(ClientWorld world) {
        for(var listener : ModKeyBindings.keyListeners) {
            if(listener.getKey().isPressed()) listener.whileKeyDown(world);
            else listener.whileKeyUp(world);
        }
    }
}
