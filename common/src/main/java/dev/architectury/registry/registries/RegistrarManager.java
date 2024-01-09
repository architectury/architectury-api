/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.registry.registries;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Platform-agnostic wrapper of minecraft registries, should be used to register content.
 */
public final class RegistrarManager {
    private static final Map<String, RegistrarManager> MANAGER = new ConcurrentHashMap<>();
    private final RegistryProvider provider;
    private final String modId;
    
    public static RegistrarManager get(String modId) {
        return MANAGER.computeIfAbsent(modId, RegistrarManager::new);
    }
    
    private RegistrarManager(String modId) {
        this.provider = _get(modId);
        this.modId = modId;
    }
    
    public <T> Registrar<T> get(ResourceKey<Registry<T>> key) {
        return this.provider.get(key);
    }
    
    @Deprecated
    public <T> Registrar<T> get(Registry<T> registry) {
        return this.provider.get(registry);
    }
    
    /**
     * Listen to registry registration, the callback is called when content should be registered.
     * On forge, this is invoked after {@code RegistryEvent.Register}.
     * On fabric, this is invoked immediately.
     *
     * @param key      the key of the registry
     * @param callback the callback to be invoked
     * @param <T>      the type of registry entry
     */
    public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> callback) {
        this.provider.forRegistry(key, callback);
    }
    
    @SafeVarargs
    public final <T> RegistrarBuilder<T> builder(ResourceLocation registryId, T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return this.provider.builder((Class<T>) typeGetter.getClass().getComponentType(), registryId);
    }
    
    @SafeVarargs
    public final <T> RegistrarBuilder<T> builderDefaulted(ResourceLocation registryId, ResourceLocation defaultId, T... typeGetter) {
        if (typeGetter.length != 0) throw new IllegalStateException("array must be empty!");
        return this.provider.builderDefaulted((Class<T>) typeGetter.getClass().getComponentType(), registryId, defaultId);
    }
    
    /**
     * Registers a non-synced dynamic registry.
     *
     * <p>The entries of the registry will be loaded from data packs at the file path
     * {@code data/<entry namespace>/<registry namespace>/<registry path>/<entry path>.json}.
     *
     * @param key   the key of the registry
     * @param codec the codec used to load registry entries from data packs
     * @param <T>   the type of registry entry
     */
    public <T> void dynamicRegistry(ResourceKey<Registry<T>> key, @NotNull Codec<T> codec) {
        this.provider.registerDynamicRegistry(key, codec);
    }
    
    /**
     * Registers a synced dynamic registry.
     *
     * <p>The entries of the registry will be loaded from data packs at the file path
     * {@code data/<entry namespace>/<registry namespace>/<registry path>/<entry path>.json}.
     *
     * <p>The registry will be synced from the server to players' clients using the same codec
     * that is used to load the registry.
     *
     * <p>If the object contained in the registry is complex and contains a lot of data
     * that is not relevant on the client, another codec for networking can be specified with
     * {@link #dynamicRegistrySynced(ResourceKey, Codec, Codec)}.
     *
     * @param key   the key of the registry
     * @param codec the codec used to load registry entries from data packs and the network
     * @param <T>   the type of registry entry
     */
    public <T> void dynamicRegistrySynced(ResourceKey<Registry<T>> key, @NotNull Codec<T> codec) {
        this.provider.registerDynamicRegistrySynced(key, codec, codec);
    }
    
    
    /**
     * Registers a synced dynamic registry.
     *
     * <p>The entries of the registry will be loaded from data packs at the file path
     * {@code data/<entry namespace>/<registry namespace>/<registry path>/<entry path>.json}
     *
     * <p>The registry will be synced from the server to players' clients using the given network codec.
     *
     * @param key          the key of the registry
     * @param dataCodec    the codec used to load registry entries from data packs
     * @param networkCodec the codec used to load registry entries from the network
     * @param <T>          the type of registry entry
     */
    public <T> void dynamicRegistrySynced(ResourceKey<Registry<T>> key, @NotNull Codec<T> dataCodec, @NotNull Codec<T> networkCodec) {
        this.provider.registerDynamicRegistrySynced(key, dataCodec, networkCodec);
    }
    
    /**
     * Forge: If the object is {@code IForgeRegistryEntry}, use `getRegistryName`, else null
     * Fabric: Use registry
     */
    @Nullable
    public static <T> ResourceLocation getId(T object, @Nullable ResourceKey<Registry<T>> fallback) {
        if (fallback == null)
            return null;
        return getId(object, (Registry<T>) BuiltInRegistries.REGISTRY.get(fallback.location()));
    }
    
    /**
     * Forge: If the object is {@code IForgeRegistryEntry}, use `getRegistryName`, else null
     * Fabric: Use registry
     */
    @Nullable
    @Deprecated
    public static <T> ResourceLocation getId(T object, @Nullable Registry<T> fallback) {
        if (fallback == null)
            return null;
        return fallback.getKey(object);
    }
    
    @ExpectPlatform
    private static RegistryProvider _get(String modId) {
        throw new AssertionError();
    }
    
    public String getModId() {
        return modId;
    }
    
    @ApiStatus.Internal
    public interface RegistryProvider {
        <T> Registrar<T> get(ResourceKey<Registry<T>> key);
        
        @Deprecated
        <T> Registrar<T> get(Registry<T> registry);
        
        <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> consumer);
        
        <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId);
        
        <T> RegistrarBuilder<T> builderDefaulted(Class<T> type, ResourceLocation registryId, ResourceLocation defaultId);
        
        <T> void registerDynamicRegistry(ResourceKey<Registry<T>> key, Codec<T> dataCodec);
        
        <T> void registerDynamicRegistrySynced(ResourceKey<Registry<T>> key, Codec<T> dataCodec, Codec<T> networkCodec);
    }
}
