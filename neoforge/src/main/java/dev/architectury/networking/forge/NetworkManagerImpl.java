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

package dev.architectury.networking.forge;


import com.mojang.logging.LogUtils;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public class NetworkManagerImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static NetworkAggregator.Adaptor getAdaptor() {
        return new NetworkAggregator.Adaptor() {
            @Override
            public <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
                    bus.<RegisterPayloadHandlersEvent>addListener(event -> {
                        event.registrar(type.id().getNamespace()).optional().playToServer(type, codec, (arg, context) -> {
                            receiver.receive(arg, context(context.player(), context, false));
                        });
                    });
                });
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
                    bus.<RegisterPayloadHandlersEvent>addListener(event -> {
                        event.registrar(type.id().getNamespace()).optional().playToClient(type, codec, (arg, context) -> {
                            receiver.receive(arg, context(context.player(), context, true));
                        });
                    });
                });
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toC2SPacket(T payload) {
                return new ServerboundCustomPayloadPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> Packet<?> toS2CPacket(T payload) {
                return new ClientboundCustomPayloadPacket(payload);
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2CType(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
                registerS2C(type, codec, (payload, context) -> {
                });
            }
            
            public NetworkManager.PacketContext context(Player player, IPayloadContext taskQueue, boolean client) {
                return new NetworkManager.PacketContext() {
                    @Override
                    public Player getPlayer() {
                        return getEnvironment() == Env.CLIENT ? getClientPlayer() : player;
                    }
                    
                    @Override
                    public void queue(Runnable runnable) {
                        taskQueue.enqueueWork(runnable);
                    }
                    
                    @Override
                    public Env getEnvironment() {
                        return client ? Env.CLIENT : Env.SERVER;
                    }
                    
                    @Override
                    public RegistryAccess registryAccess() {
                        return getEnvironment() == Env.CLIENT ? getClientRegistryAccess() : player.registryAccess();
                    }
                };
            }
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    public static boolean canServerReceive(ResourceLocation id) {
        if (Minecraft.getInstance().getConnection() != null) {
            return Minecraft.getInstance().getConnection().hasChannel(id);
        } else {
            return false;
        }
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return player.connection.hasChannel(id);
    }
    
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        return SpawnEntityPacket.create(entity);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static RegistryAccess getClientRegistryAccess() {
        if (Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.registryAccess();
        } else if (Minecraft.getInstance().getConnection() != null) {
            return Minecraft.getInstance().getConnection().registryAccess();
        } else if (Minecraft.getInstance().gameMode != null) {
            // Sometimes the packet is sent way too fast and is between the connection and the level, better safe than sorry
            return Minecraft.getInstance().gameMode.connection.registryAccess();
        }
        
        // Fail-safe
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}
