package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;

public final class PacketID {
    private final SimpleNetworkManager manager;
    private final ResourceLocation id;
    private final NetworkManager.Side side;
    
    PacketID(SimpleNetworkManager h, ResourceLocation i, NetworkManager.Side s) {
        manager = h;
        id = i;
        side = s;
    }
    
    public SimpleNetworkManager getManager() {
        return manager;
    }
    
    public ResourceLocation getId() {
        return id;
    }
    
    public NetworkManager.Side getSide() {
        return side;
    }
}
