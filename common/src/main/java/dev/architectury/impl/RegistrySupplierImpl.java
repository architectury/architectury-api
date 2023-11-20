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

package dev.architectury.impl;

import com.mojang.datafixers.util.Either;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@ApiStatus.Internal
public interface RegistrySupplierImpl<T> extends RegistrySupplier<T> {
    @Nullable
    Holder<T> getHolder();
    
    @Override
    default T value() {
        return get();
    }
    
    @Override
    default boolean isBound() {
        return isPresent();
    }
    
    @Override
    default boolean is(ResourceLocation resourceLocation) {
        return getId().equals(resourceLocation);
    }
    
    @Override
    default boolean is(ResourceKey<T> resourceKey) {
        return getKey().equals(resourceKey);
    }
    
    @Override
    default boolean is(Predicate<ResourceKey<T>> predicate) {
        return predicate.test(getKey());
    }
    
    @Override
    default boolean is(TagKey<T> tagKey) {
        Holder<T> holder = getHolder();
        return holder != null && holder.is(tagKey);
    }
    
    @Override
    default Stream<TagKey<T>> tags() {
        Holder<T> holder = getHolder();
        return holder != null ? holder.tags() : Stream.empty();
    }
    
    @Override
    default Either<ResourceKey<T>, T> unwrap() {
        return Either.left(getKey());
    }
    
    @Override
    default Optional<ResourceKey<T>> unwrapKey() {
        return Optional.of(getKey());
    }
    
    @Override
    default Kind kind() {
        return Kind.REFERENCE;
    }
    
    @Override
    default boolean canSerializeIn(HolderOwner<T> holderOwner) {
        Holder<T> holder = getHolder();
        return holder != null && holder.canSerializeIn(holderOwner);
    }
}
