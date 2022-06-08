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

package dev.architectury.impl;

import dev.architectury.event.events.client.ClientChatEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
public class ChatProcessorImpl implements ClientChatEvent.ChatProcessor {
    private String message;
    @Nullable
    private Component component;
    
    public ChatProcessorImpl(String message, @Nullable Component component) {
        this.message = message;
        this.component = component;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    @Nullable
    public Component getComponent() {
        return component;
    }
    
    @Override
    public void setMessage(String message) {
        this.message = Objects.requireNonNull(message);
    }
    
    @Override
    public void setComponent(@Nullable Component component) {
        this.component = component;
    }
}
