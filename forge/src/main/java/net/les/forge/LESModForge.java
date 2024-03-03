package net.les.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.les.LESMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LESMod.MOD_ID)
public class LESModForge {
    public LESModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(LESMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        LESMod.init();
    }

}
