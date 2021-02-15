package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface LightningEvent {
    
    // TODO Pre - Called before a lightning bolt entity is added to the world. (cancellable)
    /**
     * Invoked after the lightning has gathered a list of entities to strike.
     * Remove entities from the list to stop them from being hit.
     */
    Event<Strike> STRIKE = EventFactory.createLoop();
    // TODO Post - Called before a lightning bolt entity is removed from the world.
    
    interface Strike {
        void onStrike(LightningBolt bolt, Level level, Vec3 pos, List<Entity> toStrike);
    }
    
}
