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

package dev.architectury.networking.transformers;

import net.minecraft.network.protocol.Packet;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SinglePacketCollector<P extends Packet<?>> implements PacketSink<P> {
    @Nullable
    private final Consumer<P> consumer;
    private P packet;
    
    public SinglePacketCollector(@Nullable Consumer<P> consumer) {
        this.consumer = consumer;
    }
    
    @Override
    public void accept(P packet) {
        if (this.packet == null) {
            this.packet = packet;
            if (this.consumer != null) {
                this.consumer.accept(packet);
            }
        } else {
            throw new IllegalStateException("Already accepted one packet!");
        }
    }
    
    public P getPacket() {
        return packet;
    }
}
