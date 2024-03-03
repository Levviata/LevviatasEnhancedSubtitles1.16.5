package net.levviata.les.common;

import me.shedaniel.architectury.registry.Registries;
import net.minecraft.util.LazyLoadedValue;

public class LESCommon {
    public static final String MOD_ID = "les";
    // We can use this if we don't want to use DeferredRegister
    public static final LazyLoadedValue<Registries> REGISTRIES = new LazyLoadedValue<>(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    
    public static void init() {
        
        System.out.println(ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}