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
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.SpawnEntityPacket;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.platform.hooks.forge.EventBusesHooksImpl;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPlayPayloadHandler;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

@Mod.EventBusSubscriber(modid = ArchitecturyConstants.MOD_ID)
public class NetworkManagerImpl {
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
    
    public static Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buffer) {
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeResourceLocation(id);
        packetBuffer.writeBytes(buffer);
        return side == NetworkManager.Side.C2S ? new ServerboundCustomPayloadPacket(new BufCustomPacketPayload(packetBuffer)) : new ClientboundCustomPayloadPacket(new BufCustomPacketPayload(packetBuffer));
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
    
    private static final Logger LOGGER = LogUtils.getLogger();
    static final ResourceLocation CHANNEL_ID = new ResourceLocation("architectury:network");
    static final ResourceLocation SYNC_IDS = new ResourceLocation("architectury:sync_ids");
    static final Map<ResourceLocation, NetworkReceiver> S2C = Maps.newHashMap();
    static final Map<ResourceLocation, NetworkReceiver> C2S = Maps.newHashMap();
    static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = Maps.newHashMap();
    static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = Maps.newHashMap();
    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();
    private static final Multimap<Player, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);
    
    static {
        EventBusesHooksImpl.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.addListener(NetworkManagerImpl::registerPackets);
        });
    }
    
    static IPlayPayloadHandler<BufCustomPacketPayload> createPacketHandler(NetworkManager.Side direction, Map<ResourceLocation, PacketTransformer> map) {
        return (arg, context) -> {
            
            NetworkManager.Side side = side(context.flow());
            if (side != direction) return;
            ResourceLocation type = arg.buf().readResourceLocation();
            PacketTransformer transformer = map.get(type);
            
            
            if (transformer != null) {
                NetworkManager.PacketContext packetContext = new NetworkManager.PacketContext() {
                    @Override
                    public Player getPlayer() {
                        return getEnvironment() == Env.CLIENT ? getClientPlayer() : context.player().orElse(null);
                    }
                    
                    @Override
                    public void queue(Runnable runnable) {
                        context.workHandler().submitAsync(runnable);
                    }
                    
                    @Override
                    public Env getEnvironment() {
                        return context.flow().getReceptionSide() == LogicalSide.CLIENT ? Env.CLIENT : Env.SERVER;
                    }
                    
                    @SuppressWarnings("removal")
                    private Player getClientPlayer() {
                        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::getClientPlayer);
                    }
                };
                
                transformer.inbound(side, type, arg.buf(), packetContext, (side1, id1, buf1) -> {
                    NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S.get(id1) : S2C.get(id1);
                    if (networkReceiver == null) {
                        throw new IllegalArgumentException("Network Receiver not found! " + id1);
                    }
                    networkReceiver.receive(buf1, packetContext);
                });
            } else {
                LOGGER.error("Unknown message ID: " + type);
            }
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerS2CReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        LOGGER.info("Registering S2C receiver with id {}", id);
        S2C.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        S2C_TRANSFORMERS.put(id, transformer);
    }
    
    public static void registerC2SReceiver(ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkReceiver receiver) {
        LOGGER.info("Registering C2S receiver with id {}", id);
        C2S.put(id, receiver);
        PacketTransformer transformer = PacketTransformer.concat(packetTransformers);
        C2S_TRANSFORMERS.put(id, transformer);
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
    
    static FriendlyByteBuf sendSyncPacket(Map<ResourceLocation, NetworkReceiver> map) {
        List<ResourceLocation> availableIds = Lists.newArrayList(map.keySet());
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeInt(availableIds.size());
        for (ResourceLocation availableId : availableIds) {
            packetBuffer.writeResourceLocation(availableId);
        }
        return packetBuffer;
    }
    
    @SubscribeEvent
    public static void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkManager.sendToPlayer((ServerPlayer) event.getEntity(), SYNC_IDS, sendSyncPacket(C2S));
    }
    
    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        clientReceivables.removeAll(event.getEntity());
    }
    
    /**
     * Needs to be on the mod bus for some reason...
     */
    public static void registerPackets(RegisterPayloadHandlerEvent event) {
        //noinspection removal
        @Nullable
        IPlayPayloadHandler<BufCustomPacketPayload> s2c = DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        
        IPayloadRegistrar registrar = event.registrar("architectury")/*.versioned(Platform.getMod("architectury-api").getVersion())*/.optional();
        registrar.play(CHANNEL_ID, BufCustomPacketPayload::new, builder -> {
            builder.server(createPacketHandler(NetworkManager.Side.C2S, C2S_TRANSFORMERS)).client(s2c);
        });
        
        
        registerC2SReceiver(SYNC_IDS, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = (Set<ResourceLocation>) clientReceivables.get(context.getPlayer());
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
        });
    }
    
    static NetworkManager.Side side(PacketFlow flow) {
        return flow.isClientbound() ? NetworkManager.Side.S2C : flow.isServerbound() ? NetworkManager.Side.C2S : null;
    }
}
