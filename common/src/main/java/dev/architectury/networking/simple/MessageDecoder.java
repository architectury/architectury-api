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
import net.minecraft.network.FriendlyByteBuf;

/**
 * Decodes a {@link Message} from a {@link FriendlyByteBuf}.
 *
 * @param <T> the message type handled by this decoder
 * @author LatvianModder
 */
@FunctionalInterface
public interface MessageDecoder<T extends Message> {
    /**
     * Decodes a {@code T} message from a byte buffer.
     *
     * @param buf the byte buffer
     * @return the decoded instance
     */
    T decode(FriendlyByteBuf buf);
    
    /**
     * Creates a network receiver from this decoder.
     *
     * <p>The returned receiver will first {@linkplain #decode(FriendlyByteBuf) decode a message}
     * and then call {@link Message#handle(NetworkManager.PacketContext)} on the decoded message.
     *
     * @return the created receiver
     */
    default NetworkManager.NetworkReceiver createReceiver() {
        return (buf, context) -> {
            Message packet = decode(buf);
            context.queue(() -> packet.handle(context));
        };
    }
}