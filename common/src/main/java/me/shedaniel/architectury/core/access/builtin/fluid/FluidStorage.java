package me.shedaniel.architectury.core.access.builtin.fluid;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.core.access.builtin.Storage;
import me.shedaniel.architectury.core.access.combined.CombinedAccess;
import net.minecraft.Util;
import net.minecraft.world.level.material.Fluid;

public final class FluidStorage {
    public static final CombinedAccess<Storage<Fluid>> ACCESS = Util.make(CombinedAccess.create(Storage::combine), FluidStorage::linkPlatformFluid);
    
    private FluidStorage() {}
    
    @ExpectPlatform
    private static void linkPlatformFluid(CombinedAccess<Storage<Fluid>> access) {
        
    }
}
