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

package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.shedaniel.architectury.platform.forge.EventBuses;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.Registry;
import net.minecraft.util.LazyValue;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

public class RegistriesImpl implements Registries.Impl {
    @Override
    public Registries.RegistryProvider get(String modId) {
        return new RegistryProviderImpl(modId);
    }
    
    public static class RegistryProviderImpl implements Registries.RegistryProvider {
        private final String modId;
        private final IEventBus eventBus;
        private final Table<Type, RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> registry = HashBasedTable.create();
        
        public RegistryProviderImpl(String modId) {
            this.modId = modId;
            this.eventBus = EventBuses.getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Can't get event bus for mod '" + modId + "' because it was not registered!"));
            this.eventBus.register(new EventListener());
        }
        
        @Override
        public <T> Registry<T> get(RegistryKey<net.minecraft.util.registry.Registry<T>> registryKey) {
            return new ForgeBackedRegistryImpl<>(registry, (IForgeRegistry) RegistryManager.ACTIVE.getRegistry(registryKey.location()));
        }
        
        @Override
        public <T> Registry<T> get(net.minecraft.util.registry.Registry<T> registry) {
            return new VanillaBackedRegistryImpl<>(registry);
        }
        
        public class EventListener {
            @SubscribeEvent
            public void handleEvent(RegistryEvent.Register event) {
                IForgeRegistry registry = event.getRegistry();
                
                for (Map.Entry<Type, Map<RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>>> row : RegistryProviderImpl.this.registry.rowMap().entrySet()) {
                    if (row.getKey() == event.getGenericType()) {
                        for (Map.Entry<RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> entry : row.getValue().entrySet()) {
                            registry.register(entry.getValue().get());
                            entry.getKey().updateReference(registry);
                        }
                    }
                }
            }
        }
    }
    
    public static class VanillaBackedRegistryImpl<T> implements Registry<T> {
        private net.minecraft.util.registry.Registry<T> delegate;
        
        public VanillaBackedRegistryImpl(net.minecraft.util.registry.Registry<T> delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public Supplier<T> delegate(ResourceLocation id) {
            LazyValue<T> value = new LazyValue<>(() -> get(id));
            return value::get;
        }
        
        @Override
        public Supplier<T> register(ResourceLocation id, Supplier<T> supplier) {
            net.minecraft.util.registry.Registry.register(delegate, id, supplier.get());
            return delegate(id);
        }
        
        @Override
        @Nullable
        public ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        @Nullable
        public T get(ResourceLocation id) {
            return delegate.get(id);
        }
    }
    
    public static class ForgeBackedRegistryImpl<T extends IForgeRegistryEntry<T>> implements Registry<T> {
        private IForgeRegistry<T> delegate;
        private Table<Type, RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> registry;
        
        public ForgeBackedRegistryImpl(Table<Type, RegistryObject<?>, Supplier<? extends IForgeRegistryEntry<?>>> registry, IForgeRegistry<T> delegate) {
            this.registry = registry;
            this.delegate = delegate;
        }
        
        @Override
        public Supplier<T> delegate(ResourceLocation id) {
            LazyValue<T> value = new LazyValue<>(() -> get(id));
            return value::get;
        }
        
        @Override
        public Supplier<T> register(ResourceLocation id, Supplier<T> supplier) {
            RegistryObject registryObject = RegistryObject.of(id, delegate);
            registry.put(delegate.getRegistrySuperType(), registryObject, () -> supplier.get().setRegistryName(id));
            return registryObject;
        }
        
        @Override
        @Nullable
        public ResourceLocation getId(T obj) {
            return delegate.getKey(obj);
        }
        
        @Override
        @Nullable
        public T get(ResourceLocation id) {
            return delegate.getValue(id);
        }
    }
}
