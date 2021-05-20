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

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface LifecycleEvent {
    /**
     * Invoked when server is starting.
     * Equal to the forge {@code FMLServerAboutToStartEvent} event and
     * fabric's {@code ServerLifecycleEvents#SERVER_STARTING}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_BEFORE_START = EventFactory.createLoop();
    /**
     * Invoked when server is starting.
     * Equal to the forge {@code FMLServerStartingEvent} event.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STARTING = EventFactory.createLoop();
    /**
     * Invoked when server has started.
     * Equal to the forge {@code FMLServerStartedEvent} event
     * and fabric's {@code ServerLifecycleEvents#SERVER_STARTED}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STARTED = EventFactory.createLoop();
    /**
     * Invoked when server is stopping.
     * Equal to the forge {@code FMLServerStoppingEvent} event and
     * fabric's {@code ServerLifecycleEvents#SERVER_STOPPING}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STOPPING = EventFactory.createLoop();
    /**
     * Invoked when server has stopped.
     * Equal to the forge {@code FMLServerStoppedEvent} event and
     * fabric's {@code ServerLifecycleEvents#SERVER_STOPPED}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STOPPED = EventFactory.createLoop();
    /**
     * Invoked after a world is loaded only on server.
     * Equal to the forge {@code WorldEvent.Load} event and
     * fabric's {@code ServerWorldEvents#LOAD}.
     *
     * @see ServerWorldState#act(Level)
     */
    Event<ServerWorldState> SERVER_WORLD_LOAD = EventFactory.createLoop();
    /**
     * Invoked after a world is unloaded.
     * Equal to the forge {@code WorldEvent.Unload} event and
     * fabric's {@code ServerWorldEvents#UNLOAD}.
     *
     * @see ServerWorldState#act(Level)
     */
    Event<ServerWorldState> SERVER_WORLD_UNLOAD = EventFactory.createLoop();
    /**
     * Invoked when the world is being saved.
     * Equal to forge's {@code WorldEvent.Save} event.
     *
     * @see ServerWorldState#act(Level)
     */
    Event<ServerWorldState> SERVER_WORLD_SAVE = EventFactory.createLoop();
    
    interface InstanceState<T> {
        /**
         * Parent event type for any events that are invoked on instance state change.
         *
         * @param instance The changed state.
         */
        void stateChanged(T instance);
    }
    
    interface ServerState extends InstanceState<MinecraftServer> {
    }
    
    interface WorldState<T extends Level> {
        /**
         * Parent event type for any events that are invoked on world state change.
         *
         * @param world The world that has changed.
         */
        void act(T world);
    }
    
    interface ServerWorldState extends WorldState<ServerLevel> {
    }
}
