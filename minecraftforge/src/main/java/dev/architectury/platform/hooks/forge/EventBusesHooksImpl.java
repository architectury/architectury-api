package dev.architectury.platform.hooks.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.Optional;
import java.util.function.Consumer;

public class EventBusesHooksImpl {
    public static void whenAvailable(String modId, Consumer<IEventBus> busConsumer) {
        EventBuses.onRegistered(modId, busConsumer);
    }
    
    public static Optional<IEventBus> getModEventBus(String modId) {
        return EventBuses.getModEventBus(modId);
    }
}
