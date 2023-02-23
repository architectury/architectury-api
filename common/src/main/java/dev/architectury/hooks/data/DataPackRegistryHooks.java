package dev.architectury.hooks.data;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class DataPackRegistryHooks {
    
    private DataPackRegistryHooks() {
    }
    
    /**
     * Registers a world-specific data registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param key          The key to identify the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param networkCodec An optional codec to sync registry contents to clients
     * @param <T> The registry object type
     */
    @ExpectPlatform
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        throw new AssertionError();
    }
    
    /**
     * Registers a world-specific data registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param key          The key to identify the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param <T> The registry object type
     */
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        addRegistryCodec(key, codec, null);
    }
}
