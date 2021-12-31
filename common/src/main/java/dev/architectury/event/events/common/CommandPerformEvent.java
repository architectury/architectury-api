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

package dev.architectury.event.events.common;

import com.mojang.brigadier.ParseResults;
import dev.architectury.event.Event;
import dev.architectury.event.EventActor;
import dev.architectury.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

/**
 * This event is invoked whenever a command is issued.
 * The {@link ParseResults} can be modified and even a custom {@link Throwable} can be used to tell the source of failure.
 *
 * <p>A command fails when any other result than {@link net.minecraft.world.InteractionResult#PASS} is returned.
 * When PASS is used, the {@link CommandPerformEvent#getResults()} is used for execution.
 *
 * <p> Equivalent to Forge's {@code CommandEvent} event.
 */
public class CommandPerformEvent {
    public static final Event<EventActor<CommandPerformEvent>> EVENT = EventFactory.createEventActorLoop();
    
    private ParseResults<CommandSourceStack> results;
    @Nullable
    private Throwable throwable;
    
    /**
     * @param results   The initial used parsed results.
     * @param throwable The initial used throwable.
     */
    public CommandPerformEvent(ParseResults<CommandSourceStack> results, @Nullable Throwable throwable) {
        this.results = results;
        this.throwable = throwable;
    }
    
    /**
     * @return The parsed results for the issued command.
     */
    public ParseResults<CommandSourceStack> getResults() {
        return results;
    }
    
    /**
     * @param results The new results the command should use.
     */
    public void setResults(ParseResults<CommandSourceStack> results) {
        this.results = results;
    }
    
    /**
     * @return An throwable to be used as why the command has failed.
     */
    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }
    
    /**
     * @param throwable The throwable used when the command has failed.
     */
    public void setThrowable(@Nullable Throwable throwable) {
        this.throwable = throwable;
    }
}
