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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ContextChain;
import dev.architectury.event.events.common.CommandPerformEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.ParseBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(Commands.class)
public class MixinCommands {
    /**
     * Since 26.4, commands are parsed against a detached {@link net.minecraft.commands.ParseContext}
     * instead of the {@link CommandSourceStack}. To keep {@link CommandPerformEvent} behaving as before,
     * listeners get a copy of the parse results whose source is the actual command sender.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @WrapOperation(method = "finishParsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/commands/ParseBox$Result;unpack()Lcom/mojang/brigadier/context/ContextChain;"))
    private static ContextChain<CommandSourceStack> finishParsing(ParseBox.Result command, Operation<ContextChain<CommandSourceStack>> original,
                                                                  @Local(argsOnly = true) CommandSourceStack sender) {
        ParseResults<?> parsed = command.parseResults;
        CommandContextBuilder<CommandSourceStack> context = ((CommandContextBuilder) parsed.getContext().copy()).withSource(sender);
        ParseResults<CommandSourceStack> results = new ParseResults<>(context, parsed.getReader(), (Map) parsed.getExceptions());
        
        var event = new CommandPerformEvent(results, null);
        if (CommandPerformEvent.EVENT.invoker().act(event).isPresent()) {
            if (event.getThrowable() != null) {
                Throwables.throwIfUnchecked(event.getThrowable());
            }
            return null;
        }
        
        if (event.getResults() != results) {
            command = new ParseBox.Result(command.commandString(), (ParseResults) event.getResults());
        }
        
        return original.call(command);
    }
}
