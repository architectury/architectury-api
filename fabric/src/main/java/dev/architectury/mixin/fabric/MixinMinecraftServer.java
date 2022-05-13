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

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.impl.fabric.EventChatDecorator;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {
    @Inject(method = "getChatDecorator", at = @At("RETURN"), cancellable = true)
    private void getChatDecorator(CallbackInfoReturnable<ChatDecorator> cir) {
        ChatDecorator parent = cir.getReturnValue();
        cir.setReturnValue(new EventChatDecorator(parent, (player, component) -> {
            CompoundEventResult<Component> result = ChatEvent.SERVER.invoker().process(player, component);
            if (result.isPresent()) {
                if (result.isFalse()) {
                    return EventChatDecorator.CANCELLING_COMPONENT;
                } else if (result.object() != null) {
                    return result.object();
                }
            }
            
            return component;
        }));
    }
}
