package net.levviata.les.forge.init;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.levviata.les.forge.gui.SubtitleOverlayGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public static void preInit() {
        MinecraftForge.EVENT_BUS.register(new SubtitleOverlayGui(Minecraft.getInstance()));
    }
}
