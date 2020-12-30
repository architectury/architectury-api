/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

import com.google.common.base.Objects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DeferredRegister<T> {
    private final Supplier<Registries> registriesSupplier;
    private final ResourceKey<net.minecraft.core.Registry<T>> key;
    private final List<Entry<T>> entries = new ArrayList<>();
    private boolean registered = false;
    
    private DeferredRegister(Supplier<Registries> registriesSupplier, ResourceKey<net.minecraft.core.Registry<T>> key) {
        this.registriesSupplier = registriesSupplier;
        this.key = key;
    }
    
    @NotNull
    public static <T> DeferredRegister<T> create(Registries registries, ResourceKey<net.minecraft.core.Registry<T>> key) {
        return new DeferredRegister<>(() -> registries, key);
    }
    
    @NotNull
    public static <T> DeferredRegister<T> create(Supplier<Registries> registries, ResourceKey<net.minecraft.core.Registry<T>> key) {
        return new DeferredRegister<>(registries, key);
    }
    
    @NotNull
    public static <T> DeferredRegister<T> create(LazyLoadedValue<Registries> registries, ResourceKey<net.minecraft.core.Registry<T>> key) {
        return create(registries::get, key);
    }
    
    public RegistrySupplier<T> register(ResourceLocation id, Supplier<T> supplier) {
        Entry<T> entry = new Entry<>(id, supplier);
        this.entries.add(entry);
        if (registered) {
            Registry<T> registry = registriesSupplier.get().get(key);
            entry.value = registry.registerSupplied(entry.id, entry.supplier);
        }
        return entry;
    }
    
    public void register() {
        if (registered) {
            throw new IllegalStateException("Cannot register a deferred register twice!");
        }
        registered = true;
        Registry<T> registry = registriesSupplier.get().get(key);
        for (Entry<T> entry : entries) {
            entry.value = registry.registerSupplied(entry.id, entry.supplier);
        }
    }
    
    private class Entry<T> implements RegistrySupplier<T> {
        private final ResourceLocation id;
        private final Supplier<T> supplier;
        private RegistrySupplier<T> value;
    
        public Entry(ResourceLocation id, Supplier<T> supplier) {
            this.id = id;
            this.supplier = supplier;
        }
    
        @Override
        public @NotNull ResourceLocation getRegistryId() {
            return key.location();
        }
    
        @Override
        public @NotNull ResourceLocation getId() {
            return id;
        }
    
        @Override
        public boolean isPresent() {
            return value != null && value.isPresent();
        }
    
        @Override
        public T get() {
            if (isPresent()) {
                return value.get();
            }
            throw new NullPointerException("Registry Object not present: " + this.id);
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
    }
}
