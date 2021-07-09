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
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * A unique ID for a packet in a {@link SimpleNetworkManager}.
 *
 * @author LatvianModder
 */
public final class PacketID {
    private final SimpleNetworkManager manager;
    private final ResourceLocation id;
    private final NetworkManager.Side side;
    
    PacketID(SimpleNetworkManager h, ResourceLocation i, NetworkManager.Side s) {
        manager = h;
        id = i;
        side = s;
    }
    
    /**
     * {@return the network manager that manages this packet ID}
     */
    public SimpleNetworkManager getManager() {
        return manager;
    }
    
    /**
     * {@return the ID of this packet}
     */
    public ResourceLocation getId() {
        return id;
    }
    
    /**
     * {@return the network side of this packet}
     */
    public NetworkManager.Side getSide() {
        return side;
    }
    
    @Override
    public String toString() {
        return id.toString() + ":" + side.name().toLowerCase();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        PacketID packetID = (PacketID) o;
        return id.equals(packetID.id) && side == packetID.side;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, side);
    }
}
