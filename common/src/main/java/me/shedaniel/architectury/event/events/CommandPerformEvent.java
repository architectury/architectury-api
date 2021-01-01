/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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
