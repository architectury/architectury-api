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

package dev.architectury.networking.client.fabric;

import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.fabric.NetworkManagerImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class ClientNetworkManagerImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        LOGGER.info("Registering S2C receiver with id {}", type.id());
        ClientPlayNetworking.registerGlobalReceiver(type, new ClientPlayPayloadHandler<>(receiver));
    }
    
    // Lambda methods aren't included in @EnvType, so this inelegant solution is used instead.
    @Environment(EnvType.CLIENT)
    static class ClientPlayPayloadHandler<T extends CustomPacketPayload> implements ClientPlayNetworking.PlayPayloadHandler<T> {
        private final NetworkManager.NetworkReceiver<T> receiver;
        
        ClientPlayPayloadHandler(NetworkManager.NetworkReceiver<T> receiver) {
            this.receiver = receiver;
        }
        
        @Override
        public void receive(T payload, ClientPlayNetworking.Context fabricContext) {
            var context = NetworkManagerImpl.context(fabricContext.player(), fabricContext.client(), true);
            receiver.receive(payload, context);
        }
    }
}
