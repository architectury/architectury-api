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
import org.jetbrains.annotations.Nullable;

/**
 * The read-only outcome surface shared by {@link EventResult} and {@link CompoundEventResult}.
 * Implementing a common type lets listeners and bridge code inspect or forward a result without
 * needing to know which concrete result type produced it.
 *
 * @see EventResult
 * @see CompoundEventResult
 */
public interface EventResultHolder {
    /**
     * Returns whether this result interrupts the evaluation of further listeners.
     */
    boolean interruptsFurtherEvaluation();

    /**
     * Returns the boolean outcome of the result, or {@code null} when no outcome is set (fallback).
     */
    @Nullable
    Boolean value();

    /**
     * Returns whether the result does not contain an outcome.
     */
    boolean isEmpty();

    /**
     * Returns whether the result contains an outcome.
     */
    boolean isPresent();

    /**
     * Returns whether the result contains a {@code true} outcome.
     */
    boolean isTrue();

    /**
     * Returns whether the result contains a {@code false} outcome.
     */
    boolean isFalse();

    /**
     * Returns the Minecraft-facing {@link InteractionResult} for this result.
     */
    InteractionResult asMinecraft();
}
