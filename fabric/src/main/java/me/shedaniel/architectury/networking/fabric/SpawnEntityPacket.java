package me.shedaniel.architectury.networking.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.impl.networking.ServerSidePacketRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.UUID;

public class SpawnEntityPacket {
    
    public static final ResourceLocation NAME = new ResourceLocation("architectury", "spawn_entity_packet");
    
    @Environment(EnvType.CLIENT)
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(NAME, SpawnEntityPacket::receive);
    }
    
    public static Packet<?> create(Entity entity){
        if (entity.level.isClientSide()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        FriendlyByteBuf buffer = PacketByteBufs.create();
        buffer.writeVarInt(Registry.ENTITY_TYPE.getId(entity.getType()));
        buffer.writeUUID(entity.getUUID());
        buffer.writeVarInt(entity.getId());
        buffer.writeDouble(entity.position().x);
        buffer.writeDouble(entity.position().y);
        buffer.writeDouble(entity.position().z);
        return ServerSidePacketRegistryImpl.INSTANCE.toPacket(NAME, buffer);
    }
    
    @Environment(EnvType.CLIENT)
    public static void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        EntityType<?> entityType = Registry.ENTITY_TYPE.byId(buf.readVarInt());
        UUID uuid = buf.readUUID();
        int entityId = buf.readVarInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        client.execute(() -> {
            if(client.level == null){
                throw new IllegalStateException("Client world is null!");
            }
            Entity entity = entityType.create(client.level);
            if(entity == null){
                throw new IllegalStateException("Created entity is null!");
            }
            entity.setUUID(uuid);
            entity.setId(entityId);
            entity.setPos(x, y, z);
            entity.setPacketCoordinates(x, y, z);
            client.level.putNonPlayerEntity(entityId, entity);
        });
    }
    
}
