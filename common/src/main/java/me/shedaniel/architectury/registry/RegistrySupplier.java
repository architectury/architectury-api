/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface RegistrySupplier<T> extends Supplier<T> {
    /**
     * @return the identifier of the registry
     */
    @NotNull
    ResourceLocation getRegistryId();
    
    /**
     * @return the identifier of the entry
     */
    @NotNull
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
    
    @NotNull
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
    
    @NotNull
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
}
