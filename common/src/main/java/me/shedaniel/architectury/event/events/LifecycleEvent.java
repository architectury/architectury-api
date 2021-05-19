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
     * Invoked when server is starting, equivalent to forge's {@code FMLServerAboutToStartEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STARTING}.
     */
    Event<ServerState> SERVER_BEFORE_START = EventFactory.createLoop();
    /**
     * Invoked when server is starting, equivalent to forge's {@code FMLServerStartingEvent}.
     */
    Event<ServerState> SERVER_STARTING = EventFactory.createLoop();
    /**
     * Invoked when server has started, equivalent to forge's {@code FMLServerStartedEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STARTED}.
     */
    Event<ServerState> SERVER_STARTED = EventFactory.createLoop();
    /**
     * Invoked when server is stopping, equivalent to forge's {@code FMLServerStoppingEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STOPPING}.
     */
    Event<ServerState> SERVER_STOPPING = EventFactory.createLoop();
    /**
     * Invoked when server has stopped, equivalent to forge's {@code FMLServerStoppedEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STOPPED}.
     */
    Event<ServerState> SERVER_STOPPED = EventFactory.createLoop();
    /**
     * Invoked after a world is loaded only on server, equivalent to forge's {@code WorldEvent.Load} and fabric's {@code ServerWorldEvents#LOAD}.
     */
    Event<ServerWorldState> SERVER_WORLD_LOAD = EventFactory.createLoop();
    /**
     * Invoked after a world is unloaded, equivalent to forge's {@code WorldEvent.Unload} and fabric's {@code ServerWorldEvents#UNLOAD}.
     */
    Event<ServerWorldState> SERVER_WORLD_UNLOAD = EventFactory.createLoop();
    /**
     * Invoked during a world is saved, equivalent to forge's {@code WorldEvent.Save}.
     */
    Event<ServerWorldState> SERVER_WORLD_SAVE = EventFactory.createLoop();
    
    interface InstanceState<T> {
        void stateChanged(T instance);
    }
    
    interface ServerState extends InstanceState<MinecraftServer> {
    }
    
    interface WorldState<T extends Level> {
        void act(T world);
    }
    
    interface ServerWorldState extends WorldState<ServerLevel> {
    }
}
