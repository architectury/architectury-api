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

package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.impl.fabric.ChatComponentImpl;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {
    @Shadow
    public ServerPlayer player;
    
    @Shadow
    @Final
    private MinecraftServer server;
    
    @Shadow
    protected abstract void detectRateSpam();
    
    @Inject(method = "handleChat(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;Lnet/minecraft/server/network/TextFilter$FilteredText;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/network/chat/SignedMessage;Lnet/minecraft/server/network/TextFilter$FilteredText;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/resources/ResourceKey;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleChat(ServerboundChatPacket packet, TextFilter.FilteredText message, CallbackInfo ci) {
        MutableComponent raw = Component.literal(message.getRaw());
        MutableComponent filtered = Component.literal(message.getFiltered());
        var chatComponent = new ChatComponentImpl(raw, filtered);
        var process = ChatEvent.SERVER.invoker().process(this.player, message, chatComponent);
        if (process.isEmpty()) return;
        if (process.isFalse()) {
            ci.cancel();
        } else if (!Objects.equals(chatComponent.getRaw(), raw) || !Objects.equals(chatComponent.getFiltered(), filtered)) {
            MessageSignature messageSignature = packet.getSignature(this.player.getUUID());
            SignedMessage signedMessage = new SignedMessage(chatComponent.getRaw(), messageSignature);
            
            {
                SignedMessage filteredMessage;
                if (!chatComponent.getFiltered().getString().isEmpty()) {
                    filteredMessage = new SignedMessage(chatComponent.getFiltered(), MessageSignature.unsigned());
                } else {
                    filteredMessage = null;
                }
                
                this.server.getPlayerList().broadcastChatMessage(signedMessage, (serverPlayer2) -> {
                    return player.shouldFilterMessageTo(serverPlayer2) ? filteredMessage : signedMessage;
                }, player.asChatSender(), ChatType.CHAT);
            }
            
            this.detectRateSpam();
            ci.cancel();
        }
    }
}
