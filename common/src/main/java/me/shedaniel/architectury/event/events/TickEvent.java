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

public interface TickEvent<T> {
    @Environment(EnvType.CLIENT) Event<Client> CLIENT_PRE = EventFactory.createLoop(Client.class);
    @Environment(EnvType.CLIENT) Event<Client> CLIENT_POST = EventFactory.createLoop(Client.class);
    Event<Server> SERVER_PRE = EventFactory.createLoop(Server.class);
    Event<Server> SERVER_POST = EventFactory.createLoop(Server.class);
    @Environment(EnvType.CLIENT) Event<ClientWorld> CLIENT_WORLD_PRE = EventFactory.createLoop(ClientWorld.class);
    @Environment(EnvType.CLIENT) Event<ClientWorld> CLIENT_WORLD_POST = EventFactory.createLoop(ClientWorld.class);
    Event<ServerWorld> SERVER_WORLD_PRE = EventFactory.createLoop(ServerWorld.class);
    Event<ServerWorld> SERVER_WORLD_POST = EventFactory.createLoop(ServerWorld.class);
    
    void tick(T instance);
    
    @Environment(EnvType.CLIENT)
    interface Client extends TickEvent<Minecraft> {}
    
    interface Server extends TickEvent<MinecraftServer> {}
    
    interface WorldTick<T extends Level> extends TickEvent<T> {}
    
    @Environment(EnvType.CLIENT)
    interface ClientWorld extends WorldTick<ClientLevel> {}
    
    interface ServerWorld extends WorldTick<ServerLevel> {}
}
