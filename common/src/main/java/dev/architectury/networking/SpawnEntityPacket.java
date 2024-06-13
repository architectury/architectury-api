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
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.UUID;

/**
 * @see net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 */
public class SpawnEntityPacket {
    private static final ResourceLocation PACKET_ID = ResourceLocation.fromNamespaceAndPath("architectury", "spawn_entity_packet");
    private static final CustomPacketPayload.Type<PacketPayload> PACKET_TYPE = new CustomPacketPayload.Type<>(PACKET_ID);
    private static final StreamCodec<RegistryFriendlyByteBuf, PacketPayload> PACKET_CODEC = CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new);
    
    public static Packet<ClientGamePacketListener> create(Entity entity, ServerEntity serverEntity) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        return (Packet<ClientGamePacketListener>) NetworkManager.toPacket(NetworkManager.s2c(), new PacketPayload(entity, serverEntity), entity.registryAccess());
    }
    
    public static void register() {
        NetworkManager.registerS2CPayloadType(PACKET_TYPE, PACKET_CODEC);
    }
    
    
    @Environment(EnvType.CLIENT)
    public static class Client {
        @Environment(EnvType.CLIENT)
        public static void register() {
            NetworkManager.registerReceiver(NetworkManager.s2c(), PACKET_TYPE, PACKET_CODEC, Client::receive);
        }
        
        @Environment(EnvType.CLIENT)
        private static void receive(PacketPayload payload, NetworkManager.PacketContext context) {
            context.queue(() -> {
                if (Minecraft.getInstance().level == null) {
                    throw new IllegalStateException("Client world is null!");
                }
                var entity = payload.entityType().create(Minecraft.getInstance().level);
                if (entity == null) {
                    throw new IllegalStateException("Created entity is null!");
                }
                entity.setUUID(payload.uuid());
                entity.setId(payload.id());
                entity.syncPacketPositionCodec(payload.x(), payload.y(), payload.z());
                entity.moveTo(payload.x(), payload.y(), payload.z());
                entity.setXRot(payload.xRot());
                entity.setYRot(payload.yRot());
                entity.setYHeadRot(payload.yHeadRot());
                entity.setYBodyRot(payload.yHeadRot());
                if (entity instanceof EntitySpawnExtension ext) {
                    RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()), context.registryAccess());
                    ext.loadAdditionalSpawnData(buf);
                    buf.release();
                }
                Minecraft.getInstance().level.addEntity(entity);
                entity.lerpMotion(payload.deltaX(), payload.deltaY(), payload.deltaZ());
            });
        }
    }
    
    private record PacketPayload(EntityType<?> entityType, UUID uuid, int id, double x, double y, double z, float xRot,
                                 float yRot,
                                 float yHeadRot,
                                 double deltaX, double deltaY, double deltaZ,
                                 byte[] data) implements CustomPacketPayload {
        public PacketPayload(RegistryFriendlyByteBuf buf) {
            this(ByteBufCodecs.registry(Registries.ENTITY_TYPE).decode(buf), buf.readUUID(), buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readByteArray());
        }
        
        public PacketPayload(Entity entity, ServerEntity serverEntity) {
            this(entity.getType(), entity.getUUID(), entity.getId(), serverEntity.getPositionBase().x(),
                    serverEntity.getPositionBase().y(), serverEntity.getPositionBase().z(), serverEntity.getLastSentXRot(),
                    serverEntity.getLastSentYRot(), serverEntity.getLastSentYHeadRot(), serverEntity.getLastSentMovement().x,
                    serverEntity.getLastSentMovement().y, serverEntity.getLastSentMovement().z, saveExtra(entity));
        }
        
        public PacketPayload(Entity entity, BlockPos pos) {
            this(entity.getType(), entity.getUUID(), entity.getId(), pos.getX(),
                    pos.getY(), pos.getZ(), entity.getXRot(), entity.getYRot(), entity.getYHeadRot(),
                    entity.getDeltaMovement().x, entity.getDeltaMovement().y, entity.getDeltaMovement().z, saveExtra(entity));
        }
        
        private static byte[] saveExtra(Entity entity) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            try {
                if (entity instanceof EntitySpawnExtension ext) {
                    ext.saveAdditionalSpawnData(buf);
                }
                return ByteBufUtil.getBytes(buf);
            } finally {
                buf.release();
            }
        }
        
        public void write(RegistryFriendlyByteBuf buf) {
            ByteBufCodecs.registry(Registries.ENTITY_TYPE).encode(buf, entityType);
            buf.writeUUID(uuid);
            buf.writeVarInt(id);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(xRot);
            buf.writeFloat(yRot);
            buf.writeFloat(yHeadRot);
            buf.writeDouble(deltaX);
            buf.writeDouble(deltaY);
            buf.writeDouble(deltaZ);
            buf.writeByteArray(data);
        }
        
        @Override
        public Type<? extends CustomPacketPayload> type() {
            return PACKET_TYPE;
        }
    }
}
