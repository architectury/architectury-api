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

import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

/**
 * The base class for messages managed by a {@link SimpleNetworkManager}.
 *
 * @author LatvianModder
 * @see BaseC2SMessage
 * @see BaseS2CMessage
 */
public abstract class Message {
    Message() {
    }
    
    /**
     * {@return the {@link MessageType } of this message}
     *
     * @see SimpleNetworkManager#registerC2S(String, PacketDecoder)
     * @see SimpleNetworkManager#registerS2C(String, PacketDecoder)
     */
    public abstract MessageType getType();
    
    /**
     * Writes this message to a byte buffer.
     *
     * @param buf the byte buffer
     */
    public abstract void write(FriendlyByteBuf buf);
    
    /**
     * Handles this message when it is received.
     *
     * @param context the packet context for handling this message
     */
    public abstract void handle(NetworkManager.PacketContext context);
    
    /**
     * Converts this message into a corresponding vanilla {@link Packet}.
     *
     * @return the converted {@link Packet}
     */
    public final Packet<?> toPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        write(buf);
        return NetworkManager.toPacket(getType().getSide(), getType().getId(), buf);
    }
}
