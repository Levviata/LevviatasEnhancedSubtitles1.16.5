package net.les.fabric;

import net.les.LESMod;
import net.fabricmc.api.ModInitializer;

public class LESModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LESMod.init();
    }
}
