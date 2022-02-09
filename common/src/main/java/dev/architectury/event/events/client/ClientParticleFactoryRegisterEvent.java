package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ClientParticleFactoryRegisterEvent {
    /**
     * Registers Particle Factory on the client.
     * <p>
     * <p>
     * see Forge's {@code ParticleFactoryRegisterEvent}
     */
    Event<Runnable> REGISTER = EventFactory.createLoop();
}
