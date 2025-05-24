package pub.pigeon.yggdyy.hexcreating.client.key;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModKeyBindings {
    public static final KeyBinding CONTROL_AMETHYST_LAMP = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.hexcreating.control_amethyst_lamp",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_CAPS_LOCK,
            "category.hexcreating.default"
    ));
    public static void init() {
        keyListeners.add(new ControlAmethystLampKey());
    }
    public static List<KeyListener> keyListeners = new ArrayList<>(List.of());
    public interface KeyListener {
        KeyBinding getKey();
        void whileKeyDown(ClientWorld world);
        void whileKeyUp(ClientWorld world);
    }
}
