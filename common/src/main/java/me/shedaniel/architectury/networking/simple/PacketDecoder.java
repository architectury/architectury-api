package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Decodes a {@link BasePacket} from a {@link FriendlyByteBuf}.
 *
 * @param <T> the packet type handled by this decoder
 * @author LatvianModder
 */
@FunctionalInterface
public interface PacketDecoder<T extends BasePacket> {
    /**
     * Decodes a {@code T} packet from a byte buffer.
     *
     * @param buf the byte buffer
     * @return the decoded instance
     */
    T decode(FriendlyByteBuf buf);
    
    /**
     * Creates a network receiver from this decoder.
     *
     * <p>The returned receiver will first {@linkplain #decode(FriendlyByteBuf) decode a packet}
     * and then call {@link BasePacket#handle(NetworkManager.PacketContext)} on the decoded packet.
     *
     * @return the created receiver
     */
    default NetworkManager.NetworkReceiver createReceiver() {
        return (buf, context) -> {
            BasePacket packet = decode(buf);
            context.queue(() -> packet.handle(context));
        };
    }
}
