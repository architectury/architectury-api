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
import dev.architectury.event.EventResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public interface ClientChatEvent {
    /**
     * @see Send#send(String, Component)
     */
    Event<Send> SEND = EventFactory.createEventResult();
    /**
     * @see Received#process(ChatType.Bound, Component)
     */
    Event<Received> RECEIVED = EventFactory.createCompoundEventResult();
    
    @Environment(EnvType.CLIENT)
    interface Send {
        /**
         * Event to cancel clients sending the chat message.
         * Equivalent to Forge's {@code ClientChatEvent} event.
         *
         * @param message   The chat message.
         * @param component The chat component that was decorated, can be {@code null}.
         * @return A {@link EventResult} determining the outcome of the event,
         * if an outcome is set, the message and component will be ignored.
         */
        EventResult send(String message, @Nullable Component component);
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
         * @return A {@link CompoundEventResult} determining the outcome of the event,
         * if an outcome is set, the received message is overridden.
         */
        CompoundEventResult<Component> process(ChatType.Bound type, Component message);
    }
}
