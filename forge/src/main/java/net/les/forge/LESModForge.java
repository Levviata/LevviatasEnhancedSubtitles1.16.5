package net.les.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.les.LESMod;
import net.les.forge.gui.SubtitleOverlayGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LESMod.MOD_ID)
public class LESModForge {
    public LESModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(LESMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(SubtitleOverlayGui.class);
        MinecraftForge.EVENT_BUS.unregister(new SubtitleOverlay(Minecraft.getInstance()));
        LESMod.init();
    }

}
