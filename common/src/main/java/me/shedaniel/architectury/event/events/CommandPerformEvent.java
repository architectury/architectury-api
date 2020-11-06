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

package me.shedaniel.architectury.event.events;

import com.mojang.brigadier.ParseResults;
import me.shedaniel.architectury.event.Actor;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandPerformEvent {
    /**
     * Invoked after server parses a command but before server executes it, equivalent to forge's {@code CommandEvent}.
     */
    public static final Event<Actor<CommandPerformEvent>> EVENT = EventFactory.createActorLoop(CommandPerformEvent.class);
    
    @NotNull
    private ParseResults<CommandSourceStack> results;
    @Nullable
    private Throwable throwable;
    
    public CommandPerformEvent(@NotNull ParseResults<CommandSourceStack> results, @Nullable Throwable throwable) {
        this.results = results;
        this.throwable = throwable;
    }
    
    @NotNull
    public ParseResults<CommandSourceStack> getResults() {
        return results;
    }
    
    public void setResults(@NotNull ParseResults<CommandSourceStack> results) {
        this.results = results;
    }
    
    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }
    
    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}
