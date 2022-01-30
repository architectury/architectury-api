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

package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.common.LifecycleEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
public interface ClientLifecycleEvent {
    /**
     * Invoked when client has been initialised.
     * Equivalent to Fabric's {@code ClientLifecycleEvents.CLIENT_STARTING}.
     */
    Event<ClientState> CLIENT_STARTED = EventFactory.createLoop();
    /**
     * Invoked when client is stopping.
     * Equivalent to Fabric's {@code ClientLifecycleEvents.CLIENT_STOPPING}.
     */
    Event<ClientState> CLIENT_STOPPING = EventFactory.createLoop();
    /**
     * Invoked after a level is loaded only on the client-side.
     * Equivalent to Forge's {@code WorldEvent.Load} event (on client).
     */
    Event<ClientLevelState> CLIENT_LEVEL_LOAD = EventFactory.createLoop();
    /**
     * Invoked once client setup has begun.
     * <p> This happens during {@code FMLClientSetupEvent} on Forge,
     * or when Architectury API's client entrypoint initialises on Fabric.
     * <p>
     * Registries should have been initialised by this point, but there
     * are no such guarantees, as you can modify the registry beyond this point
     * on non-Forge environments.
     */
    Event<ClientState> CLIENT_SETUP = EventFactory.createLoop();
    
    @Environment(EnvType.CLIENT)
    interface ClientState extends LifecycleEvent.InstanceState<Minecraft> {
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientLevelState extends LifecycleEvent.LevelState<ClientLevel> {
    }
}
