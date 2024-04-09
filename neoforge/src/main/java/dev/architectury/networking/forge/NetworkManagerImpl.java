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


import com.google.common.collect.*;
import com.mojang.logging.LogUtils;
import dev.architectury.impl.NetworkAggregator;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.ISynchronizedWorkHandler;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static dev.architectury.networking.forge.ClientNetworkingManager.getClientPlayer;
import static dev.architectury.networking.forge.ClientNetworkingManager.getClientRegistryAccess;

@Mod.EventBusSubscriber(modid = ArchitecturyConstants.MOD_ID)
public class NetworkManagerImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    static final ResourceLocation SYNC_IDS_S2C = new ResourceLocation("architectury:sync_ids_s2c");
    static final ResourceLocation SYNC_IDS_C2S = new ResourceLocation("architectury:sync_ids_c2s");
    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();
    private static final Multimap<Player, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);
    
    static {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SYNC_IDS_C2S, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = (Set<ResourceLocation>) clientReceivables.get(context.getPlayer());
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
        });
        EnvExecutor.runInEnv(Env.SERVER, () -> () -> NetworkManager.registerS2CPayloadType(SYNC_IDS_S2C));
    }
    
    public static NetworkAggregator.Adaptor getAdaptor() {
        return new NetworkAggregator.Adaptor() {
            @Override
            public <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
                    bus.<RegisterPayloadHandlerEvent>addListener(event -> {
                        event.registrar(type.id().getNamespace()).optional().play(type, codec, (arg, context) -> {
                            receiver.receive(arg, context(context.player().orElse(null), context.workHandler(), false));
                        });
                    });
                });
            }
            
            @Override
            public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkReceiver<T> receiver) {
                EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
                    bus.<RegisterPayloadHandlerEvent>addListener(event -> {
                        event.registrar(type.id().getNamespace()).optional().play(type, codec, (arg, context) -> {
                            receiver.receive(arg, context(context.player().orElse(null), context.workHandler(), true));
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
            
            public NetworkManager.PacketContext context(Player player, ISynchronizedWorkHandler taskQueue, boolean client) {
                return new NetworkManager.PacketContext() {
                    @Override
                    public Player getPlayer() {
                        return getEnvironment() == Env.CLIENT ? getClientPlayer() : player;
                    }
                    
                    @Override
                    public void queue(Runnable runnable) {
                        taskQueue.submitAsync(runnable);
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
    
    public static boolean canServerReceive(ResourceLocation id) {
        return serverReceivables.contains(id);
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return clientReceivables.get(player).contains(id);
    }
    
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        return SpawnEntityPacket.create(entity);
    }
    
    static RegistryFriendlyByteBuf sendSyncPacket(Map<ResourceLocation, ?> map, RegistryAccess access) {
        List<ResourceLocation> availableIds = Lists.newArrayList(map.keySet());
        RegistryFriendlyByteBuf packetBuffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), access);
        packetBuffer.writeInt(availableIds.size());
        for (ResourceLocation availableId : availableIds) {
            packetBuffer.writeResourceLocation(availableId);
        }
        return packetBuffer;
    }
    
    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.sendToPlayer((ServerPlayer) event.getEntity(), SYNC_IDS_S2C, sendSyncPacket(NetworkAggregator.C2S_RECEIVER, event.getEntity().registryAccess()));
    }
    
    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        clientReceivables.removeAll(event.getEntity());
    }
    
    static NetworkManager.Side side(PacketFlow flow) {
        return flow.isClientbound() ? NetworkManager.Side.S2C : flow.isServerbound() ? NetworkManager.Side.C2S : null;
    }
}
