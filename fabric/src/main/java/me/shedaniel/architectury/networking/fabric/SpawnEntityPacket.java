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

package me.shedaniel.architectury.networking.fabric;

import me.shedaniel.architectury.extensions.network.EntitySpawnExtension;
import me.shedaniel.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

/**
 * @see net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 */
public class SpawnEntityPacket {
    private static final ResourceLocation PACKET_ID = new ResourceLocation("architectury", "spawn_entity_packet");
    
    public static Packet<?> create(Entity entity) {
        if (entity.level.isClientSide()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeVarInt(Registry.ENTITY_TYPE.getId(entity.getType()));
        buffer.writeUUID(entity.getUUID());
        buffer.writeVarInt(entity.getId());
        Vec3 position = entity.position();
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
        buffer.writeFloat(entity.xRot);
        buffer.writeFloat(entity.yRot);
        buffer.writeFloat(entity.getYHeadRot());
        Vec3 deltaMovement = entity.getDeltaMovement();
        buffer.writeDouble(deltaMovement.x);
        buffer.writeDouble(deltaMovement.y);
        buffer.writeDouble(deltaMovement.z);
        if (entity instanceof EntitySpawnExtension) {
            ((EntitySpawnExtension) entity).saveAdditionalSpawnData(buffer);
        }
        return NetworkManager.toPacket(NetworkManager.s2c(), PACKET_ID, buffer);
    }
    
    
    @Environment(EnvType.CLIENT)
    public static class Client {
        @Environment(EnvType.CLIENT)
        public static void register() {
            NetworkManager.registerReceiver(NetworkManager.s2c(), PACKET_ID, Client::receive);
        }
        
        @Environment(EnvType.CLIENT)
        public static void receive(FriendlyByteBuf buf, NetworkManager.PacketContext context) {
            int entityTypeId = buf.readVarInt();
            UUID uuid = buf.readUUID();
            int id = buf.readVarInt();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float xRot = buf.readFloat();
            float yRot = buf.readFloat();
            float yHeadRot = buf.readFloat();
            double deltaX = buf.readDouble();
            double deltaY = buf.readDouble();
            double deltaZ = buf.readDouble();
            context.queue(() -> {
                EntityType<?> entityType = Registry.ENTITY_TYPE.byId(entityTypeId);
                if (entityType == null) {
                    throw new IllegalStateException("Entity type (" + entityTypeId + ") is unknown, spawning at (" + x + ", " + y + ", " + z + ")");
                }
                if (Minecraft.getInstance().level == null) {
                    throw new IllegalStateException("Client world is null!");
                }
                Entity entity = entityType.create(Minecraft.getInstance().level);
                if (entity == null) {
                    throw new IllegalStateException("Created entity is null!");
                }
                entity.setUUID(uuid);
                entity.setId(id);
                entity.setPacketCoordinates(x, y, z);
                entity.absMoveTo(x, y, z, xRot, yRot);
                entity.setYHeadRot(yHeadRot);
                entity.setYBodyRot(yHeadRot);
                if (entity instanceof EntitySpawnExtension) {
                    ((EntitySpawnExtension) entity).loadAdditionalSpawnData(buf);
                }
                Minecraft.getInstance().level.putNonPlayerEntity(id, entity);
                entity.lerpMotion(deltaX, deltaY, deltaZ);
            });
        }
    }
}
