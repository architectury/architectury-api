package me.shedaniel.architectury.event.forge;

import me.shedaniel.architectury.event.Event;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

public class EventFactoryImpl {
    public static <T> Event<Consumer<T>> attachToForge(Event<Consumer<T>> event) {
        event.register(eventObj -> {
            if (!(eventObj instanceof net.minecraftforge.eventbus.api.Event)) {
                throw new ClassCastException(eventObj.getClass() + " is not an instance of forge Event!");
            }
            MinecraftForge.EVENT_BUS.post((net.minecraftforge.eventbus.api.Event) eventObj);
        });
        return event;
    }
}