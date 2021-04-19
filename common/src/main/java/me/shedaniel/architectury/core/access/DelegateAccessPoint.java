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

package me.shedaniel.architectury.core.access;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public interface DelegateAccessPoint<T, SELF extends AccessPoint<T, SELF>> extends AccessPoint<T, SELF> {
    AccessPoint<T, ?> getParent();
    
    @ApiStatus.Internal
    default AccessPoint<T, SELF> castedParent() {
        return (AccessPoint<T, SELF>) getParent();
    }
    
    @Override
    default T get() {
        return getParent().get();
    }
    
    @Override
    default SELF addListener(Consumer<SELF> listener) {
        return castedParent().addListener(listener);
    }
    
    @Override
    default SELF add(T provider) {
        return castedParent().add(provider);
    }
    
    @Override
    default SELF addAll(Collection<T> providers) {
        return castedParent().addAll(providers);
    }
    
    @Override
    default SELF dependsOn(SELF access) {
        return castedParent().dependsOn(access);
    }
    
    @Override
    default <E> SELF dependsOn(AccessPoint<E, ?> accessPoint, Function<E, T> function) {
        return castedParent().dependsOn(accessPoint, function);
    }
}
