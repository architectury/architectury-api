package dev.architectury.mixin.fabric;

import net.minecraft.resources.RegistryDataLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public interface RegistryDataLoaderAccessor {
    
    @Mutable
    @Accessor("WORLDGEN_REGISTRIES")
    static void setWorldgenRegistries(List<RegistryDataLoader.RegistryData<?>> list) {
        throw new UnsupportedOperationException();
    }
}
