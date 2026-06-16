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

package dev.architectury.test.events;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventPriority;
import dev.architectury.test.TestMod;

import java.util.ArrayList;
import java.util.List;

/**
 * Self-checking verification for {@link EventPriority} listener ordering.
 * Registers listeners deliberately out of priority order on a throwaway event,
 * fires it once, and asserts they were invoked highest-priority first with registration order
 * preserved within a single priority. Runs at mod init on both loaders.
 */
public final class EventPriorityTest {
    @FunctionalInterface
    public interface Marker {
        void mark();
    }

    public static void run() {
        List<String> order = new ArrayList<>();
        Event<Marker> event = EventFactory.createLoop();
        // Registered out of priority order; within NORMAL, registration order must be preserved.
        event.register(EventPriority.NORMAL, () -> order.add("normal-1"));
        event.register(EventPriority.LOWEST, () -> order.add("lowest"));
        event.register(EventPriority.HIGHEST, () -> order.add("highest"));
        event.register(() -> order.add("normal-2")); // no priority => NORMAL, after normal-1
        event.register(EventPriority.HIGH, () -> order.add("high"));

        event.invoker().mark();

        List<String> expected = List.of("highest", "high", "normal-1", "normal-2", "lowest");
        if (!order.equals(expected)) {
            throw new AssertionError("[architectury-test] EventPriority ordering broken: expected "
                    + expected + " but got " + order);
        }
        TestMod.SINK.accept("EventPriority ordering OK: " + order);
    }

    public EventPriorityTest() {
    }
}
