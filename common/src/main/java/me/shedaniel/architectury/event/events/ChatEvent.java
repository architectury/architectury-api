/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResultHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ChatEvent {
    /**
     * Invoked when client tries to send a message, equivalent to forge's {@code ClientChatEvent}.
     */
    @Environment(EnvType.CLIENT) Event<Client> CLIENT = EventFactory.createInteractionResultHolder(Client.class);
    /**
     * Invoked when client receives a message, equivalent to forge's {@code ClientChatReceivedEvent}.
     */
    @Environment(EnvType.CLIENT) Event<ClientReceived> CLIENT_RECEIVED = EventFactory.createInteractionResultHolder(ClientReceived.class);
    /**
     * Invoked when server receives a message, equivalent to forge's {@code ServerChatEvent}.
     */
    Event<Server> SERVER = EventFactory.createInteractionResultHolder(Server.class);
    
    @Environment(EnvType.CLIENT)
    interface Client {
        @NotNull
        InteractionResultHolder<String> process(String message);
    }
    
    @Environment(EnvType.CLIENT)
    interface ClientReceived {
        @NotNull
        InteractionResultHolder<Component> process(ChatType type, Component message, @Nullable UUID sender);
    }
    
    interface Server {
        @NotNull
        InteractionResultHolder<Component> process(ServerPlayer player, String message, Component component);
    }
}
