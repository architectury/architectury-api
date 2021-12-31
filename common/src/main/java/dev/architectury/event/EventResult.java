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

package dev.architectury.event;

import net.minecraft.world.InteractionResult;
import org.apache.commons.lang3.BooleanUtils;

/**
 * A result from an event, determines if the event should continue to other listeners,
 * and determines the outcome of the event.
 *
 * @see #pass()
 * @see #interrupt(Boolean)
 * @see CompoundEventResult
 */
public final class EventResult {
    private static final EventResult TRUE = new EventResult(true, true);
    private static final EventResult STOP = new EventResult(true, null);
    private static final EventResult PASS = new EventResult(false, null);
    private static final EventResult FALSE = new EventResult(true, false);
    
    /**
     * Passes the event to other listeners, and does not set an outcome of the event.
     *
     * @return an event that passes the event to other listeners
     */
    public static EventResult pass() {
        return PASS;
    }
    
    /**
     * Interrupts the event and stops it from being passed on to other listeners,
     * may or may not set an outcome of the event.
     *
     * @param value the outcome of the event, passing {@code null} here means the default outcome,
     *              which often means falling back to vanilla logic
     * @return an event that interrupts the event
     */
    public static EventResult interrupt(Boolean value) {
        if (value == null) return STOP;
        if (value) return TRUE;
        return FALSE;
    }
    
    /**
     * Interrupts the event and stops it from being passed on to other listeners,
     * and denotes the {@code true} outcome.
     *
     * @return an event that interrupts the event
     */
    public static EventResult interruptTrue() {
        return TRUE;
    }
    
    /**
     * Interrupts the event and stops it from being passed on to other listeners,
     * and does not set an outcome.
     *
     * @return an event that interrupts the event
     */
    public static EventResult interruptDefault() {
        return STOP;
    }
    
    /**
     * Interrupts the event and stops it from being passed on to other listeners,
     * and denotes the {@code false} outcome.
     *
     * @return an event that interrupts the event
     */
    public static EventResult interruptFalse() {
        return FALSE;
    }
    
    private final boolean interruptsFurtherEvaluation;
    
    private final Boolean value;
    
    EventResult(boolean interruptsFurtherEvaluation, Boolean value) {
        this.interruptsFurtherEvaluation = interruptsFurtherEvaluation;
        this.value = value;
    }
    
    /**
     * Returns whether this result interrupts the evaluation of other listeners.
     *
     * @return whether this result interrupts the evaluation of other listeners
     */
    public boolean interruptsFurtherEvaluation() {
        return interruptsFurtherEvaluation;
    }
    
    /**
     * Returns the outcome of the result, an passing result will never have an outcome.
     *
     * @return the outcome of the result, returns {@code null} if fallback
     */
    public Boolean value() {
        return value;
    }
    
    /**
     * Returns whether the result does not contain an outcome, may be {@code false} only
     * if the event is deterministic.
     *
     * @return whether the result does not contain an outcome
     */
    public boolean isEmpty() {
        return value == null;
    }
    
    /**
     * Returns whether the result contains an outcome, may be {@code true} only
     * if the event is deterministic.
     *
     * @return whether the result contains an outcome
     */
    public boolean isPresent() {
        return value != null;
    }
    
    /**
     * Returns whether the result contains a {@code true} outcome
     *
     * @return whether the result contains a {@code true} outcome
     */
    public boolean isTrue() {
        return BooleanUtils.isTrue(value);
    }
    
    /**
     * Returns whether the result contains a {@code false} outcome
     *
     * @return whether the result contains a {@code false} outcome
     */
    public boolean isFalse() {
        return BooleanUtils.isFalse(value);
    }
    
    /**
     * Returns the Minecraft-facing result, however ignores {@link #interruptsFurtherEvaluation()}.
     *
     * @return the Minecraft-facing result
     */
    public InteractionResult asMinecraft() {
        if (isPresent()) {
            return value() ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        }
        return InteractionResult.PASS;
    }
}
