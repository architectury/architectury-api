/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow private Minecraft minecraft;
    @Unique private LocalPlayer tmpPlayer;
    
    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;broadcastOptions()V"))
    private void handleLogin(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        PlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(minecraft.player);
    }
    
    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawnPre(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        this.tmpPlayer = minecraft.player;
    }
    
    @Inject(method = "handleRespawn", at = @At(value = "INVOKE",
                                               target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void handleRespawn(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        PlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(tmpPlayer, minecraft.player);
        this.tmpPlayer = null;
    }
}
