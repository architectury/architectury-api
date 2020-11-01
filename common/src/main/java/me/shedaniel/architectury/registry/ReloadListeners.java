package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public final class ReloadListeners {
    private ReloadListeners() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static void registerReloadListener(PackType type, PreparableReloadListener listener) {
        IMPL.registerReloadListener(type, listener);
    }
    
    public interface Impl {
        void registerReloadListener(PackType type, PreparableReloadListener listener);
    }
    
    static {
        ArchitecturyPopulator.populate(ReloadListeners.class);
    }
}
