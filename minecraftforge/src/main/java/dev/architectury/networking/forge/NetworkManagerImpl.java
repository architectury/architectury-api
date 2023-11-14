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
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.*;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = "architectury")
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
        return (side == NetworkManager.Side.C2S ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT).buildPacket(packetBuffer, CHANNEL_ID).getThis();
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
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation("architectury:network");
    static final ResourceLocation SYNC_IDS = new ResourceLocation("architectury:sync_ids");
    static final EventNetworkChannel CHANNEL = ChannelBuilder.named(CHANNEL_ID).acceptedVersions((status, version) -> true).optional().eventNetworkChannel();
    static final Map<ResourceLocation, NetworkReceiver> S2C = Maps.newHashMap();
    static final Map<ResourceLocation, NetworkReceiver> C2S = Maps.newHashMap();
    static final Map<ResourceLocation, PacketTransformer> S2C_TRANSFORMERS = Maps.newHashMap();
    static final Map<ResourceLocation, PacketTransformer> C2S_TRANSFORMERS = Maps.newHashMap();
    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();
    private static final Multimap<Player, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);
    
    static {
        CHANNEL.addListener(createPacketHandler(NetworkDirection.PLAY_TO_SERVER, C2S_TRANSFORMERS));
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        
        registerC2SReceiver(SYNC_IDS, Collections.emptyList(), (buffer, context) -> {
            Set<ResourceLocation> receivables = (Set<ResourceLocation>) clientReceivables.get(context.getPlayer());
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
        });
    }
    
    static <T extends CustomPayloadEvent> Consumer<T> createPacketHandler(NetworkDirection direction, Map<ResourceLocation, PacketTransformer> map) {
        return event -> {
            CustomPayloadEvent.Context context = event.getSource();
            if (context.getDirection() != direction) return;
            if (context.getPacketHandled()) return;
            FriendlyByteBuf buffer = event.getPayload();
            if (buffer == null) return;
            ResourceLocation type = buffer.readResourceLocation();
            PacketTransformer transformer = map.get(type);
            
            if (transformer != null) {
                NetworkManager.Side side = context.getDirection().getReceptionSide() == LogicalSide.CLIENT ? NetworkManager.Side.S2C : NetworkManager.Side.C2S;
                NetworkManager.PacketContext packetContext = new NetworkManager.PacketContext() {
                    @Override
                    public Player getPlayer() {
                        return getEnvironment() == Env.CLIENT ? getClientPlayer() : context.getSender();
                    }
                    
                    @Override
                    public void queue(Runnable runnable) {
                        context.enqueueWork(runnable);
                    }
                    
                    @Override
                    public Env getEnvironment() {
                        return context.getDirection().getReceptionSide() == LogicalSide.CLIENT ? Env.CLIENT : Env.SERVER;
                    }
                    
                    private Player getClientPlayer() {
                        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::getClientPlayer);
                    }
                };
                transformer.inbound(side, type, buffer, packetContext, (side1, id1, buf1) -> {
                    NetworkReceiver networkReceiver = side == NetworkManager.Side.C2S ? C2S.get(id1) : S2C.get(id1);
                    if (networkReceiver == null) {
                        throw new IllegalArgumentException("Network Receiver not found! " + id1);
                    }
                    networkReceiver.receive(buf1, packetContext);
                });
            } else {
                LOGGER.error("Unknown message ID: " + type);
            }
            
            context.setPacketHandled(true);
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
        try {
            // I love forge
            Constructor<?> constructor = Class.forName("net.minecraftforge.network.packets.SpawnEntity").getDeclaredConstructor(Entity.class);
            constructor.setAccessible(true);
            Object message = constructor.newInstance(entity);
            Packet<ClientGamePacketListener>[] packet = new Packet[1];
            NetworkInitialization.PLAY.send(message, new PacketDistributor.PacketTarget(p -> {
                packet[0] = (Packet<ClientGamePacketListener>) p;
            }, NetworkDirection.PLAY_TO_CLIENT));
            return Objects.requireNonNull(packet[0], "Expected packet to be sent!");
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
}
