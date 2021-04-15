/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.event;

import net.minecraft.world.InteractionResultHolder;

/**
 * A result from an event, determines if the event should continue to other listeners,
 * determines the outcome of the event, and provides extra result for the outcome.
 *
 * @param <T> the type of the extra result
 * @see #pass()
 * @see #interrupt(Boolean, Object)
 * @see CompoundEventResult
 */
public class CompoundEventResult<T> {
    private static final CompoundEventResult<?> PASS = new CompoundEventResult<>(EventResult.pass(), null);
    private final EventResult result;
    private final T object;
    
    /**
     * Passes the event to other listeners, and does not set an outcome of the event.
     *
     * @param <T> the type of the extra result
     * @return an event that passes the event to other listeners
     */
    public static <T> CompoundEventResult<T> pass() {
        return (CompoundEventResult<T>) PASS;
    }
    
    /**
     * Interrupts the event and stops it from being passed on to other listeners,
     * may or may not set an outcome and extra data of the event.
     *
     * @param value  the outcome of the event, passing {@code null} here means the default outcome,
     *               which often means falling back to vanilla logic
     * @param object the extra data of the result, this usually is the returning value of the event
     * @return an event that interrupts the event
     */
    public static <T> CompoundEventResult<T> interrupt(Boolean value, T object) {
        return new CompoundEventResult<>(EventResult.interrupt(value), object);
    }
    
    private CompoundEventResult(EventResult result, T object) {
        this.result = result;
        this.object = object;
    }
    
    /**
     * Returns whether this result interrupts the evaluation of other listeners.
     *
     * @return whether this result interrupts the evaluation of other listeners
     */
    public boolean interruptsFurtherEvaluation() {
        return result.interruptsFurtherEvaluation();
    }
    
    /**
     * Returns the outcome of the result, an passing result will never have an outcome.
     *
     * @return the outcome of the result, returns {@code null} if fallback
     */
    public Boolean value() {
        return result.value();
    }
    
    /**
     * Returns the {@link EventResult} view of the result, this returns the same values as
     * {@link #interruptsFurtherEvaluation()} and {@link #value()}.
     *
     * @return the {@link EventResult} view of the result.
     */
    public EventResult result() {
        return result;
    }
    
    /**
     * Returns the extra data of the result, an passing result will never contain any extra data.
     *
     * @return the extra data of the result, returns {@code null} if passing
     */
    public T object() {
        return object;
    }
    
    /**
     * Returns the Minecraft-facing result, however ignores {@link #interruptsFurtherEvaluation()}.
     *
     * @return the Minecraft-facing result
     */
    public InteractionResultHolder<T> asMinecraft() {
        if (value() != null) {
            return value() ? InteractionResultHolder.success(object()) : InteractionResultHolder.fail(object());
        }
        return InteractionResultHolder.pass(object());
    }
}
