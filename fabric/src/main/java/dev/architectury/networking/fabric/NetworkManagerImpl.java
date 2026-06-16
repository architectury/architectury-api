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

package dev.architectury.networking.fabric;

import com.mojang.logging.LogUtils;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.networking.client.fabric.ClientNetworkManagerImpl;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

public class NetworkManagerImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static NetworkAggregator.Adaptor getAdaptor() {
        return new NetworkAggregator.Adaptor() {
            @Override
            public <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                LOGGER.info("Registering C2S receiver with id {}", type.id());
                PayloadTypeRegistry.serverboundPlay().register(type, codec);
                ServerPlayNetworking.registerGlobalReceiver(type, (payload, fabricContext) -> {
                    var context = context(fabricContext.player(), fabricContext.server(), false);
                    receiver.receive(payload, context);
                });
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                PayloadTypeRegistry.clientboundPlay().register(type, codec);
                if (Platform.getEnvironment() == Env.CLIENT)
                    ClientNetworkManagerImpl.registerS2C(type, codec, receiver);
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toC2SPacket(T payload) {
                return ClientPlayNetworking.createServerboundPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toS2CPacket(T payload) {
                return ServerPlayNetworking.createClientboundPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2CType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
                PayloadTypeRegistry.clientboundPlay().register(type, codec);
            }
        };
    }
    
    public static NetworkManager.PacketContext context(Player player, BlockableEventLoop<?> taskQueue, boolean client) {
        return new NetworkManager.PacketContext() {
            @Override
            public Player getPlayer() {
                return player;
            }
            
            @Override
            public void queue(Runnable runnable) {
                taskQueue.execute(runnable);
            }
            
            @Override
            public Env getEnvironment() {
                return client ? Env.CLIENT : Env.SERVER;
            }
            
            @Override
            public RegistryAccess registryAccess() {
                return player.registryAccess();
            }
        };
    }
    
    @Environment(EnvType.CLIENT)
    public static boolean canServerReceive(Identifier id) {
        return ClientPlayNetworking.canSend(id);
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, Identifier id) {
        return ServerPlayNetworking.canSend(player, id);
    }
    
    public static <T extends CustomPacketPayload> void sendToServer(T payload) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;
        NetworkManager.collectPackets(PacketSink.client(), NetworkManager.c2s(), payload, connection.registryAccess());
    }
    
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity, ServerEntity serverEntity) {
        return SpawnEntityPacket.create(entity, serverEntity);
    }
}
