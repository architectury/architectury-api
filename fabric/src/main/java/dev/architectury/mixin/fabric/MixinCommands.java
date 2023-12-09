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
import com.mojang.brigadier.context.ContextChain;
import dev.architectury.event.events.common.CommandPerformEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Commands.class)
public class MixinCommands {
    @ModifyVariable(method = "finishParsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;validateParseResults(Lcom/mojang/brigadier/ParseResults;)V"), argsOnly = true)
    private static ParseResults<CommandSourceStack> finishParsing(ParseResults<CommandSourceStack> results) {
        var event = new CommandPerformEvent(results, null);
        if (CommandPerformEvent.EVENT.invoker().act(event).isPresent()) {
            if (event.getThrowable() != null) {
                Throwables.throwIfUnchecked(event.getThrowable());
            }
            return null;
        }
        return event.getResults();
    }
    
    @Inject(method = "finishParsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/Commands;validateParseResults(Lcom/mojang/brigadier/ParseResults;)V"), cancellable = true)
    private static void finishParsing(ParseResults<CommandSourceStack> results, String command, CommandSourceStack stack, CallbackInfoReturnable<ContextChain<CommandSourceStack>> cir) {
        if (results == null) cir.setReturnValue(null);
    }
}
