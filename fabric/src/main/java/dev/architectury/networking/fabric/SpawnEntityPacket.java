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

import dev.architectury.extensions.network.EntitySpawnExtension;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * @see net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 */
public class SpawnEntityPacket {
    private static final ResourceLocation PACKET_ID = new ResourceLocation("architectury", "spawn_entity_packet");
    
    public static Packet<ClientGamePacketListener> create(Entity entity) {
        if (entity.level.isClientSide()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        var buffer = PacketByteBufs.create();
        buffer.writeVarInt(BuiltInRegistries.ENTITY_TYPE.getId(entity.getType()));
        buffer.writeUUID(entity.getUUID());
        buffer.writeVarInt(entity.getId());
        var position = entity.position();
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
        buffer.writeFloat(entity.getXRot());
        buffer.writeFloat(entity.getYRot());
        buffer.writeFloat(entity.getYHeadRot());
        var deltaMovement = entity.getDeltaMovement();
        buffer.writeDouble(deltaMovement.x);
        buffer.writeDouble(deltaMovement.y);
        buffer.writeDouble(deltaMovement.z);
        if (entity instanceof EntitySpawnExtension ext) {
            ext.saveAdditionalSpawnData(buffer);
        }
        return (Packet<ClientGamePacketListener>) NetworkManager.toPacket(NetworkManager.s2c(), PACKET_ID, buffer);
    }
    
    
    @Environment(EnvType.CLIENT)
    public static class Client {
        @Environment(EnvType.CLIENT)
        public static void register() {
            NetworkManager.registerReceiver(NetworkManager.s2c(), PACKET_ID, Client::receive);
        }
        
        @Environment(EnvType.CLIENT)
        public static void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
            var entityTypeId = buf.readVarInt();
            var uuid = buf.readUUID();
            var id = buf.readVarInt();
            var x = buf.readDouble();
            var y = buf.readDouble();
            var z = buf.readDouble();
            var xRot = buf.readFloat();
            var yRot = buf.readFloat();
            var yHeadRot = buf.readFloat();
            var deltaX = buf.readDouble();
            var deltaY = buf.readDouble();
            var deltaZ = buf.readDouble();
            // Retain this buffer so we can use it in the queued task (EntitySpawnExtension)
            buf.retain();
            context.queue(() -> {
                var entityType = BuiltInRegistries.ENTITY_TYPE.byId(entityTypeId);
                if (entityType == null) {
                    throw new IllegalStateException("Entity type (" + entityTypeId + ") is unknown, spawning at (" + x + ", " + y + ", " + z + ")");
                }
                if (Minecraft.getInstance().level == null) {
                    throw new IllegalStateException("Client world is null!");
                }
                var entity = entityType.create(Minecraft.getInstance().level);
                if (entity == null) {
                    throw new IllegalStateException("Created entity is null!");
                }
                entity.setUUID(uuid);
                entity.setId(id);
                entity.syncPacketPositionCodec(x, y, z);
                entity.moveTo(x, y, z);
                entity.setXRot(xRot);
                entity.setYRot(yRot);
                entity.setYHeadRot(yHeadRot);
                entity.setYBodyRot(yHeadRot);
                if (entity instanceof EntitySpawnExtension ext) {
                    ext.loadAdditionalSpawnData(buf);
                }
                buf.release();
                Minecraft.getInstance().level.putNonPlayerEntity(id, entity);
                entity.lerpMotion(deltaX, deltaY, deltaZ);
            });
        }
    }
}
