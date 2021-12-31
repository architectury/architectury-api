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

package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.jetbrains.annotations.Nullable;

public interface ChunkEvent {
    /**
     * @see SaveData#save(ChunkAccess, ServerLevel, CompoundTag)
     */
    Event<SaveData> SAVE_DATA = EventFactory.createLoop();
    /**
     * @see LoadData#load(ChunkAccess, ServerLevel, CompoundTag)
     */
    Event<LoadData> LOAD_DATA = EventFactory.createLoop();
    
    interface SaveData {
        /**
         * Invoked when a chunk's data is saved, just before the data is written.
         * Add your own data to the {@link CompoundTag} parameter to get your data saved as well.
         * Equivalent to Forge's {@code ChunkDataEvent.Save}.
         *
         * @param chunk The chunk that is saved.
         * @param level The level the chunk is in.
         * @param nbt   The chunk data that is written to the save file.
         */
        void save(ChunkAccess chunk, ServerLevel level, CompoundTag nbt);
    }
    
    interface LoadData {
        /**
         * Invoked just before a chunk's data is fully read.
         * You can read out your own data from the {@link CompoundTag} parameter, when you have saved one before.
         * Equivalent to Forge's {@code ChunkDataEvent.Load}.
         *
         * @param chunk The chunk that is loaded.
         * @param level The level the chunk is in, may be {@code null}.
         * @param nbt   The chunk data that was read from the save file.
         */
        void load(ChunkAccess chunk, @Nullable ServerLevel level, CompoundTag nbt);
    }
}
