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

package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
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
        public Supplier<T> delegate(ResourceLocation id) {
            LazyLoadedValue<T> value = new LazyLoadedValue<>(() -> get(id));
            return value::get;
        }
        
        @Override
        public Supplier<T> register(ResourceLocation id, Supplier<T> supplier) {
            net.minecraft.core.Registry.register(delegate, id, supplier.get());
            return delegate(id);
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
