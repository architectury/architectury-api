package me.shedaniel.architectury.init.fabric;

import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.client.Minecraft;

public class ArchitecturyClient {
    public static void init() {
        ClientLifecycleEvent.CLIENT_SETUP.invoker().stateChanged(Minecraft.getInstance());
    }
}
