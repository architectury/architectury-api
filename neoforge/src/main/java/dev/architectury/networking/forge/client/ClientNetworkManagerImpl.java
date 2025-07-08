package dev.architectury.networking.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;

public class ClientNetworkManagerImpl {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }
    
    public static RegistryAccess getClientRegistryAccess() {
        if (Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.registryAccess();
        } else if (Minecraft.getInstance().getConnection() != null) {
            return Minecraft.getInstance().getConnection().registryAccess();
        } else if (Minecraft.getInstance().gameMode != null) {
            // Sometimes the packet is sent way too fast and is between the connection and the level, better safe than sorry
            return Minecraft.getInstance().gameMode.connection.registryAccess();
        }
        
        // Fail-safe
        return RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }
}
