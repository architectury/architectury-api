/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.networking.forge;


import com.google.common.collect.*;
import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.NetworkManager.NetworkReceiver;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.event.EventNetworkChannel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class NetworkManagerImpl {
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, NetworkReceiver receiver) {
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, receiver);
        }
    }
    
    public static IPacket<?> toPacket(NetworkManager.Side side, ResourceLocation id, PacketBuffer buffer) {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeResourceLocation(id);
        packetBuffer.writeBytes(buffer);
        return (side == NetworkManager.Side.C2S ? NetworkDirection.PLAY_TO_SERVER : NetworkDirection.PLAY_TO_CLIENT).buildPacket(Pair.of(packetBuffer, 0), CHANNEL_ID).getThis();
    }
    
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation("architectury:network");
    static final ResourceLocation SYNC_IDS = new ResourceLocation("architectury:sync_ids");
    static final EventNetworkChannel CHANNEL = NetworkRegistry.newEventChannel(CHANNEL_ID, () -> "1", version -> true, version -> true);
    static final Map<ResourceLocation, NetworkReceiver> S2C = Maps.newHashMap();
    static final Map<ResourceLocation, NetworkReceiver> C2S = Maps.newHashMap();
    static final Set<ResourceLocation> serverReceivables = Sets.newHashSet();
    private static final Multimap<PlayerEntity, ResourceLocation> clientReceivables = Multimaps.newMultimap(Maps.newHashMap(), Sets::newHashSet);
    
    static  {
        CHANNEL.addListener(createPacketHandler(NetworkEvent.ClientCustomPayloadEvent.class, C2S));
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::initClient);
        
        MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedInEvent>addListener(event -> NetworkManager.sendToPlayer((ServerPlayerEntity) event.getPlayer(), SYNC_IDS, sendSyncPacket(C2S)));
        MinecraftForge.EVENT_BUS.<PlayerEvent.PlayerLoggedOutEvent>addListener(event -> clientReceivables.removeAll(event.getPlayer()));
        
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
            PacketBuffer buffer = new PacketBuffer(event.getPayload().copy());
            ResourceLocation type = buffer.readResourceLocation();
            NetworkReceiver receiver = map.get(type);
            
            if (receiver != null) {
                receiver.receive(buffer, new NetworkManager.PacketContext() {
                    @Override
                    public PlayerEntity getPlayer() {
                        return getEnv() == Dist.CLIENT ? getClientPlayer() : context.getSender();
                    }
                    
                    @Override
                    public void queue(Runnable runnable) {
                        context.enqueueWork(runnable);
                    }
                    
                    @Override
                    public Dist getEnv() {
                        return context.getDirection().getReceptionSide() == LogicalSide.CLIENT ? Dist.CLIENT : Dist.DEDICATED_SERVER;
                    }
                    
                    private PlayerEntity getClientPlayer() {
                        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> ClientNetworkingManager::getClientPlayer);
                    }
                });
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
    
    public static boolean canPlayerReceive(ServerPlayerEntity player, ResourceLocation id) {
        return clientReceivables.get(player).contains(id);
    }
    
    static PacketBuffer sendSyncPacket(Map<ResourceLocation, NetworkReceiver> map) {
        List<ResourceLocation> availableIds = Lists.newArrayList(map.keySet());
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeInt(availableIds.size());
        for (ResourceLocation availableId : availableIds) {
            packetBuffer.writeResourceLocation(availableId);
        }
        return packetBuffer;
    }
}
