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

package dev.architectury.event.forge;

import dev.architectury.event.Event;
import dev.architectury.event.EventActor;
import dev.architectury.event.EventResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.bus.EventBus;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class EventFactoryImpl {
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.internal.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            }
            post((net.minecraftforge.eventbus.internal.Event) eventObj);
        });
        return event;
    }

    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActor(Event<EventActor<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.internal.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            }
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.event.characteristic.Cancellable)) {
                throw new ClassCastException(eventObj.getClass() + " is not cancellable Event!");
            }
            post((net.minecraftforge.eventbus.internal.Event) eventObj);
            return EventResult.pass();
        });
        return event;
    }

    @ApiStatus.Internal
    public static <T> Event<EventActor<T>> attachToForgeEventActorCancellable(Event<EventActor<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.internal.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            }
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.event.characteristic.Cancellable)) {
                throw new ClassCastException(eventObj.getClass() + " is not cancellable Event!");
            }
            if (post((net.minecraftforge.eventbus.internal.Event) eventObj)) {
                return EventResult.interrupt(false);
            }
            return EventResult.pass();
        });
        return event;
    }

    @SuppressWarnings("unchecked")
    private static boolean post(net.minecraftforge.eventbus.internal.Event event) {
        try {
            Field busField = event.getClass().getField("BUS");
            Object bus = busField.get(null);
            if (bus instanceof EventBus<?> eventBus) {
                return ((EventBus<net.minecraftforge.eventbus.internal.Event>) eventBus).post(event);
            }
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Failed to locate Forge event bus for " + event.getClass(), exception);
        }

        throw new IllegalStateException("Event class " + event.getClass() + " does not expose a static BUS field");
    }
}
