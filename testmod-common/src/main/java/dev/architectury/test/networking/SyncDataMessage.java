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

package dev.architectury.test.networking;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncDataMessage(CompoundTag serverData) implements CustomPacketPayload {
    public static final Type<SyncDataMessage> TYPE = new Type<>(Identifier.fromNamespaceAndPath(ArchitecturyConstants.MOD_ID, "sync_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDataMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SyncDataMessage::serverData,
            SyncDataMessage::new
    );
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public void handle(NetworkManager.PacketContext context) {
        context.getPlayer().sendSystemMessage(Component.literal("Received data from server: " + serverData));
    }
}