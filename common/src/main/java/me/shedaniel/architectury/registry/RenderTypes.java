package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
public final class RenderTypes {
    private RenderTypes() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static void register(RenderType type, Block... blocks) {
        IMPL.register(type, blocks);
    }
    
    public static void register(RenderType type, Fluid... fluids) {
        IMPL.register(type, fluids);
    }
    
    public interface Impl {
        void register(RenderType type, Block... blocks);
        
        void register(RenderType type, Fluid... fluids);
    }
    
    static {
        ArchitecturyPopulator.populate(RenderTypes.class);
    }
}

