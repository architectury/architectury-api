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

package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;

/**
 * A simple wrapper for {@link NetworkManager} to make it easier to register packets and send them to clients/servers.
 *
 * @author LatvianModder
 */
public class SimpleNetworkManager {
    /**
     * Creates a new {@code SimpleNetworkManager}.
     *
     * @param namespace a unique namespace for the packets ({@link #namespace})
     * @return the created network manager
     */
    public static SimpleNetworkManager create(String namespace) {
        return new SimpleNetworkManager(namespace);
    }
    
    /**
     * The unique namespace for the packets managed by this manager.
     * This will typically be a mod ID.
     */
    public final String namespace;
    
    private SimpleNetworkManager(String n) {
        namespace = n;
    }
    
    /**
     * Registers a server -&gt; client packet.
     *
     * @param id a unique ID for the packet, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder the packet decoder for the packet
     * @return a {@link PacketID} describing the registered packet
     */
    public PacketID registerS2C(String id, PacketDecoder<BaseS2CPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(namespace, id), NetworkManager.s2c());
        
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
            NetworkManager.registerReceiver(NetworkManager.s2c(), packetID.getId(), receiver);
        }
        
        return packetID;
    }
    
    /**
     * Registers a client -&gt; server packet.
     *
     * @param id a unique ID for the packet, must be a valid value for {@link ResourceLocation#getPath}
     * @param decoder the packet decoder for the packet
     * @return a {@link PacketID} describing the registered packet
     */
    public PacketID registerC2S(String id, PacketDecoder<BaseC2SPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(namespace, id), NetworkManager.c2s());
        NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
        NetworkManager.registerReceiver(NetworkManager.c2s(), packetID.getId(), receiver);
        return packetID;
    }
}
