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

package dev.architectury.impl.fabric;

import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class EventChatDecorator implements ChatDecorator {
    public static final Component CANCELLING_COMPONENT = Component.literal("THIS SHOULDN'T BE DISPLAYED, ARCHITECTURY SPECIFIC STRING DO NOT IMITATE THIS");
    private final ChatDecorator parent;
    private final ChatProcessor processor;
    
    public EventChatDecorator(ChatDecorator parent, ChatProcessor processor) {
        this.parent = parent;
        this.processor = processor;
    }
    
    @Override
    public Component decorate(@Nullable ServerPlayer player, Component component) {
        return processor.process(player, parent.decorate(player, component));
    }
    
    @Override
    public PlayerChatMessage decorate(@Nullable ServerPlayer player, Component component, MessageSignature signature, boolean signedPreview) {
        PlayerChatMessage message = parent.decorate(player, component, signature, signedPreview);
        Component newContent = processor.process(player, component);
        if (!newContent.equals(component)) {
            return !signedPreview ? PlayerChatMessage.signed(component, signature).withUnsignedContent(newContent) : PlayerChatMessage.signed(newContent, signature);
        }
        return message;
    }
    
    @Override
    public PlayerChatMessage decorate(@Nullable ServerPlayer player, PlayerChatMessage message) {
        PlayerChatMessage newMessage = parent.decorate(player, message.signedContent(), message.signature(), false);
        Component newContent = processor.process(player, message.signedContent());
        if (!newContent.equals(message.signedContent())) {
            return PlayerChatMessage.signed(message.signedContent(), message.signature()).withUnsignedContent(newContent);
        }
        return message;
    }
    
    @FunctionalInterface
    public interface ChatProcessor {
        Component process(@Nullable ServerPlayer player, Component component);
    }
}
