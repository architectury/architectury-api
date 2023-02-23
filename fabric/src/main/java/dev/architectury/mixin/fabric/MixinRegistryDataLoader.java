package dev.architectury.mixin.fabric;

import dev.architectury.hooks.data.fabric.DataPackRegistryHooksImpl;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RegistryDataLoader.class)
public class MixinRegistryDataLoader {
    
    // Mirror forge's directory style for architectury registrars
    @Inject(method = "registryDirPath", at = @At("HEAD"), cancellable = true)
    private static void registryDirPath(ResourceLocation key, CallbackInfoReturnable<String> cir) {
        if (DataPackRegistryHooksImpl.shouldPrefix(key))
            cir.setReturnValue(key.getNamespace() + "/" + key.getPath());
    }
}
