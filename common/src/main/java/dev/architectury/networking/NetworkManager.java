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

package dev.architectury.networking;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public final class NetworkManager {
    @ExpectPlatform
    public static void registerReceiver(Side side, ResourceLocation id, NetworkReceiver receiver) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static Packet<?> toPacket(Side side, ResourceLocation id, FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(toPacket(serverToClient(), id, buf));
    }
    
    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation id, FriendlyByteBuf buf) {
        var packet = toPacket(serverToClient(), id, buf);
        for (var player : players) {
            Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(toPacket(clientToServer(), id, buf));
        } else {
            throw new IllegalStateException("Unable to send packet to the server while not in game!");
        }
    }
    
    @Environment(EnvType.CLIENT)
    @ExpectPlatform
    public static boolean canServerReceive(ResourceLocation id) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        throw new AssertionError();
    }
    
    /**
     * Easy to use utility method to create an entity spawn packet.
     * This packet is needed everytime any mod adds a non-living entity.
     * The entity should override {@link Entity#getAddEntityPacket()} to point to this method!
     *
     * @param entity The entity which should be spawned.
     * @return The ready to use packet to spawn the entity on the client.
     * @see Entity#getAddEntityPacket()
     */
    @ExpectPlatform
    public static Packet<?> createAddEntityPacket(Entity entity) {
        throw new AssertionError();
    }
    
    @FunctionalInterface
    public interface NetworkReceiver {
        void receive(FriendlyByteBuf buf, PacketContext context);
    }
    
    public interface PacketContext {
        Player getPlayer();
        
        void queue(Runnable runnable);
        
        Env getEnvironment();
        
        default EnvType getEnv() {
            return getEnvironment().toPlatform();
        }
    }
    
    public static Side s2c() {
        return Side.S2C;
    }
    
    public static Side c2s() {
        return Side.C2S;
    }
    
    public static Side serverToClient() {
        return Side.S2C;
    }
    
    public static Side clientToServer() {
        return Side.C2S;
    }
    
    public enum Side {
        S2C,
        C2S
    }
}
