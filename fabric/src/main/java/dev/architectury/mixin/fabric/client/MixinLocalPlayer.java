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
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientChatEvent;
import dev.architectury.impl.ChatProcessorImpl;
import dev.architectury.impl.fabric.EventChatDecorator;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSigner;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
    public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey) {
        super(clientLevel, gameProfile, profilePublicKey);
    }
    
    @Shadow
    protected abstract void sendChat(MessageSigner messageSigner, String string, @Nullable Component component);
    
    @Inject(method = "chat(Ljava/lang/String;Lnet/minecraft/network/chat/Component;)V", at = @At(value = "HEAD"), cancellable = true)
    private void chat(String message, Component component, CallbackInfo ci) {
        ChatProcessorImpl processor = new ChatProcessorImpl(message, component);
        EventResult process = ClientChatEvent.PROCESS.invoker().process(processor);
        if (process.isPresent()) {
            if (process.isFalse())
                ci.cancel();
            else {
                String processorMessage = processor.getMessage();
                Component processorComponent = processor.getComponent();
                
                if (!Objects.equals(processorMessage, message) || !Objects.equals(processorComponent, component)) {
                    MessageSigner messageSigner = MessageSigner.create(this.getUUID());
                    this.sendChat(messageSigner, processorMessage, processorComponent);
                    ci.cancel();
                }
            }
        }
    }
}
