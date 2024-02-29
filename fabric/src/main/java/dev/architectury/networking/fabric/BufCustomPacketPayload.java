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

package dev.architectury.networking.fabric;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Wraps a {@link FriendlyByteBuf}.
 */
public record BufCustomPacketPayload(Type<BufCustomPacketPayload> _type,
                                     byte[] payload) implements CustomPacketPayload {
    public BufCustomPacketPayload(Type<BufCustomPacketPayload> _type, RegistryFriendlyByteBuf payload) {
        this(_type, payload.readByteArray());
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return this._type();
    }
    
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeByteArray(payload);
    }
    
    public static StreamCodec<ByteBuf, BufCustomPacketPayload> streamCodec(Type<BufCustomPacketPayload> type) {
        return ByteBufCodecs.BYTE_ARRAY.map(bytes -> new BufCustomPacketPayload(type, bytes), BufCustomPacketPayload::payload);
    }
}
