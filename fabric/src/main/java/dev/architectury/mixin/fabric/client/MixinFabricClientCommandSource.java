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

import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FabricClientCommandSource.class)
public interface MixinFabricClientCommandSource extends ClientCommandRegistrationEvent.ClientCommandSourceStack {
    @Override
    default void arch$sendSuccess(Component message, boolean broadcastToAdmins) {
        ((FabricClientCommandSource) this).sendFeedback(message);
    }
    
    @Override
    default void arch$sendFailure(Component message) {
        ((FabricClientCommandSource) this).sendError(message);
    }
    
    @Override
    default LocalPlayer arch$getPlayer() {
        return ((FabricClientCommandSource) this).getPlayer();
    }
    
    @Override
    default Vec3 arch$getPosition() {
        return ((FabricClientCommandSource) this).getPosition();
    }
    
    @Override
    default Vec2 arch$getRotation() {
        return ((FabricClientCommandSource) this).getRotation();
    }
    
    @Override
    default ClientLevel arch$getLevel() {
        return ((FabricClientCommandSource) this).getWorld();
    }
}
