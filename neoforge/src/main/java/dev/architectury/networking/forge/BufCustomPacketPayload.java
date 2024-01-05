package dev.architectury.networking.forge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Wraps a {@link FriendlyByteBuf} because NeoForge doesn't easily let us use the buf directly.
 */
public record BufCustomPacketPayload(FriendlyByteBuf buf) implements CustomPacketPayload {
    
    @Override
    public void write(FriendlyByteBuf arg) {
        arg.writeBytes(buf);
    }
    
    @SuppressWarnings("NullableProblems")
    @Override
    public ResourceLocation id() {
        return NetworkManagerImpl.CHANNEL_ID;
    }
}
