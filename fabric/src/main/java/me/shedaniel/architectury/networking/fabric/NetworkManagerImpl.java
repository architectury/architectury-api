package me.shedaniel.architectury.networking.fabric;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.NetworkManager.NetworkReceiver;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class NetworkManagerImpl implements NetworkManager.Impl {
    @Override
    public void registerReceiver(NetworkManager.Side side, ResourceLocation id, NetworkReceiver receiver) {
        if (side == NetworkManager.Side.C2S) {
            registerC2SReceiver(id, receiver);
        } else if (side == NetworkManager.Side.S2C) {
            registerS2CReceiver(id, receiver);
        }
    }
    
    private void registerC2SReceiver(ResourceLocation id, NetworkReceiver receiver) {
        ServerSidePacketRegistry.INSTANCE.register(id, (packetContext, buf) -> receiver.receive(buf, to(packetContext)));
    }
    
    @Environment(EnvType.CLIENT)
    private void registerS2CReceiver(ResourceLocation id, NetworkReceiver receiver) {
        ClientSidePacketRegistry.INSTANCE.register(id, (packetContext, buf) -> receiver.receive(buf, to(packetContext)));
    }
    
    private NetworkManager.PacketContext to(PacketContext context) {
        return new NetworkManager.PacketContext() {
            @Override
            public Player getPlayer() {
                return context.getPlayer();
            }
            
            @Override
            public void queue(Runnable runnable) {
                context.getTaskQueue().execute(runnable);
            }
            
            @Override
            public EnvType getEnv() {
                return context.getPacketEnvironment();
            }
        };
    }
    
    @Override
    public Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        if (side == NetworkManager.Side.C2S) {
            return toC2SPacket(id, buf);
        } else if (side == NetworkManager.Side.S2C) {
            return toS2CPacket(id, buf);
        }
        
        throw new IllegalArgumentException("Invalid side: " + side);
    }
    
    @Override
    public boolean canServerReceive(ResourceLocation id) {
        return ClientSidePacketRegistry.INSTANCE.canServerReceive(id);
    }
    
    @Override
    public boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return ServerSidePacketRegistry.INSTANCE.canPlayerReceive(player, id);
    }
    
    @Environment(EnvType.CLIENT)
    private Packet<?> toC2SPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return ClientSidePacketRegistry.INSTANCE.toPacket(id, buf);
    }
    
    private Packet<?> toS2CPacket(ResourceLocation id, FriendlyByteBuf buf) {
        return ServerSidePacketRegistry.INSTANCE.toPacket(id, buf);
    }
}
