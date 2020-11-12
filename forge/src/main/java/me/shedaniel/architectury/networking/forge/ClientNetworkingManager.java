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

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;

import static me.shedaniel.architectury.networking.forge.NetworkManagerImpl.C2S;
import static me.shedaniel.architectury.networking.forge.NetworkManagerImpl.SYNC_IDS;

@OnlyIn(Dist.CLIENT)
public class ClientNetworkingManager {
    public static void initClient() {
        NetworkManagerImpl.CHANNEL.addListener(NetworkManagerImpl.createPacketHandler(NetworkEvent.ServerCustomPayloadEvent.class, NetworkManagerImpl.S2C));
        MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggedOutEvent>addListener(event -> NetworkManagerImpl.serverReceivables.clear());
        
        NetworkManagerImpl.registerS2CReceiver(SYNC_IDS, (buffer, context) -> {
            Set<ResourceLocation> receivables = NetworkManagerImpl.serverReceivables;
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
            NetworkManager.sendToServer(SYNC_IDS, NetworkManagerImpl.sendSyncPacket(C2S));
        });
    }
    
    public static PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}