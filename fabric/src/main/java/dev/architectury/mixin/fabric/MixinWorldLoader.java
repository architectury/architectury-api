package dev.architectury.mixin.fabric;

import dev.architectury.hooks.data.fabric.DataPackRegistryHooksImpl;
import dev.architectury.registry.registries.fabric.RegistrarManagerImpl;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.WorldLoader;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(WorldLoader.class)
public class MixinWorldLoader {
    
    @Redirect(method = "load", at = @At(value = "FIELD", target = "Lnet/minecraft/resources/RegistryDataLoader;WORLDGEN_REGISTRIES:Ljava/util/List;", opcode = Opcodes.GETSTATIC))
    private static List<RegistryDataLoader.RegistryData<?>> load() {
        List<RegistryDataLoader.RegistryData<?>> data = new ArrayList<>(RegistryDataLoader.WORLDGEN_REGISTRIES);
        data.addAll(DataPackRegistryHooksImpl.getDataRegistries());
        return data;
    }
}
