package me.shedaniel.architectury.test.networking;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.networking.simple.BaseS2CPacket;
import me.shedaniel.architectury.networking.simple.PacketID;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;

public class SyncDataPacket extends BaseS2CPacket {
    private final CompoundTag serverData;
    
    /**
     * To send this packet, call new SyncDataPacket(tag).sendToPlayer(player) / sendToAll(server) / etc.
     *
     * @see BaseS2CPacket
     */
    public SyncDataPacket(CompoundTag tag) {
        serverData = tag;
    }
    
    public SyncDataPacket(FriendlyByteBuf buf) {
        serverData = buf.readAnySizeNbt();
    }
    
    @Override
    public PacketID getId() {
        return TestModNet.SYNC_DATA;
    }
    
    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(serverData);
    }
    
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.getPlayer().sendMessage(new TextComponent("Received data from server: " + serverData), Util.NIL_UUID);
    }
}
