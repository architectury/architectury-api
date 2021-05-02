/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.ChatEvent;
import me.shedaniel.architectury.impl.fabric.ChatComponentImpl;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import org.apache.commons.lang3.StringUtils;
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
    @Shadow public ServerPlayer player;
    
    @Shadow @Final private MinecraftServer server;
    
    @Shadow private int chatSpamTickCount;
    
    @Shadow
    public abstract void disconnect(Component component);
    
    @Inject(method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"),
            cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void handleChat(TextFilter.FilteredText message, CallbackInfo ci, String string, Component component, Component component2) {
        ChatComponentImpl chatComponent = new ChatComponentImpl(component2, component);
        InteractionResult process = ChatEvent.SERVER.invoker().process(this.player, message, chatComponent);
        if (process == InteractionResult.FAIL)
            ci.cancel();
        else if (!Objects.equals(chatComponent.getRaw(), component2) || !Objects.equals(chatComponent.getFiltered(), component)) {
            this.server.getPlayerList().broadcastMessage(chatComponent.getRaw(), (serverPlayer) -> {
                return this.player.shouldFilterMessageTo(serverPlayer) ? chatComponent.getFiltered() : chatComponent.getRaw();
            }, ChatType.CHAT, this.player.getUUID());
            
            this.chatSpamTickCount += 20;
            if (this.chatSpamTickCount > 200 && !this.server.getPlayerList().isOp(this.player.getGameProfile())) {
                this.disconnect(new TranslatableComponent("disconnect.spam"));
            }
            ci.cancel();
        }
    }
}
