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

package me.shedaniel.architectury.core.access.builtin.util;

import me.shedaniel.architectury.core.access.builtin.Storage;
import me.shedaniel.architectury.core.access.builtin.Transaction;

public class CombinedStorage<T> implements Storage<T> {
    private final Iterable<Storage<T>> storages;
    
    public CombinedStorage(Iterable<Storage<T>> storages) {
        this.storages = storages;
    }
    
    @Override
    public long extract(T type, long maxAmount, Transaction transaction) {
        long totalExtracted = 0;
        for (Storage<T> storage : storages) {
            long extracted = storage.extract(type, maxAmount - totalExtracted, transaction);
            totalExtracted += extracted;
            if (totalExtracted >= maxAmount) {
                return totalExtracted;
            }
        }
        return totalExtracted;
    }
    
    @Override
    public long insert(T type, long amount, Transaction transaction) {
        long totalInserted = 0;
        for (Storage<T> storage : storages) {
            long extracted = storage.insert(type, amount - totalInserted, transaction);
            totalInserted += extracted;
            if (totalInserted >= amount) {
                return totalInserted;
            }
        }
        return totalInserted;
    }
}
