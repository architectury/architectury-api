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
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
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
                PayloadTypeRegistry.playC2S().register(type, codec);
                ServerPlayNetworking.registerGlobalReceiver(type, (payload, fabricContext) -> {
                    var context = context(fabricContext.player(), fabricContext.player().server, false);
                    receiver.receive(payload, context);
                });
            }
            
            @Override
            @Environment(EnvType.CLIENT)
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                LOGGER.info("Registering S2C receiver with id {}", type.id());
                PayloadTypeRegistry.playS2C().register(type, codec);
                ClientPlayNetworking.registerGlobalReceiver(type, new ClientPlayPayloadHandler<>(receiver));
            }
            
            // Lambda methods aren't included in @EnvType, so this inelegant solution is used instead.
            @Environment(EnvType.CLIENT)
            class ClientPlayPayloadHandler<T extends CustomPacketPayload> implements ClientPlayNetworking.PlayPayloadHandler<T> {
                private final NetworkReceiver<T> receiver;
    
                ClientPlayPayloadHandler(NetworkReceiver<T> receiver) {
                    this.receiver = receiver;
                }
    
                @Override
                public void receive(T payload, ClientPlayNetworking.Context fabricContext) {
                    var context = context(fabricContext.player(), fabricContext.client(), true);
                    receiver.receive(payload, context);
                }
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toC2SPacket(T payload) {
                return ClientPlayNetworking.createC2SPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toS2CPacket(T payload) {
                return ServerPlayNetworking.createS2CPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2CType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
                PayloadTypeRegistry.playS2C().register(type, codec);
            }
        };
    }
    
    private static NetworkManager.PacketContext context(Player player, BlockableEventLoop<?> taskQueue, boolean client) {
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
    public static boolean canServerReceive(ResourceLocation id) {
        return ClientPlayNetworking.canSend(id);
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return ServerPlayNetworking.canSend(player, id);
    }
    
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        return SpawnEntityPacket.create(entity);
    }
}
