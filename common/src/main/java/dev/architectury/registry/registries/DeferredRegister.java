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

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class DeferredRegister<T> implements Iterable<RegistrySupplier<T>> {
    private final Supplier<RegistrarManager> registriesSupplier;
    private final ResourceKey<Registry<T>> key;
    private final List<Entry<T>> entries = new ArrayList<>();
    private final List<RegistrySupplier<T>> entryView = Collections.unmodifiableList(this.entries);
    private boolean registered = false;
    @Nullable
    private final String modId;
    
    private DeferredRegister(Supplier<RegistrarManager> registriesSupplier, ResourceKey<Registry<T>> key, @Nullable String modId) {
        this.registriesSupplier = Objects.requireNonNull(registriesSupplier);
        this.key = Objects.requireNonNull(key);
        this.modId = modId;
    }
    
    public static <T> DeferredRegister<T> create(String modId, ResourceKey<Registry<T>> key) {
        Supplier<RegistrarManager> value = Suppliers.memoize(() -> RegistrarManager.get(modId));
        return new DeferredRegister<>(value, key, Objects.requireNonNull(modId));
    }
    
    public <R extends T> RegistrySupplier<R> register(String id, Supplier<? extends R> supplier) {
        if (modId == null) {
            throw new NullPointerException("You must create the deferred register with a mod id to register entries without the namespace!");
        }
        
        return register(new ResourceLocation(modId, id), supplier);
    }
    
    public <R extends T> RegistrySupplier<R> register(ResourceLocation id, Supplier<? extends R> supplier) {
        var entry = new Entry<T>(id, (Supplier<T>) supplier);
        this.entries.add(entry);
        if (registered) {
            var registrar = getRegistrar();
            entry.value = registrar.register(entry.id, entry.supplier);
        }
        return (RegistrySupplier<R>) entry;
    }
    
    public void register() {
        if (registered) {
            throw new IllegalStateException("Cannot register a deferred register twice!");
        }
        registered = true;
        var registrar = getRegistrar();
        for (var entry : entries) {
            entry.value = registrar.register(entry.id, entry.supplier);
        }
    }
    
    @Override
    public Iterator<RegistrySupplier<T>> iterator() {
        return entryView.iterator();
    }
    
    public RegistrarManager getRegistrarManager() {
        return registriesSupplier.get();
    }
    
    public Registrar<T> getRegistrar() {
        return registriesSupplier.get().get(key);
    }
    
    private class Entry<R> implements RegistrySupplier<R> {
        private final ResourceLocation id;
        private final Supplier<R> supplier;
        private RegistrySupplier<R> value;
        
        public Entry(ResourceLocation id, Supplier<R> supplier) {
            this.id = id;
            this.supplier = supplier;
        }
        
        @Override
        public RegistrarManager getRegistrarManager() {
            return DeferredRegister.this.getRegistrarManager();
        }
        
        @Override
        public Registrar<R> getRegistrar() {
            return (Registrar<R>) DeferredRegister.this.getRegistrar();
        }
        
        @Override
        public ResourceLocation getRegistryId() {
            return key.location();
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public boolean isPresent() {
            return value != null && value.isPresent();
        }
        
        @Override
        public R get() {
            if (isPresent()) {
                return value.get();
            }
            throw new NullPointerException("Registry Object not present: " + this.id);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(getRegistryId(), getId());
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
        
        public Collection<RegistrySupplier<T>> getEntries()
        {
            return entryView;
        }
    }
}
