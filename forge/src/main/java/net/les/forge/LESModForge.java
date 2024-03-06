package net.les.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.les.LESMod;
import net.les.forge.config.Config;
import net.les.forge.gui.SubtitleOverlayGui;
import net.les.forge.init.KeybindsInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LESMod.MOD_ID)
public class LESModForge {

    public LESModForge() {
        EventBuses.registerModEventBus(LESMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.register(SubtitleOverlayGui.class);
        KeybindsInit.register();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC, "LevviatasEnhancedSubtitles.toml");
        LESMod.init();
    }
}
