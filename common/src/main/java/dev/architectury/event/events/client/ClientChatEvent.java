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

package dev.architectury.event.events.client;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ChatSender;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Environment(EnvType.CLIENT)
public interface ClientChatEvent {
    /**
     * @see Process#process(String)
     */
    Event<Process> PROCESS = EventFactory.createCompoundEventResult();
    /**
     * @see Received#process(ChatType, Component, UUID)
     */
    Event<Received> RECEIVED = EventFactory.createCompoundEventResult();
    
    @Environment(EnvType.CLIENT)
    interface Process {
        /**
         * Event to modify the chat message a clients sends.
         * Equivalent to Forge's {@code ClientChatEvent} event.
         *
         * @param message The raw chat message the client wants to send.
         * @return A {@link CompoundEventResult} determining the outcome of the event,
         * if an outcome is set, the sent message is overridden.
         */
        CompoundEventResult<String> process(String message);
    }
    
    @Environment(EnvType.CLIENT)
    interface Received {
        /**
         * Event to intercept the receiving of an chat message.
         * Invoked as soon as the client receives the chat message packet.
         * Equivalent to Forge's {@code ClientChatReceivedEvent} event.
         *
         * @param type    Where was the message emitted from.
         * @param message The chat message.
         * @param sender  The packet sender. Can be {@code null}, but probably is the sending player UUID or null for system messages.
         * @return A {@link CompoundEventResult} determining the outcome of the event,
         * if an outcome is set, the received message is overridden.
         */
        CompoundEventResult<Component> process(ChatType type, Component message, @Nullable ChatSender sender);
    }
}
