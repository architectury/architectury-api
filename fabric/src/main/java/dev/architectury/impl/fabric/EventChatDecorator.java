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

import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.ChatDecorator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EventChatDecorator implements ChatDecorator {
    public static final Component CANCELLING_COMPONENT = Component.literal("THIS SHOULDN'T BE DISPLAYED, ARCHITECTURY SPECIFIC STRING DO NOT IMITATE THIS");
    private final ChatDecorator parent;
    private final ChatProcessor processor;
    
    public EventChatDecorator(ChatDecorator parent, ChatProcessor processor) {
        this.parent = parent;
        this.processor = processor;
    }
    
    @Override
    public CompletableFuture<Component> decorate(@Nullable ServerPlayer player, Component component) {
        return parent.decorate(player, component).thenApply(c -> {
            return processor.process(player, FilteredText.fullyFiltered(c)).raw();
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return component;
        });
    }
    
    @Override
    public CompletableFuture<FilteredText<Component>> decorateFiltered(@Nullable ServerPlayer player, FilteredText<Component> message) {
        return parent.decorateFiltered(player, message).thenApply(newMessage -> {
            FilteredText<Component> newContent = processor.process(player, newMessage);
            if (!newContent.equals(newMessage)) {
                return newContent;
            }
            return newMessage;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return message;
        });
    }
    
    @Override
    public CompletableFuture<FilteredText<PlayerChatMessage>> decorateChat(@Nullable ServerPlayer player, FilteredText<Component> component, MessageSignature signature, boolean signedPreview) {
        return parent.decorateChat(player, component, signature, signedPreview).thenApply(message -> {
            FilteredText<Component> newComponent = processor.process(player, message.map(PlayerChatMessage::signedContent));
            FilteredText<PlayerChatMessage> newMessage = PlayerChatMessage.filteredSigned(component, newComponent, signature, signedPreview);
            if (!newMessage.equals(message)) {
                return newMessage;
            }
            return message;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return PlayerChatMessage.filteredSigned(component, component, signature, signedPreview);
        });
    }
    
    @FunctionalInterface
    public interface ChatProcessor {
        FilteredText<Component> process(@Nullable ServerPlayer player, FilteredText<Component> text);
    }
}
