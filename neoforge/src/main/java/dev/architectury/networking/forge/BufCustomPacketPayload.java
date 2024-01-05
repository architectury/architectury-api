package dev.architectury.networking.forge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class BufCustomPacketPayload implements CustomPacketPayload {
    
    private final FriendlyByteBuf buf;
    
    public BufCustomPacketPayload(FriendlyByteBuf buf) {
        this.buf = buf;
    }
    @Override
    public void write(FriendlyByteBuf arg) {
        arg.writeBytes(buf);
    }
    
    @Override
    public ResourceLocation id() {
        return new ResourceLocation("architectury:network");
    }

    public FriendlyByteBuf getBuf() {
        return buf;
    }
}
