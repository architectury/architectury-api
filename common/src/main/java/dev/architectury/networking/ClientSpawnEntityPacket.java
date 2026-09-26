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

package dev.architectury.networking;

import dev.architectury.extensions.network.EntitySpawnExtension;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.phys.Vec3;

public class ClientSpawnEntityPacket {
    public static void register() {
        NetworkManager.registerReceiver(NetworkManager.s2c(), SpawnEntityPacket.PACKET_TYPE, SpawnEntityPacket.PACKET_CODEC, ClientSpawnEntityPacket::receive);
    }
    
    private static void receive(SpawnEntityPacket.PacketPayload payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (Minecraft.getInstance().level == null) {
                throw new IllegalStateException("Client world is null!");
            }
            var entity = payload.entityType().create(Minecraft.getInstance().level, EntitySpawnReason.LOAD);
            if (entity == null) {
                throw new IllegalStateException("Created entity is null!");
            }
            entity.setUUID(payload.uuid());
            entity.setId(payload.id());
            entity.syncPacketPositionCodec(payload.x(), payload.y(), payload.z());
            entity.snapTo(payload.x(), payload.y(), payload.z(), payload.yRot(), payload.xRot());
            entity.setYHeadRot(payload.yHeadRot());
            entity.setYBodyRot(payload.yHeadRot());
            if (entity instanceof EntitySpawnExtension ext) {
                RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()), context.registryAccess());
                ext.loadAdditionalSpawnData(buf);
                buf.release();
            }
            Minecraft.getInstance().level.addEntity(entity);
            entity.lerpMotion(new Vec3(payload.deltaX(), payload.deltaY(), payload.deltaZ()));
        });
    }
}
