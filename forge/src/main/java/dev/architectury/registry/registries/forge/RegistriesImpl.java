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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistriesImpl {
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
        
        public void register(IForgeRegistry registry, RegistryObject object, Supplier<? extends IForgeRegistryEntry<?>> reference) {
            if (!collected) {
                objects.put(object, reference);
            } else {
                registry.register(reference.get());
                object.updateReference(registry);
            }
        }
    }
    
    public static class RegistryProviderImpl implements Registries.RegistryProvider {
        private final String modId;
        private final Supplier<IEventBus> eventBus;
        private final Map<Type, Data> registry = new HashMap<>();
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
            return get(RegistryManager.ACTIVE.getRegistry(registryKey.location()));
        }
        
        public <T> Registrar<T> get(IForgeRegistry registry) {
            updateEventBus();
            return new ForgeBackedRegistryImpl<>(this.registry, registry);
        }
        
        @Override
        public <T> Registrar<T> get(net.minecraft.core.Registry<T> registry) {
            updateEventBus();
            return new VanillaBackedRegistryImpl<>(registry);
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
            @SubscribeEvent
            public void handleEvent(RegistryEvent.Register event) {
                IForgeRegistry registry = event.getRegistry();
                Registrar<Object> archRegistry = get(registry);
                
                for (Map.Entry<Type, Data> typeDataEntry : RegistryProviderImpl.this.registry.entrySet()) {
                    if (typeDataEntry.getKey() == registry.getRegistrySuperType()) {
                        Data data = typeDataEntry.getValue();
                        
                        data.collected = true;
                        
                        for (Map.Entry<RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> entry : data.objects.entrySet()) {
                            registry.register(entry.getValue().get());
                            entry.getKey().updateReference(registry);
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
        }
    }
    
    public static class RegistryBuilderWrapper<T> implements RegistrarBuilder<T> {
        @NotNull
        private final RegistryProviderImpl provider;
        @NotNull
        private final net.minecraftforge.registries.RegistryBuilder<?> builder;
        private boolean saveToDisk = false;
        private boolean syncToClients = false;
        
        public RegistryBuilderWrapper(@NotNull RegistryProviderImpl provider, @NotNull net.minecraftforge.registries.RegistryBuilder<?> builder) {
            this.provider = provider;
            this.builder = builder;
        }
        
        @Override
        public @NotNull Registrar<T> build() {
            if (!syncToClients) builder.disableSync();
            if (!saveToDisk) builder.disableSaving();
            return provider.get(builder.create());
        }
        
        @Override
        public @NotNull RegistrarBuilder<T> option(@NotNull RegistrarOption option) {
            if (option == StandardRegistrarOption.SAVE_TO_DISC) {
                this.saveToDisk = true;
            } else if (option == StandardRegistrarOption.SYNC_TO_CLIENTS) {
                this.syncToClients = true;
            }
            return this;
        }
    }
    
    public static class VanillaBackedRegistryImpl<T> implements Registrar<T> {
        private net.minecraft.core.Registry<T> delegate;
        
        public VanillaBackedRegistryImpl(net.minecraft.core.Registry<T> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public @NotNull RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
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
        public @NotNull <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            net.minecraft.core.Registry.register(delegate, id, supplier.get());
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
    }
    
    public static class ForgeBackedRegistryImpl<T extends IForgeRegistryEntry<T>> implements Registrar<T> {
        private IForgeRegistry<T> delegate;
        private Map<Type, Data> registry;
        
        public ForgeBackedRegistryImpl(Map<Type, Data> registry, IForgeRegistry<T> delegate) {
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public @NotNull RegistrySupplier<T> delegate(ResourceLocation id) {
            Supplier<T> value = Suppliers.memoize(() -> get(id));
            return new RegistrySupplier<T>() {
                @Override
                public @NotNull ResourceLocation getRegistryId() {
                    return delegate.getRegistryName();
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
        public @NotNull <E extends T> RegistrySupplier<E> register(ResourceLocation id, Supplier<E> supplier) {
            RegistryObject registryObject = RegistryObject.of(id, delegate);
            registry.computeIfAbsent(delegate.getRegistrySuperType(), type -> new Data())
                    .register(delegate, registryObject, () -> supplier.get().setRegistryName(id));
            return new RegistrySupplier<E>() {
                @Override
                public @NotNull ResourceLocation getRegistryId() {
                    return delegate.getRegistryName();
                }
                
                @Override
                public @NotNull ResourceLocation getId() {
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
    }
}
