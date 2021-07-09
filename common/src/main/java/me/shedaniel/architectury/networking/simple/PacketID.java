package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

/**
 * @author LatvianModder
 */
public final class PacketID {
    private final SimpleNetworkManager manager;
    private final ResourceLocation id;
    private final NetworkManager.Side side;
    
    PacketID(SimpleNetworkManager h, ResourceLocation i, NetworkManager.Side s) {
        manager = h;
        id = i;
        side = s;
    }
    
    /**
     * {@return the network manager that manages this packet ID}
     */
    public SimpleNetworkManager getManager() {
        return manager;
    }
    
    /**
     * {@return the ID of this packet}
     */
    public ResourceLocation getId() {
        return id;
    }
    
    public NetworkManager.Side getSide() {
        return side;
    }
    
    @Override
    public String toString() {
        return id.toString() + ":" + side.name().toLowerCase();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        PacketID packetID = (PacketID) o;
        return id.equals(packetID.id) && side == packetID.side;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, side);
    }
}
