package me.shedaniel.architectury.networking.forge;

import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Set;
import java.util.function.Consumer;

import static me.shedaniel.architectury.networking.forge.NetworkManagerImpl.C2S;
import static me.shedaniel.architectury.networking.forge.NetworkManagerImpl.SYNC_IDS;

@OnlyIn(Dist.CLIENT)
public class ClientNetworkingManager {
    public static Consumer<NetworkManagerImpl> initClient() {
        NetworkManagerImpl.CHANNEL.addListener(NetworkManagerImpl.createPacketHandler(NetworkEvent.ServerCustomPayloadEvent.class, NetworkManagerImpl.S2C));
        MinecraftForge.EVENT_BUS.<ClientPlayerNetworkEvent.LoggedOutEvent>addListener(event -> NetworkManagerImpl.serverReceivables.clear());
        
        return impl -> impl.registerS2CReceiver(SYNC_IDS, (buffer, context) -> {
            Set<ResourceLocation> receivables = NetworkManagerImpl.serverReceivables;
            int size = buffer.readInt();
            receivables.clear();
            for (int i = 0; i < size; i++) {
                receivables.add(buffer.readResourceLocation());
            }
            NetworkManager.sendToServer(SYNC_IDS, NetworkManagerImpl.sendSyncPacket(C2S));
        });
    }
    
    public static PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}