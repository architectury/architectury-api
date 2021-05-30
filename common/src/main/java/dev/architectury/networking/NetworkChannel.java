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

import com.google.common.collect.Maps;
import dev.architectury.networking.NetworkManager.PacketContext;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Forge {@code SimpleChannel} like network wrapper of {@link NetworkManager}.
 */
public final class NetworkChannel {
    private final ResourceLocation id;
    private final Map<Class<?>, MessageInfo<?>> encoders = Maps.newHashMap();
    
    private NetworkChannel(ResourceLocation id) {
        this.id = id;
    }
    
    public static NetworkChannel create(ResourceLocation id) {
        return new NetworkChannel(id);
    }
    
    public <T> void register(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        // TODO: this is pretty wasteful; add a way to specify custom or numeric ids
        var s = UUID.nameUUIDFromBytes(type.getName().getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
        var info = new MessageInfo<T>(new ResourceLocation(id + "/" + s), encoder, decoder, messageConsumer);
        encoders.put(type, info);
        NetworkManager.NetworkReceiver receiver = (buf, context) -> {
            info.messageConsumer.accept(info.decoder.apply(buf), () -> context);
        };
        NetworkManager.registerReceiver(NetworkManager.c2s(), info.packetId, receiver);
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), info.packetId, receiver);
        }
    }
    
    public static long hashCodeString(String str) {
        long h = 0;
        var length = str.length();
        for (var i = 0; i < length; i++) {
            h = 31 * h + str.charAt(i);
        }
        return h;
    }
    
    public <T> Packet<?> toPacket(NetworkManager.Side side, T message) {
        var messageInfo = (MessageInfo<T>) Objects.requireNonNull(encoders.get(message.getClass()), "Unknown message type! " + message);
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        messageInfo.encoder.accept(message, buf);
        return NetworkManager.toPacket(side, messageInfo.packetId, buf);
    }
    
    public <T> void sendToPlayer(ServerPlayer player, T message) {
        Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(toPacket(NetworkManager.s2c(), message));
    }
    
    public <T> void sendToPlayers(Iterable<ServerPlayer> players, T message) {
        var packet = toPacket(NetworkManager.s2c(), message);
        for (var player : players) {
            Objects.requireNonNull(player, "Unable to send packet to a 'null' player!").connection.send(packet);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public <T> void sendToServer(T message) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(toPacket(NetworkManager.c2s(), message));
        } else {
            throw new IllegalStateException("Unable to send packet to the server while not in game!");
        }
    }
    
    @Environment(EnvType.CLIENT)
    public <T> boolean canServerReceive(Class<T> type) {
        return NetworkManager.canServerReceive(encoders.get(type).packetId);
    }
    
    public <T> boolean canPlayerReceive(ServerPlayer player, Class<T> type) {
        return NetworkManager.canPlayerReceive(player, encoders.get(type).packetId);
    }
    
    private record MessageInfo<T>(
            ResourceLocation packetId,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<PacketContext>> messageConsumer
    ) {
    }
}
