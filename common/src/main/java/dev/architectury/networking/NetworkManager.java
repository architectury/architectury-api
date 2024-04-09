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

package dev.architectury.networking;

import dev.architectury.impl.NetworkAggregator;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.networking.transformers.PacketCollector;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.networking.transformers.SinglePacketCollector;
import dev.architectury.utils.Env;
import dev.architectury.utils.GameInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class NetworkManager {
    /**
     * For S2C types, {@link #registerReceiver} should be called on the client side,
     * while {@link #registerS2CPayloadType} should be called on the server side.
     */
    public static void registerS2CPayloadType(ResourceLocation id) {
        NetworkAggregator.registerS2CType(id, List.of());
    }
    
    /**
     * For S2C types, {@link #registerReceiver} should be called on the client side,
     * while {@link #registerS2CPayloadType} should be called on the server side.
     */
    public static <T extends CustomPacketPayload> void registerS2CPayloadType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        NetworkAggregator.registerS2CType(type, codec, List.of());
    }
    
    /**
     * For S2C types, {@link #registerReceiver} should be called on the client side,
     * while {@link #registerS2CPayloadType} should be called on the server side.
     */
    public static void registerS2CPayloadType(ResourceLocation id, List<PacketTransformer> packetTransformers) {
        NetworkAggregator.registerS2CType(id, packetTransformers);
    }
    
    /**
     * For S2C types, {@link #registerReceiver} should be called on the client side,
     * while {@link #registerS2CPayloadType} should be called on the server side.
     */
    public static <T extends CustomPacketPayload> void registerS2CPayloadType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers) {
        NetworkAggregator.registerS2CType(type, codec, packetTransformers);
    }
    
    public static void registerReceiver(Side side, ResourceLocation id, NetworkReceiver<RegistryFriendlyByteBuf> receiver) {
        registerReceiver(side, id, Collections.emptyList(), receiver);
    }
    
    @ApiStatus.Experimental
    public static void registerReceiver(Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver<RegistryFriendlyByteBuf> receiver) {
        NetworkAggregator.registerReceiver(side, id, packetTransformers, receiver);
    }
    
    public static <T extends CustomPacketPayload> void registerReceiver(Side side, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
        registerReceiver(side, id, codec, Collections.emptyList(), receiver);
    }
    
    @ApiStatus.Experimental
    public static <T extends CustomPacketPayload> void registerReceiver(Side side, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers, NetworkReceiver<T> receiver) {
        NetworkAggregator.registerReceiver(side, id, codec, packetTransformers, receiver);
    }
    
    @Deprecated
    public static Packet<?> toPacket(Side side, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        SinglePacketCollector sink = new SinglePacketCollector(null);
        collectPackets(sink, side, id, buf);
        return sink.getPacket();
    }
    
    @Deprecated
    public static List<Packet<?>> toPackets(Side side, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        PacketCollector sink = new PacketCollector(null);
        collectPackets(sink, side, id, buf);
        return sink.collect();
    }
    
    public static void collectPackets(PacketSink sink, Side side, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        NetworkAggregator.collectPackets(sink, side, id, buf);
    }
    
    public static <T extends CustomPacketPayload> void collectPackets(PacketSink sink, Side side, T payload, RegistryAccess access) {
        NetworkAggregator.collectPackets(sink, side, payload, access);
    }
    
    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayer(player), serverToClient(), id, buf);
    }
    
    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation id, RegistryFriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayers(players), serverToClient(), id, buf);
    }
    
    @Environment(EnvType.CLIENT)
    public static void sendToServer(ResourceLocation id, RegistryFriendlyByteBuf buf) {
        collectPackets(PacketSink.client(), clientToServer(), id, buf);
    }
    
    public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
        collectPackets(PacketSink.ofPlayer(player), serverToClient(), payload, player.registryAccess());
    }
    
    public static <T extends CustomPacketPayload> void sendToPlayers(Iterable<ServerPlayer> players, T payload) {
        Iterator<ServerPlayer> iterator = players.iterator();
        if (!iterator.hasNext()) return;
        collectPackets(PacketSink.ofPlayers(players), serverToClient(), payload, iterator.next().registryAccess());
    }
    
    @Environment(EnvType.CLIENT)
    public static <T extends CustomPacketPayload> void sendToServer(T payload) {
        ClientPacketListener connection = GameInstance.getClient().getConnection();
        if (connection == null) return;
        collectPackets(PacketSink.client(), clientToServer(), payload, connection.registryAccess());
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
     * <p>
     * Additionally, entities may implement {@link dev.architectury.extensions.network.EntitySpawnExtension}
     * to load / save additional data to the client.
     *
     * @param entity The entity which should be spawned.
     * @return The ready to use packet to spawn the entity on the client.
     * @see Entity#getAddEntityPacket()
     */
    @ExpectPlatform
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static NetworkAggregator.Adaptor getAdaptor() {
        throw new AssertionError();
    }
    
    @FunctionalInterface
    public interface NetworkReceiver<T> {
        void receive(T value, PacketContext context);
    }
    
    public interface PacketContext {
        Player getPlayer();
        
        void queue(Runnable runnable);
        
        Env getEnvironment();
        
        RegistryAccess registryAccess();
        
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
