package me.shedaniel.architectury.utils.forge;

import me.shedaniel.architectury.utils.GameInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class GameInstanceImpl implements GameInstance.Impl {
    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
