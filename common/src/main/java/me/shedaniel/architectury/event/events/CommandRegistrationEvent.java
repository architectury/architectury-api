package me.shedaniel.architectury.event.events;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.commands.CommandSourceStack;

public interface CommandRegistrationEvent {
    Event<CommandRegistrationEvent> EVENT = EventFactory.createLoop(CommandRegistrationEvent.class);
    
    void register(CommandDispatcher<CommandSourceStack> var1);
}
