package me.shedaniel.architectury.utils;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public final class GameInstance {
    @Populatable
    private static final Impl IMPL = null;
    
    @Environment(EnvType.CLIENT)
    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }
    
    @Environment(EnvType.SERVER)
    public static MinecraftServer getServer() {
        return IMPL.getServer();
    }
    
    public interface Impl {
        MinecraftServer getServer();
    }
    
    static {
        ArchitecturyPopulator.populate(GameInstance.class);
    }
}
