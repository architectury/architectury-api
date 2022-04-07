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

package dev.architectury.platform.forge;

import net.minecraftforge.eventbus.api.IEventBus;

import java.util.*;
import java.util.function.Consumer;

public final class EventBuses {
    private EventBuses() {
    }
    
    private static final Map<String, IEventBus> EVENT_BUS_MAP = new HashMap<>();
    private static final Map<String, List<Consumer<IEventBus>>> ON_REGISTERED = new HashMap<>();
    
    public static void registerModEventBus(String modId, IEventBus bus) {
        synchronized (EVENT_BUS_MAP) {
            if (EVENT_BUS_MAP.putIfAbsent(modId, bus) != null) {
                throw new IllegalStateException("Can't register event bus for mod '" + modId + "' because it was previously registered!");
            }
        }
        
        List<Consumer<IEventBus>> consumers;
        
        synchronized (ON_REGISTERED) {
            consumers = ON_REGISTERED.getOrDefault(modId, Collections.emptyList());
        }
        
        for (Consumer<IEventBus> runnable : consumers) {
            runnable.accept(bus);
        }
    }
    
    public static void onRegistered(String modId, Consumer<IEventBus> busConsumer) {
        boolean registered;
        
        synchronized (EVENT_BUS_MAP) {
            registered = EVENT_BUS_MAP.containsKey(modId);
        }
        
        if (registered) {
            busConsumer.accept(EVENT_BUS_MAP.get(modId));
        } else {
            synchronized (ON_REGISTERED) {
                ON_REGISTERED.computeIfAbsent(modId, s -> new ArrayList<>()).add(busConsumer);
            }
        }
    }
    
    public static Optional<IEventBus> getModEventBus(String modId) {
        synchronized (EVENT_BUS_MAP) {
            return Optional.ofNullable(EVENT_BUS_MAP.get(modId));
        }
    }
}
