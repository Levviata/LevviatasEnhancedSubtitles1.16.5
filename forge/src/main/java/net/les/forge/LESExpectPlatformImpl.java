package net.les.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class LESExpectPlatformImpl {
    /**
     * This is our actual method to {@link net.les.LESExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
