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

package dev.architectury.mixin.forge.client;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandSourceStack.class)
public abstract class MixinCommandSourceStack implements ClientCommandRegistrationEvent.ClientCommandSourceStack {
    @Override
    public void arch$sendSuccess(Component message, boolean broadcastToAdmins) {
        ((CommandSourceStack) (Object) this).sendSuccess(message, broadcastToAdmins);
    }
    
    @Override
    public void arch$sendFailure(Component message) {
        ((CommandSourceStack) (Object) this).sendFailure(message);
    }
    
    @Override
    public LocalPlayer arch$getPlayer() {
        try {
            return (LocalPlayer) ((CommandSourceStack) (Object) this).getEntityOrException();
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public Vec3 arch$getPosition() {
        return ((CommandSourceStack) (Object) this).getPosition();
    }
    
    @Override
    public Vec2 arch$getRotation() {
        return ((CommandSourceStack) (Object) this).getRotation();
    }
    
    @Override
    public ClientLevel arch$getLevel() {
        return (ClientLevel) ((CommandSourceStack) (Object) this).getUnsidedLevel();
    }
}
