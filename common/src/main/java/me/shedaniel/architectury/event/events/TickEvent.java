/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

public interface TickEvent<T> {
    Event<Server> SERVER_PRE = EventFactory.createLoop(Server.class);
    Event<Server> SERVER_POST = EventFactory.createLoop(Server.class);
    Event<ServerWorld> SERVER_WORLD_PRE = EventFactory.createLoop(ServerWorld.class);
    Event<ServerWorld> SERVER_WORLD_POST = EventFactory.createLoop(ServerWorld.class);
    Event<Player> PLAYER_PRE = EventFactory.createLoop(Player.class);
    Event<Player> PLAYER_POST = EventFactory.createLoop(Player.class);
    
    void tick(T instance);
    
    interface Server extends TickEvent<MinecraftServer> {}
    
    interface WorldTick<T extends Level> extends TickEvent<T> {}
    
    interface ServerWorld extends WorldTick<ServerLevel> {}
    
    interface Player extends TickEvent<net.minecraft.world.entity.player.Player> {}
}
