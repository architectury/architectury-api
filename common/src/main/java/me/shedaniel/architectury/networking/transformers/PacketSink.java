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

package me.shedaniel.architectury.networking.transformers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

@FunctionalInterface
public interface PacketSink {
    static PacketSink ofPlayer(ServerPlayer player) {
        return packet -> Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
    }
    
    static PacketSink ofPlayers(Iterable<? extends ServerPlayer> players) {
        return packet -> {
            for (ServerPlayer player : players) {
                Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
            }
        };
    }
    
    @Environment(EnvType.CLIENT)
    static PacketSink client() {
        return packet -> {
            if (Minecraft.getInstance().getConnection() != null) {
                Minecraft.getInstance().getConnection().send(packet);
            } else {
                throw new IllegalStateException("Unable to send packet to the server while not in game!");
            }
        };
    }
    
    void accept(Packet<?> packet);
}
