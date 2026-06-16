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

public interface Event<T> {
    /**
     * Returns the combined invoker for all registered listeners. The returned object is a
     * proxy whose methods delegate to every registered listener, ordered by
     * {@linkplain EventPriority priority} and then by registration order.
     * The reference may change after each call to {@link #register} or {@link #unregister},
     * so do not cache it across registrations.
     */
    T invoker();

    /**
     * Registers a listener at {@link EventPriority#NORMAL} priority. Duplicate registrations
     * are allowed and will cause the listener to be invoked multiple times per event fire.
     *
     * @param listener the listener to add; must not be {@code null}
     * @see #register(EventPriority, Object)
     */
    void register(T listener);

    /**
     * Registers a listener at the given {@linkplain EventPriority priority}. Listeners with a
     * higher priority are invoked before those with a lower priority; listeners sharing a
     * priority are invoked in registration order. Duplicate registrations are allowed and will
     * cause the listener to be invoked multiple times per event fire.
     *
     * @param priority the priority to register the listener at; must not be {@code null}
     * @param listener the listener to add; must not be {@code null}
     */
    default void register(EventPriority priority, T listener) {
        register(listener);
    }

    /**
     * Removes the first occurrence of the given listener. Has no effect if the listener
     * is not registered.
     *
     * @param listener the listener to remove
     */
    void unregister(T listener);

    /**
     * Returns {@code true} if the given listener is currently registered.
     *
     * @param listener the listener to check
     */
    boolean isRegistered(T listener);

    /**
     * Removes all registered listeners.
     */
    void clearListeners();
}
