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

import com.google.common.base.Throwables;
import com.mojang.brigadier.ParseResults;
import dev.architectury.event.events.common.CommandPerformEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Commands.class)
public class MixinCommands {
    @ModifyVariable(method = "performCommand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;validateParseResults(Lcom/mojang/brigadier/ParseResults;)V", remap = false), argsOnly = true)
    private ParseResults<CommandSourceStack> performCommand(ParseResults<CommandSourceStack> results) {
        var event = new CommandPerformEvent(results, null);
        if (CommandPerformEvent.EVENT.invoker().act(event).isPresent()) {
            if (event.getThrowable() != null) {
                Throwables.throwIfUnchecked(event.getThrowable());
            }
            return null;
        }
        return event.getResults();
    }
    
    @Inject(method = "performCommand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;validateParseResults(Lcom/mojang/brigadier/ParseResults;)V", remap = false), cancellable = true)
    private void performCommand(ParseResults<CommandSourceStack> results, String command, CallbackInfo ci) {
        if (results == null) ci.cancel();
    }
}
