/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.mixin.fabric.client;

import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientRecipeUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateRecipesPacket;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    @Final
    private RecipeManager recipeManager;
    @Shadow
    private ClientLevel level;
    @Unique
    private LocalPlayer tmpPlayer;
    
    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;broadcastOptions()V"))
    private void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.invoker().join(minecraft.player);
    }
    
    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawnPre(ClientboundRespawnPacket packet, CallbackInfo ci) {
        this.tmpPlayer = minecraft.player;
    }
    
    @Inject(method = "handleRespawn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/ClientLevel;addPlayer(ILnet/minecraft/client/player/AbstractClientPlayer;)V"))
    private void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.invoker().respawn(tmpPlayer, minecraft.player);
        this.tmpPlayer = null;
    }
    
    @Inject(method = "handleSystemChat", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;handleSystemChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;)V"),
            cancellable = true)
    private void handleChat(ClientboundSystemChatPacket packet, CallbackInfo ci) {
        var registry = this.level.registryAccess().registryOrThrow(Registry.CHAT_TYPE_REGISTRY);
        var chatType = packet.resolveType(registry);
        var process = ClientChatEvent.RECEIVED.invoker().process(chatType, packet.content(), null);
        if (process.isEmpty()) return;
        if (process.isFalse()) {
            ci.cancel();
        } else if (process.object() != null && !process.object().equals(packet.content())) {
            this.minecraft.gui.handleSystemChat(chatType, packet.content());
            ci.cancel();
        }
    }
    
    @Inject(method = "handlePlayerChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/ChatSender;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;handlePlayerChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatSender;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleChat(ChatType chatType, PlayerChatMessage playerChatMessage, ChatSender sender, CallbackInfo ci, boolean showSigned, PlayerInfo playerInfo, Component component) {
        var registry = this.level.registryAccess().registryOrThrow(Registry.CHAT_TYPE_REGISTRY);
        var process = ClientChatEvent.RECEIVED.invoker().process(chatType, component, sender);
        if (process.isEmpty()) return;
        if (process.isFalse()) {
            ci.cancel();
        } else if (process.object() != null && !process.object().equals(component)) {
            this.minecraft.gui.handlePlayerChat(chatType, component, sender);
            ci.cancel();
        }
    }
    
    @Inject(method = "handleUpdateRecipes", at = @At("RETURN"))
    private void handleUpdateRecipes(ClientboundUpdateRecipesPacket clientboundUpdateRecipesPacket, CallbackInfo ci) {
        ClientRecipeUpdateEvent.EVENT.invoker().update(recipeManager);
    }
}
