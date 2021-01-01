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

package me.shedaniel.architectury.registry.fabric;

import com.google.common.base.Objects;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class RegistriesImpl {
    public static Registries.RegistryProvider _get(String modId) {
        return RegistryProviderImpl.INSTANCE;
    }
    
    public static <T> ResourceLocation getId(T object, ResourceKey<net.minecraft.core.Registry<T>> fallback) {
        if (fallback == null)
            return null;
        return RegistryProviderImpl.INSTANCE.get(fallback).getId(object);
    }
    
    public static <T> ResourceLocation getId(T object, net.minecraft.core.Registry<T> fallback) {
        if (fallback == null)
            return null;
        return RegistryProviderImpl.INSTANCE.get(fallback).getId(object);
    }
    
    public enum RegistryProviderImpl implements Registries.RegistryProvider {
        INSTANCE;
        
        @Override
        public <T> Registry<T> get(ResourceKey<net.minecraft.core.Registry<T>> key) {
            return new RegistryImpl<>((net.minecraft.core.Registry<T>) net.minecraft.core.Registry.REGISTRY.get(key.location()));
        }
        
        @Override
        public <T> Registry<T> get(net.minecraft.core.Registry<T> registry) {
            return new RegistryImpl<>(registry);
        }
    }
    
    public static class RegistryImpl<T> implements Registry<T> {
        private net.minecraft.core.Registry<T> delegate;
        
        public RegistryImpl(net.minecraft.core.Registry<T> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public @NotNull RegistrySupplier<T> delegateSupplied(ResourceLocation id) {
            LazyLoadedValue<T> value = new LazyLoadedValue<>(() -> get(id));
            return new RegistrySupplier<T>() {
                @Override
                public @NotNull ResourceLocation getRegistryId() {
                    return delegate.key().location();
                }
                
                @Override
                public @NotNull ResourceLocation getId() {
                    return id;
                }
                
                @Override
                public boolean isPresent() {
                    return contains(id);
                }
                
                @Override
                public T get() {
                    return value.get();
                }
                
                @Override
                public int hashCode() {
                    return Objects.hashCode(getRegistryId(), getId());
                }
                
                @Override
                public boolean equals(Object obj) {
                    if (this == obj) return true;
                    if (!(obj instanceof RegistrySupplier)) return false;
                    RegistrySupplier<?> other = (RegistrySupplier<?>) obj;
                    return other.getRegistryId().equals(getRegistryId()) && other.getId().equals(getId());
                }
                
                @Override
                public String toString() {
                    return getRegistryId().toString() + "@" + id.toString();
                }
            };
        }
        
        @Override
        public @NotNull RegistrySupplier<T> registerSupplied(ResourceLocation id, Supplier<T> supplier) {
            net.minecraft.core.Registry.register(delegate, id, supplier.get());
            return delegateSupplied(id);
        }
        
        @Override
        public @Nullable ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        public Optional<ResourceKey<T>> getKey(T obj) {
            return delegate.getResourceKey(obj);
        }
        
        @Override
        public @Nullable T get(ResourceLocation id) {
            return delegate.get(id);
        }
        
        @Override
        public boolean contains(ResourceLocation id) {
            return delegate.containsKey(id);
        }
        
        @Override
        public boolean containsValue(T obj) {
            return delegate.getResourceKey(obj).isPresent();
        }
        
        @Override
        public Set<ResourceLocation> getIds() {
            return delegate.keySet();
        }
        
        @Override
        public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
            return delegate.entrySet();
        }
        
        @Override
        public ResourceKey<? extends net.minecraft.core.Registry<T>> key() {
            return delegate.key();
        }
        
        @NotNull
        @Override
        public Iterator<T> iterator() {
            return delegate.iterator();
        }
    }
}
