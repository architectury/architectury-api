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

/**
 * Controls the order in which listeners registered to a single {@link Event} are invoked.
 * Listeners with a higher priority are invoked before listeners with a lower priority.
 * Listeners that share a priority are invoked in the order they were registered.
 *
 * <p>This ordering is resolved entirely within Architectury, across all listeners registered
 * to the same {@link Event}. It is independent of, and does not interact with, the underlying
 * platform's own event ordering (NeoForge's {@code EventPriority} or Fabric's event phases) —
 * Architectury registers a single aggregating listener per platform regardless of priority.
 *
 * @see Event#register(EventPriority, Object)
 */
public enum EventPriority {
    /**
     * The highest priority; these listeners are invoked first.
     */
    HIGHEST,
    /**
     * Invoked after {@link #HIGHEST} but before {@link #NORMAL}.
     */
    HIGH,
    /**
     * The default priority, used when a listener is registered without an explicit priority.
     */
    NORMAL,
    /**
     * Invoked after {@link #NORMAL} but before {@link #LOWEST}.
     */
    LOW,
    /**
     * The lowest priority; these listeners are invoked last.
     */
    LOWEST;

    /**
     * The default priority ({@link #NORMAL}) applied to listeners registered via
     * {@link Event#register(Object)}.
     */
    public static final EventPriority DEFAULT = NORMAL;
}
