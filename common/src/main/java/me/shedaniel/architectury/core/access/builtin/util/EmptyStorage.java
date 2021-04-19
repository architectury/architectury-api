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

public final class EmptyStorage implements Storage<Object> {
    private static final Storage<Object> EMPTY = new EmptyStorage();
    
    public static <T> Storage<T> empty() {
        return (Storage<T>) EMPTY;
    }
    
    @Override
    public long extract(Object type, long maxAmount, Transaction transaction) {
        return 0;
    }
    
    @Override
    public long insert(Object type, long amount, Transaction transaction) {
        return 0;
    }
}
