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

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
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
    
    public <T> RegistrarBuilder<T> builder(ResourceLocation registryId) {
        return this.provider.builder(registryId);
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
        
        <T> RegistrarBuilder<T> builder(ResourceLocation registryId);
    }
}
