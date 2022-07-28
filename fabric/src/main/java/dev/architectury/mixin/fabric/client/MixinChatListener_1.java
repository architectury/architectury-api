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
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;

@Mixin(targets = "net.minecraft.client.multiplayer.chat.ChatListener$1")
public class MixinChatListener_1 {
    @Shadow
    @Final
    Component val$decoratedMessage;
    @Unique
    private ThreadLocal<Component> cancelNext = new ThreadLocal<>();
    
    @ModifyArgs(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;processPlayerChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/multiplayer/PlayerInfo;ZLjava/time/Instant;)Z"))
    private void modifyMessage(Args args) {
        cancelNext.remove();
        ChatType.Bound boundChatType = args.get(0);
        Component message = args.get(2);
        var process = ClientChatEvent.RECEIVED.invoker().process(boundChatType, message);
        if (process.isPresent()) {
            if (process.isFalse()) {
                cancelNext.set(message);
            } else if (process.object() != null) {
                args.set(2, process.object());
            }
        }
    }
    
    @Inject(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;processPlayerChatMessage(Lnet/minecraft/network/chat/ChatType$Bound;Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/multiplayer/PlayerInfo;ZLjava/time/Instant;)Z"),
            cancellable = true)
    private void handleChatPre(CallbackInfoReturnable<Boolean> cir) {
        if (Objects.equals(cancelNext.get(), this.val$decoratedMessage)) {
            cir.setReturnValue(false);
        }
        
        cancelNext.remove();
    }
}
