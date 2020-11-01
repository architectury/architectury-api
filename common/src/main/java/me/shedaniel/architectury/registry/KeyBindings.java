package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;

@Environment(EnvType.CLIENT)
public final class KeyBindings {
    private KeyBindings() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static void registerKeyBinding(KeyMapping binding) {
        IMPL.registerKeyBinding(binding);
    }
    
    public interface Impl {
        void registerKeyBinding(KeyMapping binding);
    }
    
    static {
        ArchitecturyPopulator.populate(KeyBindings.class);
    }
}
