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
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarBuilder;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.registry.registries.options.RegistrarOption;
import dev.architectury.registry.registries.options.StandardRegistrarOption;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistrarManagerImpl {
    private static final Logger LOGGER = LogManager.getLogger(RegistrarManagerImpl.class);
    private static final Multimap<RegistryEntryId<?>, Consumer<?>> LISTENERS = HashMultimap.create();
    
    private static void listen(ResourceKey<?> resourceKey, ResourceLocation id, Consumer<?> listener, boolean vanilla) {
        LISTENERS.put(new RegistryEntryId<>(resourceKey, id), listener);
    }
    
    public static RegistrarManager.RegistryProvider _get(String modId) {
        return new RegistryProviderImpl(modId);
    }
    
    public static class Data<T> {
        private boolean registered = false;
        private final Map<ResourceLocation, Supplier<? extends T>> objects = new LinkedHashMap<>();
        
        public void registerForForge(IForgeRegistry<T> registry, ResourceLocation location, Object[] objectArr, Supplier<? extends T> reference) {
            if (!registered) {
                objects.put(location, () -> {
                    T value = reference.get();
                    objectArr[0] = value;
                    return value;
                });
            } else {
                ResourceKey<? extends Registry<Object>> resourceKey = ResourceKey.createRegistryKey(registry.getRegistryName());
                T value = reference.get();
                registry.register(location, value);
                objectArr[0] = value;
                
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(resourceKey, location);
                for (Consumer<?> consumer : LISTENERS.get(registryEntryId)) {
                    ((Consumer<Object>) consumer).accept(value);
                }
                LISTENERS.removeAll(registryEntryId);
            }
        }
        
        public void register(Registry<T> registry, ResourceLocation location, Supplier<? extends T> reference) {
            if (!registered) {
                objects.put(location, reference);
            } else {
                T value = reference.get();
                Registry.register(registry, location, value);
                
                RegistryEntryId<?> registryEntryId = new RegistryEntryId<>(registry.key(), location);
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
        private final Supplier<IEventBus> eventBus;
        private final Map<ResourceKey<? extends Registry<?>>, Data<?>> registry = new HashMap<>();
        private final Multimap<ResourceKey<Registry<?>>, Consumer<Registrar<?>>> listeners = HashMultimap.create();
        
        record RegistryBuilderEntry(RegistryBuilder<?> builder, Consumer<IForgeRegistry<?>> forgeRegistry) {
        }
        
        @Nullable
        private List<RegistryBuilderEntry> builders = new ArrayList<>();
        
        public RegistryProviderImpl(String modId) {
            this.modId = modId;
            this.eventBus = Suppliers.memoize(() -> {
                IEventBus eventBus = EventBusesHooks.getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Can't get event bus for mod '" + modId + "' because it was not registered!"));
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
        public <T> Registrar<T> get(ResourceKey<Registry<T>> registryKey) {
            updateEventBus();
            ForgeRegistry<T> registry = RegistryManager.ACTIVE.getRegistry(registryKey.location());
            if (registry == null) {
                Registry<T> ts = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
                if (ts != null) {
                    return get(ts);
                }
                Registrar<?> customReg = RegistryProviderImpl.CUSTOM_REGS.get(registryKey);
                if (customReg != null) return (Registrar<T>) customReg;
                throw new IllegalArgumentException("Registry " + registryKey + " does not exist!");
            }
            return get(registry);
        }
        
        public <T> Registrar<T> get(IForgeRegistry<T> registry) {
            updateEventBus();
            return new ForgeBackedRegistryImpl<>(modId, this.registry, registry);
        }
        
        @Override
        public <T> Registrar<T> get(Registry<T> registry) {
            updateEventBus();
            return new VanillaBackedRegistryImpl<>(modId, this.registry, registry);
        }
        
        @Override
        public <T> void forRegistry(ResourceKey<Registry<T>> key, Consumer<Registrar<T>> consumer) {
            this.listeners.put((ResourceKey<net.minecraft.core.Registry<?>>) (ResourceKey<? extends net.minecraft.core.Registry<?>>) key,
                    (Consumer<Registrar<?>>) (Consumer<? extends Registrar<?>>) consumer);
        }
        
        @Override
        public <T> RegistrarBuilder<T> builder(Class<T> type, ResourceLocation registryId) {
            return new RegistryBuilderWrapper<>(this, new net.minecraftforge.registries.RegistryBuilder<>()
                    .setName(registryId), registryId);
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
                    Registrar<Object> archRegistry;
                    if (event.getForgeRegistry() != null) {
                        archRegistry = get(event.getForgeRegistry());
                    } else if (event.getVanillaRegistry() != null) {
                        archRegistry = get(event.getVanillaRegistry());
                    } else {
                        LOGGER.error("Unable to find registry from RegisterEvent as both vanilla and forge registries are null! " + event.getRegistryKey());
                        return;
                    }
                    
                    for (Map.Entry<ResourceKey<net.minecraft.core.Registry<?>>, Consumer<Registrar<?>>> entry : listeners.entries()) {
                        if (entry.getKey().location().equals(resourceKey.location())) {
                            entry.getValue().accept(archRegistry);
                        }
                    }
                });
            }
            
            @SubscribeEvent(priority = EventPriority.LOWEST)
            public void handleEventPost(RegisterEvent event) {
                Registrar<Object> archRegistry;
                if (event.getForgeRegistry() != null) {
                    archRegistry = get(event.getForgeRegistry());
                } else if (event.getVanillaRegistry() != null) {
                    archRegistry = get(event.getVanillaRegistry());
                } else {
                    LOGGER.error("Unable to find registry from RegisterEvent as both vanilla and forge registries are null! " + event.getRegistryKey());
                    return;
                }
                
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
                if (builders != null) {
                    for (RegistryBuilderEntry builder : builders) {
                        //noinspection rawtypes
                        event.create((RegistryBuilder<?>) builder.builder(), (Consumer) builder.forgeRegistry());
                    }
                    builders = null;
                }
            }
        }
    }
    
    public static class RegistryBuilderWrapper<T> implements RegistrarBuilder<T> {
        private final RegistryProviderImpl provider;
        private final net.minecraftforge.registries.RegistryBuilder<?> builder;
        private final ResourceLocation registryId;
        private boolean syncToClients = false;
        
        public RegistryBuilderWrapper(RegistryProviderImpl provider, RegistryBuilder<?> builder, ResourceLocation registryId) {
            this.provider = provider;
            this.builder = builder;
            this.registryId = registryId;
        }
        
        @Override
        public Registrar<T> build() {
            if (!syncToClients) builder.disableSync();
            builder.disableSaving();
            if (provider.builders == null) {
                throw new IllegalStateException("Cannot create registries when registries are already aggregated!");
            }
            final var registrarRef = new Registrar<?>[1];
            var registrar = (DelegatedRegistrar<T>) new DelegatedRegistrar<>(provider.modId, () -> java.util.Objects.requireNonNull(registrarRef[0], "Registry not yet initialized!"), registryId);
            var entry = new RegistryProviderImpl.RegistryBuilderEntry(builder, forgeRegistry -> {
                registrarRef[0] = provider.get(forgeRegistry);
                registrar.onRegister();
            });
            provider.builders.add(entry);
            //noinspection rawtypes
            RegistryProviderImpl.CUSTOM_REGS.put((ResourceKey) registrar.key(), registrar);
            return registrar;
        }
        
        @Override
        public RegistrarBuilder<T> option(RegistrarOption option) {
            if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.syncToClients = true;
            }
            return this;
        }
    }
    
    public static class VanillaBackedRegistryImpl<T> implements Registrar<T> {
        private final String modId;
        private Registry<T> delegate;
        private Map<ResourceKey<? extends Registry<?>>, Data<?>> registry;
        
        public VanillaBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, Data<?>> registry, Registry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            Registrar<T> registrar = this;
            return new RegistrySupplier<>() {
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
                    if (!(obj instanceof RegistrySupplier)) return false;
                    RegistrySupplier<?> other = (RegistrySupplier<?>) obj;
                    return other.getRegistryId().equals(getRegistryId()) && other.getId().equals(getId());
                }
                
                @Override
                public String toString() {
                    return getRegistryId() + "@" + id.toString();
                }
            };
        }
        
        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            Data<T> data = (Data<T>) registry.computeIfAbsent(key(), type -> new Data<>());
            data.register(delegate, id, supplier);
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
        public ResourceKey<? extends Registry<T>> key() {
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
                RegistrarManagerImpl.listen(key(), id, callback, true);
            }
        }
    }
    
    public static class ForgeBackedRegistryImpl<T> implements Registrar<T> {
        private final String modId;
        private IForgeRegistry<T> delegate;
        private Map<ResourceKey<? extends Registry<?>>, Data<?>> registry;
        
        public ForgeBackedRegistryImpl(String modId, Map<ResourceKey<? extends Registry<?>>, Data<?>> registry, IForgeRegistry<T> delegate) {
            this.modId = modId;
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            Registrar<T> registrar = this;
            return new RegistrySupplier<>() {
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
            Object[] objectArr = new Object[]{null};
            Data<T> data = (Data<T>) registry.computeIfAbsent(key(), type -> new Data<>());
            data.registerForForge(delegate, id, objectArr, supplier);
            Registrar<T> registrar = this;
            return new RegistrySupplier<>() {
                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(modId);
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
                    return id;
                }
                
                @Override
                public boolean isPresent() {
                    return objectArr[0] != null;
                }
                
                @Override
                public E get() {
                    E value = (E) objectArr[0];
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
        public ResourceKey<? extends Registry<T>> key() {
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
                RegistrarManagerImpl.listen(key(), id, callback, false);
            }
        }
    }
    
    public static class DelegatedRegistrar<T> implements Registrar<T> {
        private final String modId;
        private final Supplier<Registrar<T>> delegate;
        private final ResourceLocation registryId;
        private List<Runnable> onRegister = new ArrayList<>();
        
        public DelegatedRegistrar(String modId, Supplier<Registrar<T>> delegate, ResourceLocation registryId) {
            this.modId = modId;
            this.delegate = delegate;
            this.registryId = registryId;
        }
        
        public void onRegister() {
            if (onRegister != null) {
                for (Runnable runnable : onRegister) {
                    runnable.run();
                }
            }
            onRegister = null;
        }
        
        public boolean isReady() {
            return onRegister == null;
        }
        
        @Override
        public RegistrySupplier<T> delegate(ResourceLocation id) {
            if (isReady()) return delegate.get().delegate(id);
            return new RegistrySupplier<>() {
                @Override
                public RegistrarManager getRegistrarManager() {
                    return RegistrarManager.get(modId);
                }
                
                @Override
                public Registrar<T> getRegistrar() {
                    return DelegatedRegistrar.this;
                }
                
                @Override
                public ResourceLocation getRegistryId() {
                    return DelegatedRegistrar.this.key().location();
                }
                
                @Override
                public ResourceLocation getId() {
                    return id;
                }
                
                @Override
                public boolean isPresent() {
                    return isReady() && delegate.get().contains(id);
                }
                
                @Override
                public T get() {
                    return isReady() ? delegate.get().get(id) : null;
                }
            };
        }
        
        @Override
        public <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            if (isReady()) return delegate.get().register(id, supplier);
            onRegister.add(() -> delegate.get().register(id, supplier));
            return (RegistrySupplier<E>) delegate(id);
        }
        
        @Override
        @Nullable
        public ResourceLocation getId(T obj) {
            return !isReady() ? null : delegate.get().getId(obj);
        }
        
        @Override
        public int getRawId(T obj) {
            return !isReady() ? -1 : delegate.get().getRawId(obj);
        }
        
        @Override
        public Optional<ResourceKey<T>> getKey(T obj) {
            return !isReady() ? Optional.empty() : delegate.get().getKey(obj);
        }
        
        @Override
        @Nullable
        public T get(ResourceLocation id) {
            return !isReady() ? null : delegate.get().get(id);
        }
        
        @Override
        @Nullable
        public T byRawId(int rawId) {
            return !isReady() ? null : delegate.get().byRawId(rawId);
        }
        
        @Override
        public boolean contains(ResourceLocation id) {
            return isReady() && delegate.get().contains(id);
        }
        
        @Override
        public boolean containsValue(T obj) {
            return isReady() && delegate.get().containsValue(obj);
        }
        
        @Override
        public Set<ResourceLocation> getIds() {
            return isReady() ? delegate.get().getIds() : Collections.emptySet();
        }
        
        @Override
        public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
            return isReady() ? delegate.get().entrySet() : Collections.emptySet();
        }
        
        @Override
        public ResourceKey<? extends Registry<T>> key() {
            return isReady() ? delegate.get().key() : ResourceKey.createRegistryKey(registryId);
        }
        
        @Override
        public void listen(ResourceLocation id, Consumer<T> callback) {
            if (isReady()) {
                delegate.get().listen(id, callback);
            } else {
                onRegister.add(() -> delegate.get().listen(id, callback));
            }
        }
        
        @NotNull
        @Override
        public Iterator<T> iterator() {
            return isReady() ? delegate.get().iterator() : Collections.emptyIterator();
        }
    }
}
