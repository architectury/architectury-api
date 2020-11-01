package me.shedaniel.architectury.utils.fabric;

import me.shedaniel.architectury.event.events.LifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.utils.GameInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class GameInstanceImpl implements GameInstance.Impl {
    private static MinecraftServer server = null;
    
    @Override
    public MinecraftServer getServer() {
        MinecraftServer server = null;
        if (GameInstanceImpl.server != null) server = GameInstanceImpl.server;
        if (Platform.getEnv() == EnvType.CLIENT) {
            server = getServerFromClient();
        }
        return server;
    }
    
    public static void init() {
        LifecycleEvent.SERVER_STARTING.register(server -> GameInstanceImpl.server = server);
        LifecycleEvent.SERVER_STOPPED.register(server -> GameInstanceImpl.server = null);
    }
    
    @Environment(EnvType.CLIENT)
    private static MinecraftServer getServerFromClient() {
        return Minecraft.getInstance().getSingleplayerServer();
    }
}
