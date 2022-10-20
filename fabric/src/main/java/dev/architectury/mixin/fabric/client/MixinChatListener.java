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

import com.mojang.authlib.GameProfile;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.event.events.client.ClientSystemMessageEvent;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(ChatListener.class)
public class MixinChatListener {
    @Unique
    ChatType.Bound boundChatType;
    @Unique
    private ThreadLocal<Component> cancelNextChat = new ThreadLocal<>();
    @Unique
    private ThreadLocal<Component> cancelNextSystem = new ThreadLocal<>();
    
    @Inject(method = "handlePlayerChatMessage", at = @At(value = "INVOKE", target = "Ljava/time/Instant;now()Ljava/time/Instant;"))
    private void handlePlayerChatMessage(PlayerChatMessage playerChatMessage, GameProfile gameProfile, ChatType.Bound bound, CallbackInfo ci) {
        this.boundChatType = bound;
    }
    
    @ModifyVariable(method = "handlePlayerChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/PlayerChatMessage;signature()Lnet/minecraft/network/chat/MessageSignature;"))
    private Component modifyMessage(Component value) {
        cancelNextChat.remove();
        var process = ClientChatEvent.RECEIVED.invoker().process(boundChatType, value);
        this.boundChatType = null;
        if (process.isPresent()) {
            if (process.isFalse()) {
                cancelNextChat.set(value);
            } else if (process.object() != null) {
                return process.object();
            }
        }
        
        return value;
    }
    
    @Inject(method = "handlePlayerChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleMessage(Lnet/minecraft/network/chat/MessageSignature;Ljava/util/function/BooleanSupplier;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleChatPre(PlayerChatMessage playerChatMessage, GameProfile gameProfile, ChatType.Bound bound, CallbackInfo ci, boolean onlyShowSecureChat, PlayerChatMessage filtered, Component component) {
        if (Objects.equals(cancelNextChat.get(), component)) {
            ci.cancel();
        }
        
        cancelNextChat.remove();
    }
    
    @ModifyArgs(method = "handleSystemMessage", at = @At(value = "HEAD"))
    private void modifySystemMessage(Args args) {
        cancelNextSystem.remove();
        Component message = args.get(0);
        var process = ClientSystemMessageEvent.RECEIVED.invoker().process(message);
        if (process.isPresent()) {
            if (process.isFalse()) {
                cancelNextSystem.set(message);
            } else if (process.object() != null) {
                args.set(0, process.object());
            }
        }
    }
    
    @Inject(method = "handleSystemMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;hideMatchedNames()Lnet/minecraft/client/OptionInstance;"),
            cancellable = true)
    private void handleSystemMessage(Component component, boolean bl, CallbackInfo ci) {
        if (Objects.equals(cancelNextSystem.get(), component)) {
            ci.cancel();
        }
        
        cancelNextSystem.remove();
    }
}
