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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ChunkWatchEvent {
    /**
     * This event is fired whenever a {@link ServerPlayer} begins watching a chunk and the chunk is queued up for
     * sending to the client.
     * <p>
     * This event must NOT be used to send additional chunk-related data to the client as the client will not be aware
     * of the chunk yet when this event fires. {@link ChunkWatchEvent#SENT} should be used for this purpose instead
     */
    Event<ChunkListener> WATCH = EventFactory.createLoop();
    
    /**
     * This event is fired whenever a chunk being watched by a {@link ServerPlayer} is transmitted to their client.
     * <p>
     * This event may be used to send additional chunk-related data to the client.
     */
    Event<ChunkListener> SENT = EventFactory.createLoop();
    
    /**
     * This event is fired whenever a {@link ServerPlayer} stops watching a chunk. The chunk this event fires for
     * may never have actually been known to the client if the chunk goes out of range before being sent due to
     * slow pacing of chunk sync on slow connections or to slow clients.
     */
    Event<ChunkPosListener> UNWATCH = EventFactory.createLoop();
    
    @FunctionalInterface
    interface ChunkListener {
        void listen(LevelChunk chunk, ServerLevel level, ServerPlayer player);
    }
    
    @FunctionalInterface
    interface ChunkPosListener {
        void listen(ChunkPos chunkPos, ServerLevel level, ServerPlayer player);
    }
}
