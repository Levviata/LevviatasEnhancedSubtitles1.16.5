package net.levviata.les.forge;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.levviata.les.common.LESCommon;
import net.levviata.les.forge.init.ClientProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LESCommon.MOD_ID)
public class LESForge {
    public LESForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(LESCommon.MOD_ID,
                FMLJavaModLoadingContext.get().getModEventBus());
        ClientProxy.preInit();
        LESCommon.init();
    }
}
