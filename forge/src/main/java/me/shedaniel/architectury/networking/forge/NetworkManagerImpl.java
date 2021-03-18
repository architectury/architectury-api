/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.networking.forge;


import com.google.common.collect.*;
import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.NetworkManager.NetworkReceiver;
import me.shedaniel.architectury.utils.Env;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.event.EventNetworkChannel;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = ArchitecturyForge.MOD_ID)
public class NetworkManagerImpl {
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, NetworkReceiver receiver) {
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, receiver);
        }
    }
    
    public static Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buffer) {
        FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
        packetBuffer.writeResourceLocation(id);
        packetBuffer.writeBytes(buffer);
        return (side == NetworkManager.Side.C2S ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT).buildPacket(Pair.of(packetBuffer, 0), CHANNEL_ID).getThis();
    }
    
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation("architectury:network");
    static final ResourceLocation SYNC_IDS = new ResourceLocation("architectury:sync_ids");
    static final EventNetworkChannel CHANNEL = NetworkRegistry.newEventChannel(CHANNEL_ID, () -> "1", version -> true, version -> true);
    static final Map<ResourceLocation, NetworkReceiver> S2C = Maps.newHashMap();
    static final Map<ResourceLocation, NetworkReceiver> C2S = Maps.newHashMap();
    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();
    private static final Multimap<Player, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);
    
    static {
        CHANNEL.addListener(createPacketHandler(NetworkEvent.ClientCustomPayloadEvent.class, C2S));
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        
        registerC2SReceiver(SYNC_IDS, (buffer, context) -> {
            Set<ResourceLocation> receivables = (Set<ResourceLocation>) clientReceivables.get(context.getPlayer());
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
        });
    }
    
    static <T extends NetworkEvent> Consumer<T> createPacketHandler(Class<T> clazz, Map<ResourceLocation, NetworkReceiver> map) {
        return event -> {
            if (event.getClass() != clazz) return;
            NetworkEvent.Context context = event.getSource().get();
            if (context.getPacketHandled()) return;
            FriendlyByteBuf buffer = event.getPayload();
            if (buffer == null) return;
            ResourceLocation type = buffer.readResourceLocation();
            NetworkReceiver receiver = map.get(type);
            
            if (receiver != null) {
                receiver.receive(buffer, new NetworkManager.PacketContext() {
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
                });
            } else {
                LOGGER.error("Unknown message ID: " + type);
            }
            
            context.setPacketHandled(true);
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void registerS2CReceiver(ResourceLocation id, NetworkReceiver receiver) {
        S2C.put(id, receiver);
    }
    
    public static void registerC2SReceiver(ResourceLocation id, NetworkReceiver receiver) {
        C2S.put(id, receiver);
    }
    
    public static boolean canServerReceive(ResourceLocation id) {
        return serverReceivables.contains(id);
    }
    
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return clientReceivables.get(player).contains(id);
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
        NetworkManager.sendToPlayer((ServerPlayer) event.getPlayer(), SYNC_IDS, sendSyncPacket(C2S));
    }
    
    @SubscribeEvent
    public static void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        clientReceivables.removeAll(event.getPlayer());
    }
}
