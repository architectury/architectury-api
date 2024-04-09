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

package dev.architectury.networking.simple;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * A simple wrapper for {@link NetworkManager} to make it easier to register messages and send them to clients/servers.
 */
public class SimpleNetworkManager {
    /**
     * Creates a new {@code SimpleNetworkManager}.
     *
     * @param namespace a unique namespace for the messages ({@link #namespace})
     * @return the created network manager
     */
    public static SimpleNetworkManager create(String namespace) {
        return new SimpleNetworkManager(namespace);
    }
    
    /**
     * The unique namespace for the messages managed by this manager.
     * This will typically be a mod ID.
     */
    public final String namespace;
    
    private SimpleNetworkManager(String namespace) {
        this.namespace = namespace;
    }
    
    /**
     * Registers a server -&gt; client message with no packet transformers.
     *
     * @param id      a unique ID for the message, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder the message decoder for the message
     * @return a {@link MessageType} describing the registered message
     * @see #registerS2C(String, MessageDecoder, List)
     */
    public MessageType registerS2C(String id, MessageDecoder<BaseS2CMessage> decoder) {
        return registerS2C(id, decoder, List.of());
    }
    
    /**
     * Registers a server -&gt; client message using the given packet transformers.
     *
     * @param id           a unique ID for the message, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder      the message decoder for the message
     * @param transformers a list of packet transformers to apply to the message packet
     * @return a {@link MessageType} describing the registered message
     */
    @ApiStatus.Experimental
    public MessageType registerS2C(String id, MessageDecoder<BaseS2CMessage> decoder, List<PacketTransformer> transformers) {
        MessageType messageType = new MessageType(this, new ResourceLocation(namespace, id), NetworkManager.s2c());
        
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.NetworkReceiver<RegistryFriendlyByteBuf> receiver = decoder.createReceiver();
            NetworkManager.registerReceiver(NetworkManager.s2c(), messageType.getId(), transformers, receiver);
        }
        
        return messageType;
    }
    
    /**
     * Registers a client -&gt; server message with no packet transformers.
     *
     * @param id      a unique ID for the message, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder the message decoder for the message
     * @return a {@link MessageType} describing the registered message
     * @see #registerC2S(String, MessageDecoder, List)
     */
    public MessageType registerC2S(String id, MessageDecoder<BaseC2SMessage> decoder) {
        return registerC2S(id, decoder, List.of());
    }
    
    /**
     * Registers a client -&gt; server message using the given packet transformers.
     *
     * @param id           a unique ID for the message, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder      the message decoder for the message
     * @param transformers a list of packet transformers to apply to the message packet
     * @return a {@link MessageType} describing the registered message
     */
    @ApiStatus.Experimental
    public MessageType registerC2S(String id, MessageDecoder<BaseC2SMessage> decoder, List<PacketTransformer> transformers) {
        MessageType messageType = new MessageType(this, new ResourceLocation(namespace, id), NetworkManager.c2s());
        NetworkManager.NetworkReceiver<RegistryFriendlyByteBuf> receiver = decoder.createReceiver();
        NetworkManager.registerReceiver(NetworkManager.c2s(), messageType.getId(), transformers, receiver);
        return messageType;
    }
}