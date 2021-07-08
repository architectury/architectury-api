package me.shedaniel.architectury.networking.simple;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public abstract class BaseS2CPacket extends BasePacket {
    private void sendTo(@Nullable ServerPlayer player, Packet<?> packet) {
        if (player == null) {
            System.err.println("Unable to send packet '" + getId().getId() + "' to a 'null' player!");
        } else {
            player.connection.send(packet);
        }
    }
    
    public final void sendTo(ServerPlayer player) {
        sendTo(player, toPacket());
    }
    
    public final void sendTo(Iterable<ServerPlayer> players) {
        Packet<?> packet = toPacket();
        
        for (ServerPlayer player : players) {
            sendTo(player, packet);
        }
    }
    
    public final void sendToAll(MinecraftServer server) {
        sendTo(server.getPlayerList().getPlayers());
    }
    
    public final void sendToDimension(ServerLevel dimension) {
        sendTo(dimension.players());
    }
    
    public final void sendToChunkListeners(LevelChunk chunk) {
        Packet<?> packet = toPacket();
        ((ServerChunkCache) chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(e -> sendTo(e, packet));
    }
}
