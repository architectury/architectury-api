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

package dev.architectury.networking.simple;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * The base class for server -&gt; client messages managed by a {@link SimpleNetworkManager}.
 */
public abstract class BaseS2CMessage extends Message {
    private void sendTo(ServerPlayer player, Packet<?> packet) {
        if (player == null) {
            throw new NullPointerException("Unable to send packet '" + getType().getId() + "' to a 'null' player!");
        }

        player.connection.send(packet);
    }

    /**
     * Sends this message to a player.
     *
     * @param player the player
     */
    public final void sendTo(ServerPlayer player) {
        sendTo(player, toPacket());
    }

    /**
     * Sends this message to multiple players.
     *
     * @param players the players
     */
    public final void sendTo(Iterable<ServerPlayer> players) {
        Packet<?> packet = toPacket();

        for (ServerPlayer player : players) {
            sendTo(player, packet);
        }
    }

    /**
     * Sends this message to all players in the server.
     *
     * @param server the server
     */
    public final void sendToAll(MinecraftServer server) {
        sendTo(server.getPlayerList().getPlayers());
    }

    /**
     * Sends this message to all players in a level.
     *
     * @param level the level
     */
    public final void sendToLevel(ServerLevel level) {
        sendTo(level.players());
    }

    /**
     * Sends this message to all players listening to a chunk.
     *
     * @param chunk the listened chunk
     */
    public final void sendToChunkListeners(LevelChunk chunk) {
        Packet<?> packet = toPacket();
        ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> sendTo(e, packet));
    }
}