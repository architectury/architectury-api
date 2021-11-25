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

package me.shedaniel.architectury.networking.fabric;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.NetworkManager.NetworkReceiver;
import me.shedaniel.architectury.networking.transformers.PacketSink;
import me.shedaniel.architectury.networking.transformers.PacketTransformer;
import me.shedaniel.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkManagerImpl {
    private static final Map<ResourceLocation, NetworkReceiver> C2S_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, NetworkReceiver> S2C_RECEIVER = new HashMap<>();
    private static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = new HashMap<>();
    private static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = new HashMap<>();
    
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, packetTransformers, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, packetTransformers, receiver);
        }
    }
    
    private static void registerC2SReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        C2S_RECEIVER.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        ServerSidePacketRegistry.INSTANCE.register(id, (packetContext, buf) -> {
            NetworkManager.PacketContext context = to(packetContext);
            transformer.inbound(NetworkManager.Side.C2S, id, buf, context, (side, id1, buf1) -> {
                NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1);
                if (networkReceiver == null) {
                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                }
                networkReceiver.receive(buf1, context);
            });
        });
        C2S_TRANSFORMERS.put(id, transformer);
    }
    
    @Environment(EnvType.CLIENT)
    private static void registerS2CReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        S2C_RECEIVER.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        ClientSidePacketRegistry.INSTANCE.register(id, (packetContext, buf) -> {
            NetworkManager.PacketContext context = to(packetContext);
            transformer.inbound(NetworkManager.Side.S2C, id, buf, context, (side, id1, buf1) -> {
                NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S_RECEIVER.get(id1) : S2C_RECEIVER.get(id1);
                if (networkReceiver == null) {
                    throw new IllegalArgumentException("Network Receiver not found! " + id1);
                }
                networkReceiver.receive(buf1, context);
            });
        });
        S2C_TRANSFORMERS.put(id, transformer);
    }
    
    private static NetworkManager.PacketContext to(PacketContext context) {
        return new NetworkManager.PacketContext() {
            @Override
            public Player getPlayer() {
                return context.getPlayer();
            }
            
            @Override
            public void queue(Runnable runnable) {
                context.getTaskQueue().execute(runnable);
            }
            
            @Override
            public Env getEnvironment() {
                return Env.fromPlatform(context.getPacketEnvironment());
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
    
    public static boolean canServerReceive(ResourceLocation id) {
        return ClientSidePacketRegistry.INSTANCE.canServerReceive(id);
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return ServerSidePacketRegistry.INSTANCE.canPlayerReceive(player, id);
    }
    
    public static Packet<?> createAddEntityPacket(Entity entity) {
        return SpawnEntityPacket.create(entity);
    }
    
    @Environment(EnvType.CLIENT)
    private static Packet<?> toC2SPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return ClientSidePacketRegistry.INSTANCE.toPacket(id, buf);
    }
    
    private static Packet<?> toS2CPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return ServerSidePacketRegistry.INSTANCE.toPacket(id, buf);
    }
}
