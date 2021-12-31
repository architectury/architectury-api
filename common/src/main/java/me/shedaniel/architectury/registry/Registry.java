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

package me.shedaniel.architectury.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public interface Registry<T> extends Iterable<T> {
    default Supplier<T> delegate(ResourceLocation id) {
        return delegateSupplied(id);
    }
    
    RegistrySupplier<T> delegateSupplied(ResourceLocation id);
    
    default <E extends T> Supplier<E> register(ResourceLocation id, Supplier<E> supplier) {
        return registerSupplied(id, supplier);
    }
    
    <E extends T> RegistrySupplier<E> registerSupplied(ResourceLocation id, Supplier<E> supplier);
    
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
    
    ResourceKey<? extends net.minecraft.core.Registry<T>> key();
}
