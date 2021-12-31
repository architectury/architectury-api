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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public interface LifecycleEvent {
    /**
     * Invoked before initial server startup. This is the earliest point at which the server will be available.
     * Equivalent to Forge's {@code FMLServerAboutToStartEvent} event and
     * Fabric's {@code ServerLifecycleEvents#SERVER_STARTING}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_BEFORE_START = EventFactory.createLoop();
    /**
     * Invoked during server startup.
     * Equivalent to Forge's {@code FMLServerStartingEvent} event.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STARTING = EventFactory.createLoop();
    /**
     * Invoked when the server has started and is ready to accept players.
     * Equivalent to Forge's {@code FMLServerStartedEvent} event
     * and Fabric's {@code ServerLifecycleEvents#SERVER_STARTED}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STARTED = EventFactory.createLoop();
    /**
     * Invoked when the server begins shutting down.
     * Equivalent to Forge's {@code FMLServerStoppingEvent} event and
     * Fabric's {@code ServerLifecycleEvents#SERVER_STOPPING}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STOPPING = EventFactory.createLoop();
    /**
     * Invoked when the server has finished stopping, and is about to fully shut down.
     * Equivalent to Forge's {@code FMLServerStoppedEvent} event and
     * Fabric's {@code ServerLifecycleEvents#SERVER_STOPPED}.
     *
     * @see ServerState#stateChanged(Object)
     */
    Event<ServerState> SERVER_STOPPED = EventFactory.createLoop();
    /**
     * Invoked when a level is loaded on the server-side.
     * Equivalent to Forge's {@code WorldEvent.Load} event (on server)
     * and Fabric's {@code ServerWorldEvents#LOAD}.
     *
     * @see ServerLevelState#act(Level)
     */
    Event<ServerLevelState> SERVER_LEVEL_LOAD = EventFactory.createLoop();
    /**
     * Invoked when a level is unloaded on the server-side.
     * Equivalent to Forge's {@code WorldEvent.Unload} event (on server)
     * and Fabric's {@code ServerWorldEvents#UNLOAD}.
     *
     * @see ServerLevelState#act(Level)
     */
    Event<ServerLevelState> SERVER_LEVEL_UNLOAD = EventFactory.createLoop();
    /**
     * Invoked when the level is being saved.
     * Equivalent to Forge's {@code WorldEvent.Save} event.
     *
     * @see ServerLevelState#act(Level)
     */
    Event<ServerLevelState> SERVER_LEVEL_SAVE = EventFactory.createLoop();
    /**
     * Invoked once common setup has begun.
     * <p> This happens during {@code FMLCommonSetupEvent} on Forge,
     * or when Architectury API's client/server entrypoint initialises on Fabric.
     * <p>
     * Registries should have been initialised by this point, but there
     * are no such guarantees, as you can modify the registry beyond this point
     * on non-Forge environments.
     */
    Event<Runnable> SETUP = EventFactory.createLoop();
    
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
    
    interface LevelState<T extends Level> {
        /**
         * Parent event type for any events that are invoked on world state change.
         *
         * @param world The world that has changed.
         */
        void act(T world);
    }
    
    interface ServerLevelState extends LevelState<ServerLevel> {
    }
}
