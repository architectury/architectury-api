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

import me.shedaniel.architectury.core.access.specific.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@FunctionalInterface
public interface Access<T> {
    /**
     * Returns {@link T} by providers.
     *
     * @return the single provider compiled from providers.
     */
    T get();
    
    default Access<T> filter(BooleanSupplier predicate) {
        return () -> {
            if (predicate.getAsBoolean()) {
                return this.get();
            }
            return null;
        };
    }
}
