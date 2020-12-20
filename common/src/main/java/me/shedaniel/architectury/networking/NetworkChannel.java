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

package me.shedaniel.architectury.networking;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import me.shedaniel.architectury.networking.NetworkManager.PacketContext;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Forge {@code SimpleChannel} like network wrapper of {@link NetworkManager}.
 */
public final class NetworkChannel {
    private final ResourceLocation id;
    private final IntSet takenIds = new IntOpenHashSet();
    private final Table<NetworkManager.Side, Class<?>, Pair<ResourceLocation, BiConsumer<?, FriendlyByteBuf>>> encoders = HashBasedTable.create();
    
    private NetworkChannel(ResourceLocation id) {
        this.id = id;
    }
    
    public static NetworkChannel create(ResourceLocation id) {
        return new NetworkChannel(id);
    }
    
    public <T> void register(NetworkManager.Side side, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        register(Optional.ofNullable(side), type, encoder, decoder, messageConsumer);
    }
    
    public <T> void register(Optional<NetworkManager.Side> side, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        for (int i = 0; true; i++) {
            if (!takenIds.contains(i)) {
                register(side, i, type, encoder, decoder, messageConsumer);
                break;
            }
        }
    }
    
    public <T> void register(NetworkManager.Side side, int id, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        register(Optional.ofNullable(side), id, type, encoder, decoder, messageConsumer);
    }
    
    public <T> void register(Optional<NetworkManager.Side> side, int id, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<PacketContext>> messageConsumer) {
        takenIds.add(id);
        ResourceLocation messageId = new ResourceLocation(this.id.getNamespace(), this.id.getPath() + "_" + id);
        if (!side.isPresent() || side.get() == NetworkManager.s2c()) {
            if (Platform.getEnvironment() == Env.CLIENT) {
                NetworkManager.registerReceiver(NetworkManager.s2c(), messageId, (buf, context) -> {
                    messageConsumer.accept(decoder.apply(buf), () -> context);
                });
            }
            encoders.put(NetworkManager.s2c(), type, Pair.of(messageId, encoder));
        }
        if (!side.isPresent() || side.get() == NetworkManager.c2s()) {
            NetworkManager.registerReceiver(NetworkManager.c2s(), messageId, (buf, context) -> {
                messageConsumer.accept(decoder.apply(buf), () -> context);
            });
            encoders.put(NetworkManager.c2s(), type, Pair.of(messageId, encoder));
        }
    }
    
    private <T> Pair<ResourceLocation, FriendlyByteBuf> encode(NetworkManager.Side side, T message) {
        Pair<ResourceLocation, BiConsumer<?, FriendlyByteBuf>> pair = Objects.requireNonNull(encoders.get(side, message.getClass()));
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        ((BiConsumer<T, FriendlyByteBuf>) pair.getRight()).accept(message, buf);
        return Pair.of(pair.getLeft(), buf);
    }
    
    public <T> Packet<?> toPacket(NetworkManager.Side side, T message) {
        Pair<ResourceLocation, FriendlyByteBuf> encoded = encode(side, message);
        return NetworkManager.toPacket(side, encoded.getLeft(), encoded.getRight());
    }
    
    public <T> void sendToPlayer(ServerPlayer player, T message) {
        Pair<ResourceLocation, FriendlyByteBuf> encoded = encode(NetworkManager.s2c(), message);
        NetworkManager.sendToPlayer(player, encoded.getLeft(), encoded.getRight());
    }
    
    public <T> void sendToPlayers(Iterable<ServerPlayer> players, T message) {
        Packet<?> packet = toPacket(NetworkManager.s2c(), message);
        for (ServerPlayer player : players) {
            player.connection.send(packet);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public <T> void sendToServer(T message) {
        Minecraft.getInstance().getConnection().send(toPacket(NetworkManager.c2s(), message));
    }
    
    @Environment(EnvType.CLIENT)
    public <T> boolean canServerReceive(Class<T> type) {
        return NetworkManager.canServerReceive(encoders.get(NetworkManager.c2s(), type).getLeft());
    }
    
    public <T> boolean canPlayerReceive(ServerPlayer player, Class<T> type) {
        return NetworkManager.canPlayerReceive(player, encoders.get(NetworkManager.s2c(), type).getLeft());
    }
}
