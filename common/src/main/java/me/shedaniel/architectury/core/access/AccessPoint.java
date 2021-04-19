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

import me.shedaniel.architectury.impl.access.AccessPointImpl;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An access point used to bridge providers into a single provider.
 *
 * @param <T> the type of provider
 * @apiNote You must not implement this class directly, either use {@link #create(Function)} or {@link DelegateAccessPoint}
 */
public interface AccessPoint<T, SELF extends AccessPoint<T, SELF>> extends Access<T> {
    /**
     * Creates an access.
     *
     * @param compiler the function used to combine an {@link Collection}
     *                 of {@link Supplier} into the desired type
     */
    static <T> AccessPoint<T, ?> create(Function<Collection<T>, T> compiler) {
        return new AccessPointImpl<>(compiler);
    }
    
    /**
     * Adds a listener which is fired when the access is modified.
     *
     * @param listener the listener fired when the access is modified
     * @return the same {@link AccessPoint} instance
     */
    SELF addListener(Consumer<SELF> listener);
    
    /**
     * Adds a provider to the access, and causes the access to recompile.
     *
     * @param provider the provider to add
     * @return the same {@link AccessPoint} instance
     */
    SELF add(T provider);
    
    /**
     * Adds providers to the access, and causes the access to recompile.
     *
     * @param providers the providers to add
     * @return the same {@link AccessPoint} instance
     */
    SELF addAll(Collection<T> providers);
    
    SELF dependsOn(SELF access);
    
    <E> SELF dependsOn(AccessPoint<E, ?> accessPoint, Function<E, T> function);
}
