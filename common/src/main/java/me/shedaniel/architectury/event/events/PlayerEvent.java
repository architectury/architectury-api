package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;

public interface PlayerEvent {
    Event<PlayerJoin> PLAYER_JOIN = EventFactory.createLoop(PlayerJoin.class);
    Event<PlayerQuit> PLAYER_QUIT = EventFactory.createLoop(PlayerQuit.class);
    Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop(PlayerRespawn.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerJoin> CLIENT_PLAYER_JOIN = EventFactory.createLoop(ClientPlayerJoin.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerQuit> CLIENT_PLAYER_QUIT = EventFactory.createLoop(ClientPlayerQuit.class);
    @Environment(EnvType.CLIENT) Event<ClientPlayerRespawn> CLIENT_PLAYER_RESPAWN = EventFactory.createLoop(ClientPlayerRespawn.class);
    
    interface PlayerJoin {
        void join(ServerPlayer player);
    }
    
    interface PlayerQuit {
        void quit(ServerPlayer player);
    }
    
    interface PlayerRespawn {
        void respawn(ServerPlayer newPlayer, boolean conqueredEnd);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerJoin {
        void join(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerQuit {
        void quit(LocalPlayer player);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientPlayerRespawn {
        void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer);
    }
}
