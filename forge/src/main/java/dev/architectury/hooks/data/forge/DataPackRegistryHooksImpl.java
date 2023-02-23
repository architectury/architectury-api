package dev.architectury.hooks.data.forge;

import com.mojang.serialization.Codec;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.Nullable;

public class DataPackRegistryHooksImpl {
    
    @SuppressWarnings("unchecked")
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        EventBuses.onRegistered(key.location().getNamespace(), bus -> bus.<DataPackRegistryEvent.NewRegistry>addListener(
                event -> event.dataPackRegistry((ResourceKey<Registry<T>>) key, codec, networkCodec)
        ));
    }
}
