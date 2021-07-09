package me.shedaniel.architectury.networking.simple;

import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.Env;
import net.minecraft.resources.ResourceLocation;

/**
 * A simple wrapper for <{@link NetworkManager } to make it easier to register packets and send them to client/server
 *
 * @author LatvianModder
 */
public class SimpleNetworkManager {
    public static SimpleNetworkManager create(String namespace) {
        return new SimpleNetworkManager(namespace);
    }
    
    public final String namespace;
    
    private SimpleNetworkManager(String n) {
        namespace = n;
    }
    
    public PacketID registerS2C(String id, PacketDecoder<BaseS2CPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(namespace, id), NetworkManager.s2c());
        
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
            NetworkManager.registerReceiver(NetworkManager.s2c(), packetID.getId(), receiver);
        }
        
        return packetID;
    }
    
    public PacketID registerC2S(String id, PacketDecoder<BaseC2SPacket> decoder) {
        PacketID packetID = new PacketID(this, new ResourceLocation(namespace, id), NetworkManager.c2s());
        NetworkManager.NetworkReceiver receiver = decoder.createReceiver();
        NetworkManager.registerReceiver(NetworkManager.c2s(), packetID.getId(), receiver);
        return packetID;
    }
}
