package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class MixinPlayerList {
    @Inject(method = "placeNewPlayer", at = @At("RETURN"))
    private void placeNewPlayer(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvent.PLAYER_JOIN.invoker().join(serverPlayer);
    }
    
    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvent.PLAYER_QUIT.invoker().quit(serverPlayer);
    }
    
    @Inject(method = "respawn", at = @At("RETURN"))
    private void respawn(ServerPlayer serverPlayer, boolean bl, CallbackInfoReturnable<ServerPlayer> cir) {
        PlayerEvent.PLAYER_RESPAWN.invoker().respawn(cir.getReturnValue(), bl);
    }
}
