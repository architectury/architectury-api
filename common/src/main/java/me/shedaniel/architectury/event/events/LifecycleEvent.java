/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;


public interface LifecycleEvent {
    /**
     * Invoked when client has been initialised, not available in forge.
     */
    @Deprecated @Environment(EnvType.CLIENT) Event<ClientState> CLIENT_STARTED = EventFactory.createLoop(ClientState.class);
    /**
     * Invoked when client is initialising, not available in forge.
     */
    @Deprecated @Environment(EnvType.CLIENT) Event<ClientState> CLIENT_STOPPING = EventFactory.createLoop(ClientState.class);
    /**
     * Invoked when server is starting, equivalent to forge's {@code FMLServerStartingEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STARTING}.
     */
    Event<ServerState> SERVER_STARTING = EventFactory.createLoop(ServerState.class);
    /**
     * Invoked when server has started, equivalent to forge's {@code FMLServerStartedEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STARTED}.
     */
    Event<ServerState> SERVER_STARTED = EventFactory.createLoop(ServerState.class);
    /**
     * Invoked when server is stopping, equivalent to forge's {@code FMLServerStoppingEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STOPPING}.
     */
    Event<ServerState> SERVER_STOPPING = EventFactory.createLoop(ServerState.class);
    /**
     * Invoked when server has stopped, equivalent to forge's {@code FMLServerStoppedEvent} and fabric's {@code ServerLifecycleEvents#SERVER_STOPPED}.
     */
    Event<ServerState> SERVER_STOPPED = EventFactory.createLoop(ServerState.class);
    /**
     * Invoked after a world is loaded only on server, equivalent to forge's {@code WorldEvent.Load}.
     */
    Event<ServerWorldState> SERVER_WORLD_LOAD = EventFactory.createLoop(ServerWorldState.class);
    /**
     * Invoked during a world is saved, equivalent to forge's {@code WorldEvent.Save}.
     */
    Event<ServerWorldState> SERVER_WORLD_SAVE = EventFactory.createLoop(ServerWorldState.class);
    /**
     * Invoked after a world is loaded only on client, equivalent to forge's {@code WorldEvent.Load}.
     */
    @Environment(EnvType.CLIENT) Event<ClientWorldState> CLIENT_WORLD_LOAD = EventFactory.createLoop(ClientWorldState.class);
    
    interface InstanceState<T> {
        void stateChanged(T instance);
    }
    
    @Deprecated
    @Environment(EnvType.CLIENT)
    interface ClientState extends InstanceState<Minecraft> {}
    
    interface ServerState extends InstanceState<MinecraftServer> {}
    
    interface WorldState<T extends Level> {
        void act(T world);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientWorldState extends WorldState<ClientLevel> {}
    
    interface ServerWorldState extends WorldState<ServerLevel> {}
}
