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
import dev.architectury.impl.fabric.EventChatDecorator;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Unique
    private ChatType chatType;
    @Unique
    private ChatSender chatSender;
    
    @Inject(method = "handlePlayerChat", at = @At(value = "HEAD"))
    private void handleChatPre(ChatType chatType, Component component, ChatSender chatSender, CallbackInfo ci) {
        this.chatType = chatType;
        this.chatSender = chatSender;
    }
    
    @ModifyVariable(method = "handlePlayerChat", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Component modifyMessage(Component message) {
        if (chatType == null) {
            chatType = null;
            chatSender = null;
            return message;
        }
        var process = ClientChatEvent.RECEIVED.invoker().process(chatType, message, chatSender);
        if (process.isPresent()) {
            if (process.isFalse())
                return EventChatDecorator.CANCELLING_COMPONENT;
            if (process.object() != null)
                return process.object();
        }
        return message;
    }
    
    @Inject(method = "handlePlayerChat", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;isBlocked(Ljava/util/UUID;)Z", ordinal = 0),
            cancellable = true)
    private void handleChat(ChatType chatType, Component component, ChatSender chatSender, CallbackInfo ci) {
        if (EventChatDecorator.CANCELLING_COMPONENT.equals(component)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "handleSystemChat", at = @At(value = "HEAD"))
    private void handleSystemChatPre(ChatType chatType, Component component, CallbackInfo ci) {
        this.chatType = chatType;
    }
    
    @ModifyVariable(method = "handleSystemChat", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Component modifySystemMessage(Component message) {
        if (chatType == null) {
            chatType = null;
            return message;
        }
        var process = ClientChatEvent.RECEIVED.invoker().process(chatType, message, null);
        if (process.isPresent()) {
            if (process.isFalse())
                return EventChatDecorator.CANCELLING_COMPONENT;
            if (process.object() != null)
                return process.object();
        }
        return message;
    }
    
    @Inject(method = "handleSystemChat", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Options;hideMatchedNames()Lnet/minecraft/client/OptionInstance;"),
            cancellable = true)
    private void handleChat(ChatType chatType, Component component, CallbackInfo ci) {
        if (EventChatDecorator.CANCELLING_COMPONENT.equals(component)) {
            ci.cancel();
        }
    }
}
