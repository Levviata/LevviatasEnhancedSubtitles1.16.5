package net.les.forge.init;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

import java.security.Key;

public class Keybindings {
    // Declare your keybinding object
    public static KeyMapping myCustomKey;

    public static void register() {
        // Initialize the keybinding, GLFW.GLFW_KEY_K is the default key, change as needed
        myCustomKey = new KeyMapping("key.les_keybindings.desc", GLFW.GLFW_KEY_P, "key.categories.les_keybindings");

        // Register the keybinding
        ClientRegistry.registerKeyBinding(myCustomKey);
    }
}