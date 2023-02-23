package dev.architectury.mixin.fabric;

import com.google.common.collect.ImmutableMap;
import dev.architectury.hooks.data.fabric.DataPackRegistryHooksImpl;
import dev.architectury.registry.registries.fabric.RegistrarManagerImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RegistrySynchronization.class)
public class MixinRegistrySynchronization {
    
    @Inject(method = "method_45958", at = @At(value = "RETURN", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void addNetworkedDataRegistrars(CallbackInfoReturnable<Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>>> cir, ImmutableMap.Builder<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> builder) {
        builder.putAll(DataPackRegistryHooksImpl.getNetworkableDataRegistries());
    }
}
