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

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface RegistrySupplier<T> extends Supplier<T> {
    Registries getRegistries();
    
    Registrar<T> getRegistrar();
    
    /**
     * @return the identifier of the registry
     */
    ResourceLocation getRegistryId();
    
    /**
     * @return the identifier of the registry
     */
    default ResourceKey<Registry<T>> getRegistryKey() {
        return ResourceKey.createRegistryKey(getRegistryId());
    }
    
    /**
     * @return the identifier of the entry
     */
    ResourceLocation getId();
    
    /**
     * @return whether the entry has been registered
     */
    boolean isPresent();
    
    @Nullable
    default T getOrNull() {
        if (isPresent()) {
            return get();
        }
        return null;
    }
    
    default Optional<T> toOptional() {
        return Optional.ofNullable(getOrNull());
    }
    
    default void ifPresent(Consumer<? super T> action) {
        if (isPresent()) {
            action.accept(get());
        }
    }
    
    default void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (isPresent()) {
            action.accept(get());
        } else {
            emptyAction.run();
        }
    }
    
    default Stream<T> stream() {
        if (!isPresent()) {
            return Stream.empty();
        } else {
            return Stream.of(get());
        }
    }
    
    default T orElse(T other) {
        return isPresent() ? get() : other;
    }
    
    default T orElseGet(Supplier<? extends T> supplier) {
        return isPresent() ? get() : supplier.get();
    }
    
    /**
     * Listens to when the registry entry is registered, and calls the given action.
     * Evaluates immediately if the entry is already registered.
     *
     * @param callback the action to call when the registry entry is registered
     */
    default void listen(Consumer<T> callback) {
        getRegistrar().listen(this, callback);
    }
}
