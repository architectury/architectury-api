/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Platform-agnostic wrapper of minecraft registries, should be used to register content.
 */
public final class Registries {
    private static final Map<String, Registries> REGISTRIES = new ConcurrentHashMap<>();
    private final RegistryProvider provider;
    private final String modId;
    
    public static Registries get(String modId) {
        return REGISTRIES.computeIfAbsent(modId, Registries::new);
    }
    
    private Registries(String modId) {
        this.provider = _get(modId);
        this.modId = modId;
    }
    
    public <T> Registry<T> get(ResourceKey<net.minecraft.core.Registry<T>> key) {
        return this.provider.get(key);
    }
    
    @Deprecated
    public <T> Registry<T> get(net.minecraft.core.Registry<T> registry) {
        return this.provider.get(registry);
    }
    
    /**
     * Forge: If the object is {@code IForgeRegistryEntry}, use `getRegistryName`, else null
     * Fabric: Use registry
     */
    @Nullable
    @ExpectPlatform
    public static <T> ResourceLocation getId(T object, @Nullable ResourceKey<net.minecraft.core.Registry<T>> fallback) {
        throw new AssertionError();
    }
    
    /**
     * Forge: If the object is {@code IForgeRegistryEntry}, use `getRegistryName`, else null
     * Fabric: Use registry
     */
    @Nullable
    @Deprecated
    @ExpectPlatform
    public static <T> ResourceLocation getId(T object, @Nullable net.minecraft.core.Registry<T> fallback) {
        throw new AssertionError();
    }
    
    /**
     * Forge: If the object is {@code IForgeRegistryEntry}, use `getRegistryName`, else null
     * Fabric: null
     */
    @Deprecated
    @Nullable
    public static <T> ResourceLocation getRegistryName(T object) {
        return getId(object, (ResourceKey<net.minecraft.core.Registry<T>>) null);
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
        <T> Registry<T> get(ResourceKey<net.minecraft.core.Registry<T>> key);
        
        @Deprecated
        <T> Registry<T> get(net.minecraft.core.Registry<T> registry);
    }
}
