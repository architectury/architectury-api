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

package me.shedaniel.architectury.core.access.builtin;

import me.shedaniel.architectury.core.access.builtin.util.CombinedStorage;
import me.shedaniel.architectury.core.access.builtin.util.EmptyStorage;

import java.util.Collection;

public interface Storage<T> {
    /**
     * Returns a combined {@link Storage} from a collection of storages.
     *
     * @param storages the collection of storages, storages will be evaluated by its order.
     * @param <T>      the type of storage
     * @return the combined storage
     */
    static <T> Storage<T> combine(Collection<Storage<T>> storages) {
        if (storages.size() == 0) return EmptyStorage.empty();
        if (storages.size() == 1) return storages.iterator().next();
        return new CombinedStorage<>(storages);
    }
    
    /**
     * Extracts {@link T} from the storage,
     *
     * @param type        the type to extract
     * @param maxAmount   the maximum amount to extract
     * @param transaction the transaction of this extraction
     * @return the amount extracted
     */
    long extract(T type, long maxAmount, Transaction transaction);
    
    /**
     * Inserts {@link T} into the storage,
     *
     * @param type        the type to insert
     * @param amount      the amount to insert
     * @param transaction the transaction of this insertion
     * @return the amount inserted
     */
    long insert(T type, long amount, Transaction transaction);
}
