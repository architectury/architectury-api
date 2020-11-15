package me.shedaniel.architectury.event.fabric;

import me.shedaniel.architectury.event.Event;

import java.util.function.Consumer;

public class EventFactoryImpl {
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        return event;
    }
}
