package me.shedaniel.architectury.networking.simple;

import io.netty.buffer.Unpooled;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

/**
 * The base class for packets managed by a {@link SimpleNetworkManager}.
 *
 * @author LatvianModder
 * @see BaseC2SPacket
 * @see BaseS2CPacket
 */
public abstract class BasePacket {
    BasePacket() {
    }
    
    /**
     * {@return the {@link PacketID} of this packet}
     *
     * @see SimpleNetworkManager#registerC2S(String, PacketDecoder)
     * @see SimpleNetworkManager#registerS2C(String, PacketDecoder)
     */
    public abstract PacketID getId();
    
    /**
     * Writes this packet to a byte buffer.
     *
     * @param buf the byte buffer
     */
    public abstract void write(FriendlyByteBuf buf);
    
    /**
     * Handles this packet when it is received.
     *
     * @param context the packet context for handling this packet
     */
    public abstract void handle(NetworkManager.PacketContext context);
    
    /**
     * Converts this packet into a corresponding vanilla {@link Packet}.
     *
     * @return the converted packet
     */
    public final Packet<?> toPacket() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        write(buf);
        return NetworkManager.toPacket(getId().getSide(), getId().getId(), buf);
    }
}
