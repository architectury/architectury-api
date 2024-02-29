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
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.utils.Env;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NetworkManagerImpl {
    private static final Map<ResourceLocation, CustomPacketPayload.Type<BufCustomPacketPayload>> C2S_TYPE = new HashMap<>();
    private static final Map<ResourceLocation, CustomPacketPayload.Type<BufCustomPacketPayload>> S2C_TYPE = new HashMap<>();
    private static final Map<ResourceLocation, NetworkReceiver> C2S_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, NetworkReceiver> S2C_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = new HashMap<>();
    private static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = new HashMap<>();
    
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        Objects.requireNonNull(id, "Cannot register receiver with a null ID!");
        packetTransformers = Objects.requireNonNullElse(packetTransformers, List.of());
        Objects.requireNonNull(receiver, "Cannot register a null receiver!");
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, packetTransformers, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, packetTransformers, receiver);
        }
    }
    
    private static void registerC2SReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        LOGGER.info("Registering C2S receiver with id {}", id);
        C2S_RECEIVER.put(id, receiver);
        CustomPacketPayload.Type<BufCustomPacketPayload> type = new CustomPacketPayload.Type<>(id);
        C2S_TYPE.put(id, type);
        PayloadTypeRegistry.playC2S().register(type, BufCustomPacketPayload.streamCodec(type));
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, fabricContext) -> {
            var context = context(fabricContext.player(), fabricContext.player().server, false);
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.payload()));
            transformer.inbound(NetworkManager.Side.C2S, id, buf, context, (side, id1, buf1) -> {
                NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1);
                if (networkReceiver == null) {
                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                }
                networkReceiver.receive(buf1, context);
            });
            buf.release();
        });
        C2S_TRANSFORMERS.put(id, transformer);
    }
    
    @SuppressWarnings("Convert2Lambda")
    @Environment(EnvType.CLIENT)
    private static void registerS2CReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        LOGGER.info("Registering S2C receiver with id {}", id);
        S2C_RECEIVER.put(id, receiver);
        CustomPacketPayload.Type<BufCustomPacketPayload> type = new CustomPacketPayload.Type<>(id);
        S2C_TYPE.put(id, type);
        PayloadTypeRegistry.playS2C().register(type, BufCustomPacketPayload.streamCodec(type));
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        ClientPlayNetworking.registerGlobalReceiver(type, new ClientPlayNetworking.PlayPayloadHandler<>() {
            @Override
            public void receive(BufCustomPacketPayload payload, ClientPlayNetworking.Context fabricContext) {
                var context = context(fabricContext.player(), fabricContext.client(), true);
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.payload()));
                transformer.inbound(NetworkManager.Side.S2C, id, buf, context, (side, id1, buf1) -> {
                    NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1);
                    if (networkReceiver == null) {
                        throw new IllegalArgumentException("Network Receiver not found! " + id1);
                    }
                    networkReceiver.receive(buf1, context);
                });
                buf.release();
            }
        });
        S2C_TRANSFORMERS.put(id, transformer);
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
        };
    }
    
    public static void collectPackets(PacketSink sink, NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        PacketTransformer transformer = side == NetworkManager.Side.C2S ? C2S_TRANSFORMERS.get(id) : S2C_TRANSFORMERS.get(id);
        if (transformer != null) {
            transformer.outbound(side, id, buf, (side1, id1, buf1) -> {
                sink.accept(toPacket(side1, id1, buf1));
            });
        } else {
            sink.accept(toPacket(side, id, buf));
        }
    }
    
    public static Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        if (side == NetworkManager.Side.C2S) {
            return toC2SPacket(id, buf);
        } else if (side == NetworkManager.Side.S2C) {
            return toS2CPacket(id, buf);
        }
        
        throw new IllegalArgumentException("Invalid side: " + side);
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
    
    @Environment(EnvType.CLIENT)
    private static Packet<?> toC2SPacket(ResourceLocation id, FriendlyByteBuf buf) {
        CustomPacketPayload.Type<BufCustomPacketPayload> type = C2S_TYPE.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown packet id: " + id);
        }
        return ClientPlayNetworking.createC2SPacket(new BufCustomPacketPayload(type, ByteBufUtil.getBytes(buf)));
    }
    
    private static Packet<?> toS2CPacket(ResourceLocation id, FriendlyByteBuf buf) {
        CustomPacketPayload.Type<BufCustomPacketPayload> type = S2C_TYPE.get(id);
        if (type == null) {
            throw new IllegalArgumentException("Unknown packet id: " + id);
        }
        return ServerPlayNetworking.createS2CPacket(new BufCustomPacketPayload(type, ByteBufUtil.getBytes(buf)));
    }
}
