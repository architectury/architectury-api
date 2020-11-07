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

package me.shedaniel.architectury.mixin.fabric;

import com.google.common.base.Throwables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.shedaniel.architectury.event.events.CommandPerformEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public class MixinCommands {
    @Redirect(method = "performCommand",
              at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/CommandDispatcher;execute(Lcom/mojang/brigadier/StringReader;Ljava/lang/Object;)I", remap = false))
    private int performCommand(CommandDispatcher<CommandSourceStack> dispatcher, StringReader input, Object source) throws CommandSyntaxException {
        CommandSourceStack stack = (CommandSourceStack) source;
        ParseResults<CommandSourceStack> parse = dispatcher.parse(input, stack);
        CommandPerformEvent event = new CommandPerformEvent(parse, null);
        if (CommandPerformEvent.EVENT.invoker().act(event) != InteractionResult.PASS) {
            if (event.getThrowable() != null) {
                Throwables.throwIfUnchecked(event.getThrowable());
            }
            return 1;
        }
        return dispatcher.execute(event.getResults());
    }
}
