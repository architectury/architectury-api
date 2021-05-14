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

package me.shedaniel.architectury.event.events.client;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ClientPlayerEvent {
    /**
     * @see ClientPlayerJoin#join(LocalPlayer)
     */
    Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createLoop();
    /**
     * @see ClientPlayerQuit#quit(LocalPlayer)
     */
    Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createLoop();
    /**
     * @see ClientPlayerRespawn#respawn(LocalPlayer, LocalPlayer)
     */
    Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createLoop();
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerJoin {
        /**
         * Called whenever a client player joins into a Level
         * 
         * @param player The player joining. Equal to {@link net.minecraft.client.Minecraft#player}
         */
        void join(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerQuit {
        /**
         * Called whenever a client leaves a level and it is cleared on the client side.
         * 
         * @param player The player leaving.
         */
        void quit(@Nullable LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerRespawn {
        /**
         * Called whenever the packet for client respawning is received by the client.
         * 
         * @param oldPlayer The player before the respawn happened.
         * @param newPlayer The player after the respawn happened.
         */
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
