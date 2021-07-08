package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

@FunctionalInterface
public interface PacketDecoder<T extends BasePacket> {
    T decode(FriendlyByteBuf buf);
    
    default NetworkManager.NetworkReceiver createReceiver() {
        return (buf, context) -> {
            BasePacket packet = decode(buf);
            context.queue(() -> packet.handle(context));
        };
    }
}
