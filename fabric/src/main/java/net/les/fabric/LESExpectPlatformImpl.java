package net.les.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class LESExpectPlatformImpl {
    /**
     * This is our actual method to {@link net.les.LESExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
