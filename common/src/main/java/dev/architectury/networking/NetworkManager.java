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
import net.fabricmc.api.EnvType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class NetworkManager {
    /**
     * Registers an S2C payload type so the server can send it.
     * <p>
     * For the client to receive and handle packets of this type, also call
     * {@link #registerReceiver(Side, CustomPacketPayload.Type, StreamCodec, NetworkReceiver)}
     * from client initialisation, or use {@link #registerS2C} from common code instead.
     */
    public static <T extends CustomPacketPayload> void registerS2CPayloadType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        NetworkAggregator.registerS2CType(type, codec, List.of());
    }

    /**
     * Registers an S2C payload type with packet transformers so the server can send it.
     * <p>
     * For the client to receive and handle packets of this type, also call
     * {@link #registerReceiver(Side, CustomPacketPayload.Type, StreamCodec, List, NetworkReceiver)}
     * from client initialisation.
     */
    public static <T extends CustomPacketPayload> void registerS2CPayloadType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers) {
        NetworkAggregator.registerS2CType(type, codec, packetTransformers);
    }

    public static <T extends CustomPacketPayload> void registerReceiver(Side side, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
        registerReceiver(side, id, codec, Collections.emptyList(), receiver);
    }

    public static <T extends CustomPacketPayload> void registerReceiver(Side side, CustomPacketPayload.Type<T> id, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, List<PacketTransformer> packetTransformers, NetworkReceiver<T> receiver) {
        NetworkAggregator.registerReceiver(side, id, codec, packetTransformers, receiver);
    }

    /**
     * Registers an S2C packet type and its client-side receiver in a single call,
     * safe to invoke from common initialisation code on any environment.
     * <p>
     * This is the recommended way to register S2C packets from common code.
     */
    public static <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            NetworkReceiver<T> receiver) {
        registerReceiver(s2c(), type, codec, receiver);
    }

    /**
     * Registers a C2S packet type and its server-side receiver.
     * Equivalent to {@link #registerReceiver(Side, CustomPacketPayload.Type, StreamCodec, NetworkReceiver)}
     * with {@link Side#C2S}.
     */
    public static <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            NetworkReceiver<T> receiver) {
        registerReceiver(c2s(), type, codec, receiver);
    }

    public static <T extends CustomPacketPayload> Packet<?> toPacket(Side side, T payload, RegistryAccess access) {
        SinglePacketCollector sink = new SinglePacketCollector(null);
        collectPackets(sink, side, payload, access);
        return sink.getPacket();
    }

    public static <T extends CustomPacketPayload> List<Packet<?>> toPackets(Side side, T payload, RegistryAccess access) {
        PacketCollector sink = new PacketCollector(null);
        collectPackets(sink, side, payload, access);
        return sink.collect();
    }

    public static <T extends CustomPacketPayload> void collectPackets(PacketSink sink, Side side, T payload, RegistryAccess access) {
        NetworkAggregator.collectPackets(sink, side, payload, access);
    }

    public static <T extends CustomPacketPayload> void sendToPlayer(ServerPlayer player, T payload) {
        collectPackets(PacketSink.ofPlayer(player), s2c(), payload, player.registryAccess());
    }

    public static <T extends CustomPacketPayload> void sendToPlayers(Iterable<ServerPlayer> players, T payload) {
        Iterator<ServerPlayer> iterator = players.iterator();
        if (!iterator.hasNext()) return;
        collectPackets(PacketSink.ofPlayers(players), s2c(), payload, iterator.next().registryAccess());
    }

    @ExpectPlatform
    public static <T extends CustomPacketPayload> void sendToServer(T payload) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean canServerReceive(Identifier id) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean canPlayerReceive(ServerPlayer player, Identifier id) {
        throw new AssertionError();
    }

    public static boolean canServerReceive(CustomPacketPayload.Type<?> type) {
        return canServerReceive(type.id());
    }

    public static boolean canPlayerReceive(ServerPlayer player, CustomPacketPayload.Type<?> type) {
        return canPlayerReceive(player, type.id());
    }

    /**
     * Easy to use utility method to create an entity spawn packet.
     * This packet is needed everytime any mod adds a non-living entity.
     * The entity should override {@link Entity#getAddEntityPacket(ServerEntity)} to point to this method!
     * <p>
     * Additionally, entities may implement {@link dev.architectury.extensions.network.EntitySpawnExtension}
     * to load / save additional data to the client.
     *
     * @param entity The entity which should be spawned.
     * @return The ready to use packet to spawn the entity on the client.
     * @see Entity#getAddEntityPacket(ServerEntity)
     */
    @ExpectPlatform
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity, ServerEntity serverEntity) {
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

    public enum Side {
        S2C,
        C2S
    }
}
