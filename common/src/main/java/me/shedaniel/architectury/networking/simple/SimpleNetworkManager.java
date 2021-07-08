package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;

public class SimpleNetworkManager {
    public static SimpleNetworkManager create(String modid) {
        return new SimpleNetworkManager(modid);
    }
    
    public final String modid;
    
    private SimpleNetworkManager(String m) {
        modid = m;
    }
    
    public PacketID registerS2C(String id, PacketDecoder<BaseS2CPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(modid, id), NetworkManager.s2c());
        
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
            NetworkManager.registerReceiver(NetworkManager.s2c(), packetID.getId(), receiver);
        }
        
        return packetID;
    }
    
    public PacketID registerC2S(String id, PacketDecoder<BaseC2SPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(modid, id), NetworkManager.c2s());
        NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
        NetworkManager.registerReceiver(NetworkManager.c2s(), packetID.getId(), receiver);
        return packetID;
    }
}
