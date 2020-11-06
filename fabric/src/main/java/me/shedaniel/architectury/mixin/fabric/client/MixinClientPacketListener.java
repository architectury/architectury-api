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

import me.shedaniel.architectury.event.events.ChatEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
    private void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        PlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(minecraft.player);
    }
    
    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawnPre(ClientboundRespawnPacket packet, CallbackInfo ci) {
        this.tmpPlayer = minecraft.player;
    }
    
    @Inject(method = "handleRespawn", at = @At(value = "INVOKE",
                                               target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        PlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(tmpPlayer, minecraft.player);
        this.tmpPlayer = null;
    }
    
    @Inject(method = "handleChat", at = @At(value = "INVOKE",
                                            target = "Lnet/minecraft/client/gui/Gui;handleChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V"),
            cancellable = true)
    private void handleChat(ClientboundChatPacket packet, CallbackInfo ci) {
        InteractionResultHolder<Component> process = ChatEvent.CLIENT_RECEIVED.invoker().process(packet.getType(), packet.getMessage(), packet.getSender());
        if (process.getResult() == InteractionResult.FAIL)
            ci.cancel();
        else if (process.getObject() != null && !process.getObject().equals(packet.getMessage())) {
            this.minecraft.gui.handleChat(packet.getType(), packet.getMessage(), packet.getSender());
            ci.cancel();
        }
    }
}
