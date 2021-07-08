package me.shedaniel.architectury.test.networking;

import me.shedaniel.architectury.networking.simple.PacketID;
import me.shedaniel.architectury.networking.simple.SimpleNetworkManager;
import me.shedaniel.architectury.test.TestMod;

public interface TestModNet {
    SimpleNetworkManager NET = SimpleNetworkManager.create(TestMod.MOD_ID);
    
    // An example Client to Server packet
    PacketID BUTTON_CLICKED = NET.registerC2S("button_clicked", ButtonClickedPacket::new);
    
    // An example Server to Client packet
    PacketID SYNC_DATA = NET.registerS2C("sync_data", SyncDataPacket::new);
    
    static void initialize() {
    }
}
