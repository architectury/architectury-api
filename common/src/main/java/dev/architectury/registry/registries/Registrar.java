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
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Registrar<T> extends Iterable<T> {
    RegistrySupplier<T> delegate(ResourceLocation id);
    
    default <R extends T> RegistrySupplier<R> wrap(R obj) {
        ResourceLocation id = getId(obj);
        
        if (id == null) {
            throw new IllegalArgumentException("Cannot wrap an object without an id: " + obj);
        } else {
            return (RegistrySupplier<R>) delegate(id);
        }
    }
    
    <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier);
    
    @Nullable
    ResourceLocation getId(T obj);
    
    int getRawId(T obj);
    
    Optional<ResourceKey<T>> getKey(T obj);
    
    @Nullable
    T get(ResourceLocation id);
    
    @Nullable
    T byRawId(int rawId);
    
    boolean contains(ResourceLocation id);
    
    boolean containsValue(T obj);
    
    Set<ResourceLocation> getIds();
    
    Set<Map.Entry<ResourceKey<T>, T>> entrySet();
    
    ResourceKey<? extends Registry<T>> key();
    
    @Nullable
    Holder<T> getHolder(ResourceKey<T> key);
    
    @Nullable
    default Holder<T> getHolder(ResourceLocation id) {
        return getHolder(ResourceKey.create(key(), id));
    }
    
    Codec<T> codec();
    
    Codec<Holder<T>> holderCodec();
    
    /**
     * Listens to when the registry entry is registered, and calls the given action.
     * Evaluates immediately if the entry is already registered.
     * <p>
     * Whenever the callback is called is dependent on the registry implementation.
     * On fabric, this will be called when the registry entry is registered.
     * On forge, this will be called when the registry entry is registered or when Minecraft has started.
     *
     * @param supplier the entry to listen to
     * @param callback the action to call when the registry entry is registered
     */
    default <R extends T> void listen(RegistrySupplier<R> supplier, Consumer<R> callback) {
        listen(supplier.getId(), obj -> callback.accept((R) obj));
    }
    
    /**
     * Listens to when the registry entry is registered, and calls the given action.
     * Evaluates immediately if the entry is already registered.
     * <p>
     * Whenever the callback is called is dependent on the registry implementation.
     * On fabric, this will be called when the registry entry is registered.
     * On forge, this will be called when the registry entry is registered or when Minecraft has started.
     *
     * @param id       the entry to listen to
     * @param callback the action to call when the registry entry is registered
     */
    void listen(ResourceLocation id, Consumer<T> callback);
}
