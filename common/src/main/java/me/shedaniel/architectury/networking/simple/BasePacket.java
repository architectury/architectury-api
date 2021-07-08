package me.shedaniel.architectury.networking.simple;

import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public abstract class BasePacket {
    BasePacket() {
    }
    
    public abstract PacketID getId();
    
    public abstract void write(FriendlyByteBuf buf);
    
    public abstract void handle(NetworkManager.PacketContext context);
    
    public final <T> Packet<?> toPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        write(buf);
        return NetworkManager.toPacket(getId().getSide(), getId().getId(), buf);
    }
}
