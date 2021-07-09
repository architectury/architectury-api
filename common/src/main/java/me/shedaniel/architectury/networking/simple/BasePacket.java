package me.shedaniel.architectury.networking.simple;

import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

/**
 * @author LatvianModder
 */
public abstract class BasePacket {
    BasePacket() {
    }
    
    public abstract PacketID getId();
    
    public abstract void write(FriendlyByteBuf buf);
    
    public abstract void handle(NetworkManager.PacketContext context);
    
    public final Packet<?> toPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        write(buf);
        return NetworkManager.toPacket(getId().getSide(), getId().getId(), buf);
    }
}
