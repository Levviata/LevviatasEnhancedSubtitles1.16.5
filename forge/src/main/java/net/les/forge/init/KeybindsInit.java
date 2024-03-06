package net.les.forge.init;

import net.les.LESMod;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeybindsInit {
    public static KeyMapping openGui;

    public static void register() {
        openGui = create("openGui", GLFW.GLFW_KEY_P);

        ClientRegistry.registerKeyBinding(openGui);
    }

    private static KeyMapping create(String name, int key) {
        return new KeyMapping("key." + LESMod.MOD_ID + "." + name, key, "key.category." + LESMod.MOD_ID);
    }
}