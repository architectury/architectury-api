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
import net.minecraft.server.MinecraftServer;

public interface LifecycleEvent {
    @Environment(EnvType.CLIENT)
    Event<ClientState> CLIENT_STARTED = EventFactory.createLoop(ClientState.class);
    @Environment(EnvType.CLIENT)
    Event<ClientState> CLIENT_STOPPING = EventFactory.createLoop(ClientState.class);
    Event<ServerState> SERVER_STARTING = EventFactory.createLoop(ServerState.class);
    Event<ServerState> SERVER_STARTED = EventFactory.createLoop(ServerState.class);
    Event<ServerState> SERVER_STOPPING = EventFactory.createLoop(ServerState.class);
    Event<ServerState> SERVER_STOPPED = EventFactory.createLoop(ServerState.class);
    
    interface InstanceState<T> {
        void stateChanged(T instance);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientState extends InstanceState<Minecraft> {}
    
    interface ServerState extends InstanceState<MinecraftServer> {}
}
