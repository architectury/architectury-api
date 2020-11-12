/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class Registries {
    private static final Map<String, Registries> REGISTRIES = new HashMap<>();
    private final RegistryProvider provider;
    
    public static Registries get(String modId) {
        return REGISTRIES.computeIfAbsent(modId, Registries::new);
    }
    
    private Registries(String modId) {
        this.provider = _get(modId);
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
    
    public interface RegistryProvider {
        <T> Registry<T> get(ResourceKey<net.minecraft.core.Registry<T>> key);
        
        @Deprecated
        <T> Registry<T> get(net.minecraft.core.Registry<T> registry);
    }
}
