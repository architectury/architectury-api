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

package dev.architectury.registry.registries.forge;

import com.google.common.base.Objects;
import com.google.common.base.Suppliers;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.impl.RegistrySupplierImpl;
import dev.architectury.platform.hooks.forge.EventBusesHooksImpl;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarBuilder;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.options.DefaultIdRegistrarOption;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistrarManagerImpl {
    private static final Logger LOGGER = LogManager.getLogger(RegistrarManagerImpl.class);
    private static final Multimap<RegistryEntryId<?>, Consumer<?>> LISTENERS = HashMultimap.create();
    
    private static void listen(ResourceKey<?> resourceKey, ResourceLocation id, Consumer<?> listener) {
        LISTENERS.put(new RegistryEntryId<>(resourceKey, id), listener);
    }
    
    public static RegistrarManager.RegistryProvider _get(String modId) {
        return new RegistryProviderImpl(modId);
    }
    
    public static class Data<T> {
        private boolean registered = false;
        private final Map<ResourceLocation, Supplier<? extends T>> objects = new LinkedHashMap<>();
        
        public void register(Registry<T> registry, ResourceLocation location, Mutable<T> object, Supplier<? extends T> reference) {
            if (!registered) {
                objects.put(location, () -> {
                    T value = reference.get();
                    object.setValue(value);
                    return value;
                });
            } else {
                ResourceKey<? extends Registry<T>> resourceKey = registry.key();
                T value = reference.get();
                Registry.register(registry, location, value);
                object.setValue(value);
                
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, location);
                for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                    ((Consumer<Object>) consumer).accept(value);
                }
                LISTENERS.removeAll(registryEntryId);
            }
        }
    }
    
    public record RegistryEntryId<T>(ResourceKey<T> registryKey, ResourceLocation id) {
        @Override
        public String toString() {
            return "Registry Entry [%s / %s]".formatted(registryKey.location(), id);
        }
    }
    
    public static class RegistryProviderImpl implements RegistrarManager.RegistryProvider {
        private static final Map<ResourceKey<Registry<?>>, Registrar<?>> CUSTOM_REGS = new HashMap<>();
        private final String modId;
        private final Map<ResourceKey<? extends Registry<?>>, Data<?>> registry = new HashMap<>();
        private final Multimap<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> listeners = HashMultimap.create();
        
        @Nullable
        private List<Registry<?>> newRegistries = new ArrayList<>();
        
        public RegistryProviderImpl(String modId) {
            this.modId = modId;
            EventBusesHooksImpl.getModEventBus(modId).get().register(new EventListener());
        }
        
        @Override
        public <T> Registrar<T> get(ResourceKey<Registry<T>> registryKey) {
            Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
            if (registry != null) {
                return get(registry);
            }
            Registrar<?> customReg = RegistryProviderImpl.CUSTOM_REGS.get(registryKey);
            if (customReg != null) return (Registrar<T>) customReg;
            throw new IllegalArgumentException("Registry " + registryKey + " does not exist!");
        }
        
        @Override
        public <T> Registrar<T> get(Registry<T> registry) {
            return new RegistrarImpl<>(modId, this.registry, registry);
        }
        
        @Override
        public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> consumer) {
            this.listeners.put((ResourceKey<Registry<?>>) (ResourceKey<? extends Registry<?>>) key,
                    (Consumer<Registrar<?>>) (Consumer<? extends Registrar<?>>) consumer);
        }
        
        @Override
        public <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId) {
            return new RegistryBuilderWrapper<>(this, new RegistryBuilder<>(ResourceKey.createRegistryKey(registryId)));
        }
        
        public class EventListener {
            @SubscribeEvent
            public void handleEvent(RegisterEvent event) {
                for (Map.Entry<ResourceKey<? extends Registry<?>>, Data<?>> typeDataEntry : RegistryProviderImpl.this.registry.entrySet()) {
                    if (typeDataEntry.getKey().equals(event.getRegistryKey())) {
                        //noinspection rawtypes
                        registerFor(event, (ResourceKey) typeDataEntry.getKey(), typeDataEntry.getValue());
                    }
                }
            }
            
            public <T> void registerFor(RegisterEvent event, ResourceKey<? extends Registry<T>> resourceKey, Data<T> data) {
                event.register(resourceKey, registry -> {
                    data.registered = true;
                    
                    for (Map.Entry<ResourceLocation, Supplier<? extends T>> entry : data.objects.entrySet()) {
                        ResourceLocation location = entry.getKey();
                        T value = entry.getValue().get();
                        registry.register(location, value);
                        
                        RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, location);
                        for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                            ((Consumer<Object>) consumer).accept(value);
                        }
                        LISTENERS.removeAll(registryEntryId);
                    }
                    
                    data.objects.clear();
                    Registrar<?> archRegistry = get(event.getRegistry());
                    
                    for (Map.Entry<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> entry : listeners.entries()) {
                        if (entry.getKey().location().equals(resourceKey.location())) {
                            entry.getValue().accept(archRegistry);
                        }
                    }
                });
            }
            
            @SubscribeEvent(priority = EventPriority.LOWEST)
            public void handleEventPost(RegisterEvent event) {
                Registrar<?> archRegistry = get(event.getRegistry());
                
                List<RegistryEntryId<?>> toRemove = new ArrayList<>();
                
                for (Map.Entry<RegistryEntryId<?>, Collection<Consumer<?>>> entry : LISTENERS.asMap().entrySet()) {
                    if (entry.getKey().registryKey.equals(event.getRegistryKey())) {
                        if (archRegistry.contains(entry.getKey().id)) {
                            Object value = archRegistry.get(entry.getKey().id);
                            for (Consumer<?> consumer : entry.getValue()) {
                                ((Consumer<Object>) consumer).accept(value);
                            }
                            toRemove.add(entry.getKey());
                        } else {
                            LOGGER.warn("Registry entry listened {} was not realized!", entry.getKey());
                        }
                    }
                }
                
                for (RegistryEntryId<?> id : toRemove) {
                    LISTENERS.removeAll(id);
                }
            }
            
            @SubscribeEvent
            public void handleEvent(NewRegistryEvent event) {
                if (newRegistries != null) {
                    for (Registry<?> registry : newRegistries) {
                        event.register(registry);
                    }
                    newRegistries = null;
                }
            }
        }
    }
    
    public static class RegistryBuilderWrapper<T> implements RegistrarBuilder<T> {
        private final RegistryProviderImpl provider;
        private final RegistryBuilder<T> builder;
        private boolean syncToClients = false;
        
        public RegistryBuilderWrapper(RegistryProviderImpl provider, RegistryBuilder<T> builder) {
            this.provider = provider;
            this.builder = builder;
        }
        
        @Override
        public Registrar<T> build() {
            builder.sync(syncToClients);
            if (provider.newRegistries == null) {
                throw new IllegalStateException("Cannot create registries when registries are already aggregated!");
            }
            var registry = builder.create();
            var registrar = provider.get(registry);
            provider.newRegistries.add(registry);
            //noinspection rawtypes
            RegistryProviderImpl.CUSTOM_REGS.put((ResourceKey) registrar.key(), registrar);
            return registrar;
        }
        
        @Override
        public RegistrarBuilder<T> option(RegistrarOption option) {
            if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.syncToClients = true;
            } else if (option instanceof DefaultIdRegistrarOption opt) {
                this.builder.defaultKey(opt.defaultId());
            }
            return this;
        }
    }
    
    public static class RegistrarImpl<T> implements Registrar<T> {
        private final String modId;
        private final Registry<T> delegate;
        private final Map<ResourceKey<? extends Registry<?>>, Data<?>> registry;
        
        public RegistrarImpl(String modId, Map<ResourceKey<? extends Registry<?>>, Data<?>> registry, Registry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            return asSupplier(id, this, () -> contains(id), value);
        }
        
        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            Data<T> data = (Data<T>) registry.computeIfAbsent(key(), type -> new Data<>());
            Mutable<T> object = new MutableObject<>();
            data.register(delegate, id, object, supplier);
            return asSupplier(id, (Registrar<E>) this, () -> object.getValue() != null, object::getValue);
        }
        
        private <E extends T> RegistrySupplier<E> asSupplier(ResourceLocation id, Registrar<E> registrar, BooleanSupplier isPresent, Supplier<T> object) {
            return new RegistrySupplierImpl<E>() {
                @Nullable
                Holder<E> holder = null;
                
                @Nullable
                @Override
                public Holder<E> getHolder() {
                    if (holder != null) return holder;
                    return holder = registrar.getHolder(getId());
                }
                
                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(modId);
                }
                
                @Override
                public Registrar<E> getRegistrar() {
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
                    return isPresent.getAsBoolean();
                }
                
                @Override
                public E get() {
                    E value = (E) object.get();
                    if (value == null) {
                        throw new NullPointerException("Value missing: " + this.getId() + "@" + getRegistryId());
                    }
                    return value;
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
                    return getRegistryId() + "@" + id.toString();
                }
            };
        }
        
        @Override
        @Nullable
        public ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        public int getRawId(T obj) {
            return delegate.getId(obj);
        }
        
        @Override
        public Optional<ResourceKey<T>> getKey(T t) {
            return delegate.getResourceKey(t);
        }
        
        @Override
        @Nullable
        public T get(ResourceLocation id) {
            return delegate.get(id);
        }
        
        @Override
        public T byRawId(int rawId) {
            return delegate.byId(rawId);
        }
        
        @Override
        public boolean contains(ResourceLocation resourceLocation) {
            return delegate.keySet().contains(resourceLocation);
        }
        
        @Override
        public boolean containsValue(T t) {
            return delegate.getResourceKey(t).isPresent();
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
