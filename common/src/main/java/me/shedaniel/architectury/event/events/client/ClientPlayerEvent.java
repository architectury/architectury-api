/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.event.events.client;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

@Environment(EnvType.CLIENT)
public interface ClientPlayerEvent {
    Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createLoop(ClientPlayerJoin.class);
    Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createLoop(ClientPlayerQuit.class);
    Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createLoop(ClientPlayerRespawn.class);
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerQuit {
        void quit(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerRespawn {
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
