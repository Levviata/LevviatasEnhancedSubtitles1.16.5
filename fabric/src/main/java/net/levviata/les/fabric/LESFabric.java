package net.levviata.les.fabric;

import net.fabricmc.api.ModInitializer;
import net.levviata.les.common.LESCommon;

public class LESFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LESCommon.init();
    }
}
