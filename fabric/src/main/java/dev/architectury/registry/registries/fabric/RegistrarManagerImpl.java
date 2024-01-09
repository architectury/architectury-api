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

package dev.architectury.registry.registries.fabric;

import com.google.common.base.Objects;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.serialization.Codec;
import dev.architectury.impl.RegistrySupplierImpl;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarBuilder;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistrarManagerImpl {
    private static final Multimap<RegistryEntryId<?>, Consumer<?>> LISTENERS = HashMultimap.create();
    private static final Set<ResourceKey<?>> LISTENED_REGISTRIES = new HashSet<>();
    
    private static void listen(ResourceKey<?> resourceKey, ResourceLocation id, Consumer<?> listener) {
        if (LISTENED_REGISTRIES.add(resourceKey)) {
            Registry<?> registry = java.util.Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(resourceKey.location()), "Registry " + resourceKey + " not found!");
            RegistryEntryAddedCallback.event(registry).register((rawId, entryId, object) -> {
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, entryId);
                for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                    ((Consumer<Object>) consumer).accept(object);
                }
                LISTENERS.removeAll(registryEntryId);
            });
        }
        
        LISTENERS.put(new RegistryEntryId<>(resourceKey, id), listener);
    }
    
    public static RegistrarManager.RegistryProvider _get(String modId) {
        return new RegistryProviderImpl(modId);
    }
    
    public static class RegistryProviderImpl implements RegistrarManager.RegistryProvider {
        private final String modId;
        
        public RegistryProviderImpl(String modId) {
            this.modId = modId;
        }
        
        @Override
        public <T> Registrar<T> get(ResourceKey<Registry<T>> key) {
            return new RegistrarImpl<>(modId, (Registry<T>) java.util.Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(key.location()), "Registry " + key + " not found!"));
        }
        
        @Override
        public <T> Registrar<T> get(Registry<T> registry) {
            return new RegistrarImpl<>(modId, registry);
        }
        
        @Override
        public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> consumer) {
            consumer.accept(get(key));
        }
        
        @Override
        public <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId) {
            return new RegistrarBuilderWrapper<>(modId, FabricRegistryBuilder.createSimple(type, registryId));
        }
        
        @Override
        public <T> void registerDynamicRegistry(ResourceKey<Registry<T>> key, Codec<T> dataCodec) {
            DynamicRegistries.register(key, dataCodec);
        }
        
        @Override
        public <T> void registerDynamicRegistrySynced(ResourceKey<Registry<T>> key, Codec<T> dataCodec, Codec<T> networkCodec) {
            DynamicRegistries.registerSynced(key, dataCodec, networkCodec);
        }
    }
    
    public static class RegistryEntryId<T> {
        private final ResourceKey<T> registryKey;
        private final ResourceLocation id;
        
        public RegistryEntryId(ResourceKey<T> registryKey, ResourceLocation id) {
            this.registryKey = registryKey;
            this.id = id;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RegistryEntryId)) return false;
            RegistryEntryId<?> that = (RegistryEntryId<?>) o;
            return java.util.Objects.equals(registryKey, that.registryKey) && java.util.Objects.equals(id, that.id);
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(registryKey, id);
        }
    }
    
    public static class RegistrarBuilderWrapper<T> implements RegistrarBuilder<T> {
        private final String modId;
        private FabricRegistryBuilder<T, MappedRegistry<T>> builder;
        
        public RegistrarBuilderWrapper(String modId, FabricRegistryBuilder<T, MappedRegistry<T>> builder) {
            this.modId = modId;
            this.builder = builder;
        }
        
        @Override
        public Registrar<T> build() {
            return RegistrarManager.get(modId).get(builder.buildAndRegister());
        }
        
        @Override
        public RegistrarBuilder<T> option(RegistrarOption option) {
            if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.builder.attribute(RegistryAttribute.SYNCED);
            }
            return this;
        }
    }
    
    public static class RegistrarImpl<T> implements Registrar<T> {
        private final String modId;
        private Registry<T> delegate;
        
        public RegistrarImpl(String modId, Registry<T> delegate) {
            this.modId = modId;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            RegistrarImpl<T> registrar = this;
            return new RegistrySupplierImpl<T>() {
                @Nullable
                Holder<T> holder = null;
                
                @Nullable
                @Override
                public Holder<T> getHolder() {
                    if (holder != null) return holder;
                    return holder = registrar.getHolder(getId());
                }
                
                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(modId);
                }
                
                @Override
                public Registrar<T> getRegistrar() {
                    return registrar;
                }
                
                @Override
                public ResourceLocation getRegistryId() {
                    return delegate.key().location();
                }
                
                @Override
                public ResourceLocation getId() {
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
                    if (!(obj instanceof RegistrySupplier<?> other)) return false;
                    return other.getRegistryId().equals(getRegistryId()) && other.getId().equals(getId());
                }
                
                @Override
                public String toString() {
                    return getRegistryId().toString() + "@" + id.toString();
                }
            };
        }
        
        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            Registry.register(delegate, id, supplier.get());
            return (RegistrySupplier<E>) delegate(id);
        }
        
        @Override
        public @Nullable ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        public int getRawId(T obj) {
            return delegate.getId(obj);
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
        public T byRawId(int rawId) {
            return delegate.byId(rawId);
        }
        
        @Override
        public boolean contains(ResourceLocation id) {
            return delegate.keySet().contains(id);
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
        public ResourceKey<? extends Registry<T>> key() {
            return delegate.key();
        }
        
        @Override
        @Nullable
        public Holder<T> getHolder(ResourceKey<T> key) {
            return delegate.getHolder(key).orElse(null);
        }
        
        @Override
        public Iterator<T> iterator() {
            return delegate.iterator();
        }
        
        @Override
        public void listen(ResourceLocation id, Consumer<T> callback) {
            if (contains(id)) {
                callback.accept(get(id));
            } else {
                RegistrarManagerImpl.listen(key(), id, callback);
            }
        }
    }
}
