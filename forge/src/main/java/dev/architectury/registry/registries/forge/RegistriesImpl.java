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
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarBuilder;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistriesImpl {
    private static final Logger LOGGER = LogManager.getLogger(RegistriesImpl.class);
    private static final Multimap<RegistryEntryId<?>, Consumer<?>> LISTENERS = HashMultimap.create();
    
    private static void listen(ResourceKey<?> resourceKey, ResourceLocation id, Consumer<?> listener, boolean vanilla) {
        LISTENERS.put(new RegistryEntryId<>(resourceKey, id), listener);
    }
    
    public static Registries.RegistryProvider _get(String modId) {
        return new RegistryProviderImpl(modId);
    }
    
    public static <T> ResourceLocation getId(T t, ResourceKey<net.minecraft.core.Registry<T>> registryKey) {
        if (t instanceof IForgeRegistryEntry)
            return ((IForgeRegistryEntry<?>) t).getRegistryName();
        return null;
    }
    
    public static <T> ResourceLocation getId(T t, net.minecraft.core.Registry<T> registry) {
        if (t instanceof IForgeRegistryEntry)
            return ((IForgeRegistryEntry<?>) t).getRegistryName();
        return null;
    }
    
    public static class Data {
        private boolean collected = false;
        private final Map<RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> objects = new LinkedHashMap<>();
        private final Map<ResourceLocation, Supplier<?>> vanillaObjects = new LinkedHashMap<>();
        
        public void register(IForgeRegistry registry, RegistryObject object, Supplier<? extends IForgeRegistryEntry<?>> reference) {
            if (!collected) {
                objects.put(object, reference);
            } else {
                ResourceKey<? extends Registry<Object>> resourceKey = ResourceKey.createRegistryKey(registry.getRegistryName());
                IForgeRegistryEntry<?> value = reference.get();
                registry.register(value);
                object.updateReference(registry);
                
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, object.getId());
                for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                    ((Consumer<Object>) consumer).accept(value);
                }
                LISTENERS.removeAll(registryEntryId);
            }
        }
        
        public <T> void register(Registry<T> registry, ResourceLocation id, Supplier<? extends T> reference) {
            if (!collected) {
                vanillaObjects.put(id, reference);
            } else {
                T value = reference.get();
                Registry.register(registry, id, value);
                
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(registry.key(), id);
                for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                    ((Consumer<Object>) consumer).accept(value);
                }
                LISTENERS.removeAll(registryEntryId);
            }
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
    
    public static class RegistryProviderImpl implements Registries.RegistryProvider {
        private final String modId;
        private final Supplier<IEventBus> eventBus;
        private final Map<ResourceKey<? extends Registry<?>>, Data> registry = new HashMap<>();
        private final Multimap<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> listeners = HashMultimap.create();
        
        public RegistryProviderImpl(String modId) {
            this.modId = modId;
            this.eventBus = Suppliers.memoize(() -> {
                IEventBus eventBus = EventBuses.getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Can't get event bus for mod '" + modId + "' because it was not registered!"));
                eventBus.register(new EventListener());
                return eventBus;
            });
        }
        
        private void updateEventBus() {
            synchronized (eventBus) {
                // Make sure that the eventbus is setup
                this.eventBus.get();
            }
        }
        
        @Override
        public <T> Registrar<T> get(ResourceKey<net.minecraft.core.Registry<T>> registryKey) {
            updateEventBus();
            ForgeRegistry registry = RegistryManager.ACTIVE.getRegistry(registryKey.location());
            if (registry == null) {
                Registry<T> ts = (Registry<T>) Registry.REGISTRY.get(registryKey.location());
                if (ts == null) {
                    throw new IllegalArgumentException("Registry " + registryKey + " does not exist!");
                } else {
                    return get(ts);
                }
            }
            return get(registry);
        }
        
        public <T> Registrar<T> get(IForgeRegistry registry) {
            updateEventBus();
            return new ForgeBackedRegistryImpl<>(modId, this.registry, registry);
        }
        
        @Override
        public <T> Registrar<T> get(net.minecraft.core.Registry<T> registry) {
            updateEventBus();
            return new VanillaBackedRegistryImpl<>(modId, this.registry, registry);
        }
        
        @Override
        public <T> void forRegistry(ResourceKey<net.minecraft.core.Registry<T>> key, Consumer<Registrar<T>> consumer) {
            this.listeners.put((ResourceKey<net.minecraft.core.Registry<?>>) (ResourceKey<? extends net.minecraft.core.Registry<?>>) key,
                    (Consumer<Registrar<?>>) (Consumer<? extends Registrar<?>>) consumer);
        }
        
        @Override
        public <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId) {
            return new RegistryBuilderWrapper<>(this, new net.minecraftforge.registries.RegistryBuilder<>()
                    .setName(registryId)
                    .setType((Class) type));
        }
        
        public class EventListener {
            public void handleVanillaRegistries() {
                for (Registry<?> vanillaRegistry : Registry.REGISTRY) {
                    if (RegistryManager.ACTIVE.getRegistry(vanillaRegistry.key().location()) == null) {
                        // Must be vanilla
                        Registrar<?> archRegistry = get(vanillaRegistry);
                        
                        for (Map.Entry<ResourceKey<? extends Registry<?>>, Data> typeDataEntry : RegistryProviderImpl.this.registry.entrySet()) {
                            ResourceKey<? extends Registry<?>> resourceKey = archRegistry.key();
                            if (typeDataEntry.getKey().equals(resourceKey)) {
                                Data data = typeDataEntry.getValue();
                                data.collected = true;
                                
                                for (Map.Entry<ResourceLocation, Supplier<?>> entry : data.vanillaObjects.entrySet()) {
                                    Object value = entry.getValue().get();
                                    Registry.register((Registry<Object>) vanillaRegistry, entry.getKey(), value);
                                    
                                    RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, entry.getKey());
                                    for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                                        ((Consumer<Object>) consumer).accept(value);
                                    }
                                    LISTENERS.removeAll(registryEntryId);
                                }
                                
                                data.objects.clear();
                            }
                        }
                        
                        for (Map.Entry<ResourceKey<net.minecraft.core.Registry<?>>, Consumer<Registrar<?>>> entry : listeners.entries()) {
                            if (entry.getKey().equals(vanillaRegistry.key())) {
                                entry.getValue().accept(archRegistry);
                            }
                        }
                    }
                }
            }
            
            public void handleVanillaRegistriesPost() {
                for (Registry<?> vanillaRegistry : Registry.REGISTRY) {
                    if (RegistryManager.ACTIVE.getRegistry(vanillaRegistry.key().location()) == null) {
                        // Must be vanilla
                        Registrar<?> archRegistry = get(vanillaRegistry);
                        
                        List<RegistryEntryId<?>> toRemove = new ArrayList<>();
                        for (Map.Entry<RegistryEntryId<?>, Collection<Consumer<?>>> entry : LISTENERS.asMap().entrySet()) {
                            if (entry.getKey().registryKey.equals(archRegistry.key())) {
                                if (vanillaRegistry.containsKey(entry.getKey().id)) {
                                    Object value = vanillaRegistry.get(entry.getKey().id);
                                    for (Consumer<?> consumer : entry.getValue()) {
                                        ((Consumer<Object>) consumer).accept(value);
                                    }
                                    toRemove.add(entry.getKey());
                                } else {
                                    LOGGER.warn("Registry entry listened {} was not realized!", entry.getKey());
                                }
                            }
                        }
                        
                        for (RegistryEntryId<?> entryId : toRemove) {
                            LISTENERS.removeAll(entryId);
                        }
                    }
                }
            }
            
            @SubscribeEvent
            public void handleEvent(RegistryEvent.Register event) {
                if (event.getGenericType() == Block.class) {
                    handleVanillaRegistries();
                }
                
                IForgeRegistry registry = event.getRegistry();
                Registrar<Object> archRegistry = get(registry);
                
                for (Map.Entry<ResourceKey<? extends Registry<?>>, Data> typeDataEntry : RegistryProviderImpl.this.registry.entrySet()) {
                    ResourceKey<? extends Registry<Object>> resourceKey = archRegistry.key();
                    if (typeDataEntry.getKey().equals(resourceKey)) {
                        Data data = typeDataEntry.getValue();
                        
                        data.collected = true;
                        
                        for (Map.Entry<RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> entry : data.objects.entrySet()) {
                            IForgeRegistryEntry<?> value = entry.getValue().get();
                            registry.register(value);
                            entry.getKey().updateReference(registry);
                            
                            RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, entry.getKey().getId());
                            for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                                ((Consumer<Object>) consumer).accept(value);
                            }
                            LISTENERS.removeAll(registryEntryId);
                        }
                        
                        data.objects.clear();
                    }
                }
                
                for (Map.Entry<ResourceKey<net.minecraft.core.Registry<?>>, Consumer<Registrar<?>>> entry : listeners.entries()) {
                    if (entry.getKey().location().equals(registry.getRegistryName())) {
                        entry.getValue().accept(archRegistry);
                    }
                }
            }
            
            @SubscribeEvent(priority = EventPriority.LOWEST)
            public void handleEventPost(RegistryEvent.Register event) {
                if (event.getGenericType() == ForgeWorldPreset.class) {
                    handleVanillaRegistriesPost();
                }
                
                IForgeRegistry registry = event.getRegistry();
                Registrar<Object> archRegistry = get(registry);
                ResourceKey<? extends Registry<Object>> resourceKey = archRegistry.key();
                List<RegistryEntryId<?>> toRemove = new ArrayList<>();
                for (Map.Entry<RegistryEntryId<?>, Collection<Consumer<?>>> entry : LISTENERS.asMap().entrySet()) {
                    if (entry.getKey().registryKey.equals(resourceKey)) {
                        if (registry.containsKey(entry.getKey().id)) {
                            IForgeRegistryEntry value = registry.getValue(entry.getKey().id);
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
        }
    }
    
    public static class RegistryBuilderWrapper<T> implements RegistrarBuilder<T> {
        private final RegistryProviderImpl provider;
        private final net.minecraftforge.registries.RegistryBuilder<?> builder;
        private boolean saveToDisk = false;
        private boolean syncToClients = false;
        
        public RegistryBuilderWrapper(RegistryProviderImpl provider, net.minecraftforge.registries.RegistryBuilder<?> builder) {
            this.provider = provider;
            this.builder = builder;
        }
        
        @Override
        public Registrar<T> build() {
            if (!syncToClients) builder.disableSync();
            if (!saveToDisk) builder.disableSaving();
            return provider.get(builder.create());
        }
        
        @Override
        public RegistrarBuilder<T> option(RegistrarOption option) {
            if (option == StandardRegistrarOption.SAVE_TO_DISC) {
                this.saveToDisk = true;
            } else if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.syncToClients = true;
            }
            return this;
        }
    }
    
    public static class VanillaBackedRegistryImpl<T> implements Registrar<T> {
        private final String modId;
        private net.minecraft.core.Registry<T> delegate;
        private Map<ResourceKey<? extends Registry<?>>, Data> registry;
        
        public VanillaBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, Data> registry, net.minecraft.core.Registry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            Registrar<T> registrar = this;
            return new RegistrySupplier<T>() {
                @Override
                public Registries getRegistries() {
                    return Registries.get(modId);
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
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            registry.computeIfAbsent(key(), type -> new Data())
                    .register(delegate, id, supplier);
            return (RegistrySupplier<E>) delegate(id);
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
        public ResourceKey<? extends net.minecraft.core.Registry<T>> key() {
            return delegate.key();
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
                RegistriesImpl.listen(key(), id, callback, true);
            }
        }
    }
    
    public static class ForgeBackedRegistryImpl<T extends IForgeRegistryEntry<T>> implements Registrar<T> {
        private final String modId;
        private IForgeRegistry<T> delegate;
        private Map<ResourceKey<? extends Registry<?>>, Data> registry;
        
        public ForgeBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, Data> registry, IForgeRegistry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            Registrar<T> registrar = this;
            return new RegistrySupplier<T>() {
                @Override
                public Registries getRegistries() {
                    return Registries.get(modId);
                }
                
                @Override
                public Registrar<T> getRegistrar() {
                    return registrar;
                }
                
                @Override
                public ResourceLocation getRegistryId() {
                    return delegate.getRegistryName();
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
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            RegistryObject registryObject = RegistryObject.of(id, delegate);
            registry.computeIfAbsent(key(), type -> new Data())
                    .register(delegate, registryObject, () -> supplier.get().setRegistryName(id));
            Registrar<T> registrar = this;
            return new RegistrySupplier<E>() {
                @Override
                public Registries getRegistries() {
                    return Registries.get(modId);
                }
                
                @Override
                public Registrar<E> getRegistrar() {
                    return (Registrar<E>) registrar;
                }
                
                @Override
                public ResourceLocation getRegistryId() {
                    return delegate.getRegistryName();
                }
                
                @Override
                public ResourceLocation getId() {
                    return registryObject.getId();
                }
                
                @Override
                public boolean isPresent() {
                    return registryObject.isPresent();
                }
                
                @Override
                public E get() {
                    return (E) registryObject.get();
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
        @Nullable
        public ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        public int getRawId(T obj) {
            return ((ForgeRegistry<T>) delegate).getID(obj);
        }
        
        @Override
        public Optional<ResourceKey<T>> getKey(T t) {
            return Optional.ofNullable(getId(t)).map(id -> ResourceKey.create(key(), id));
        }
        
        @Override
        @Nullable
        public T get(ResourceLocation id) {
            return delegate.getValue(id);
        }
        
        @Override
        public T byRawId(int rawId) {
            return ((ForgeRegistry<T>) delegate).getValue(rawId);
        }
        
        @Override
        public boolean contains(ResourceLocation resourceLocation) {
            return delegate.containsKey(resourceLocation);
        }
        
        @Override
        public boolean containsValue(T t) {
            return delegate.containsValue(t);
        }
        
        @Override
        public Set<ResourceLocation> getIds() {
            return delegate.getKeys();
        }
        
        @Override
        public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
            return delegate.getEntries();
        }
        
        @Override
        public ResourceKey<? extends net.minecraft.core.Registry<T>> key() {
            return ResourceKey.createRegistryKey(delegate.getRegistryName());
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
                RegistriesImpl.listen(key(), id, callback, false);
            }
        }
    }
}
