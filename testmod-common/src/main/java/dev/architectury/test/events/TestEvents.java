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

import com.google.common.base.Preconditions;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventPriority;
import dev.architectury.event.EventResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TestEvents {
    public static void test() {
        testPriority();
        testOrder();
    }
    
    public static void testPriority() {
        Event<Supplier<EventResult>> event = EventFactory.createEventResult();
        event.register(EventPriority.HIGH, () -> {
            throw new RuntimeException("This should not be ran!");
        });
        event.register(EventPriority.HIGHEST, () -> EventResult.interruptDefault());
        Preconditions.checkArgument(event.invoker().get() == EventResult.interruptDefault());
    }
    
    public static void testOrder() {
        Event<Runnable> event = EventFactory.createLoop();
        Runnable runnable;
        List<Integer> list = new ArrayList<>();
        event.register(EventPriority.HIGH, () -> list.add(2));
        event.register(() -> list.add(4));
        event.register(EventPriority.LOW, () -> list.add(6));
        event.register(EventPriority.HIGH, () -> list.add(3));
        event.register(EventPriority.LOWEST, runnable = () -> list.add(8));
        event.register(EventPriority.HIGHEST, () -> list.add(0));
        event.register(() -> list.add(5));
        event.register(EventPriority.LOW, () -> list.add(7));
        event.register(EventPriority.LOWEST, () -> list.add(9));
        event.register(EventPriority.HIGHEST, () -> list.add(1));
        
        // Test if the order is correct, when we invoke all priorities at once
        event.invoker().run();
        Preconditions.checkArgument(list.equals(list.stream().sorted().toList()));
        Preconditions.checkArgument(list.size() == 10);
        
        list.clear();
        // Test if the order is correct, when we invoke each priority individually
        for (EventPriority value : EventPriority.VALUES) {
            event.invoker(value).run();
        }
        Preconditions.checkArgument(list.equals(list.stream().sorted().toList()));
        Preconditions.checkArgument(list.size() == 10);
        
        // Test if unregistering works
        Preconditions.checkArgument(event.isRegistered(runnable));
        event.unregister(runnable);
        Preconditions.checkArgument(!event.isRegistered(runnable));
        
        // Now test if the order is correct, when we invoke all priorities at once
        list.clear();
        event.invoker().run();
        Preconditions.checkArgument(list.equals(list.stream().sorted().toList()));
        Preconditions.checkArgument(list.size() == 9);
        
        // Test if the order is correct, when we invoke each priority individually
        list.clear();
        for (EventPriority value : EventPriority.VALUES) {
            event.invoker(value).run();
        }
        Preconditions.checkArgument(list.equals(list.stream().sorted().toList()));
        Preconditions.checkArgument(list.size() == 9);
    }
}
