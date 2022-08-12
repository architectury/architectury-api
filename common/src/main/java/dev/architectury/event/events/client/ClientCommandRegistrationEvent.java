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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface ClientCommandRegistrationEvent {
    /**
     * @see ClientCommandRegistrationEvent#register(CommandDispatcher)
     */
    Event<ClientCommandRegistrationEvent> EVENT = EventFactory.createLoop();
    
    /**
     * This event is invoked after the client initializes.
     * Equivalent to Forge's {@code RegisterClientCommandsEvent} and Fabric's {@code ClientCommandManager}.
     *
     * @param dispatcher The command dispatcher to register commands to.
     */
    void register(CommandDispatcher<ClientCommandSourceStack> dispatcher);
    
    static LiteralArgumentBuilder<ClientCommandSourceStack> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }
    
    static <T> RequiredArgumentBuilder<ClientCommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
    
    interface ClientCommandSourceStack extends SharedSuggestionProvider {
        void arch$sendSuccess(Component message, boolean broadcastToAdmins);
        
        void arch$sendFailure(Component message);
        
        LocalPlayer arch$getPlayer();
        
        Vec3 arch$getPosition();
        
        Vec2 arch$getRotation();
        
        ClientLevel arch$getLevel();
    }
}
