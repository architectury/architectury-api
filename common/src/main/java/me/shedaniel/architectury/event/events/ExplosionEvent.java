/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ExplosionEvent {
    /**
     * @see Pre#explode(Level, Explosion)
     */
    Event<Pre> PRE = EventFactory.createInteractionResult();
    /**
     * @see Detonate#explode(Level, Explosion, List)
     */
    Event<Detonate> DETONATE = EventFactory.createLoop();
    
    interface Pre {
        /**
         * Called before an explosion happens.
         * Equal to the forge {@code ExplosionEvent.Start} event
         * 
         * @param world The level the explosion is happening in.
         * @param explosion The explosion.
         * @return Returning {@link InteractionResult#FAIL} cancels the explosion.
         */
        InteractionResult explode(Level world, Explosion explosion);
    }
    
    interface Detonate {
        /**
         * Called when an explosion happens.
         * Equal to the forge {@code ExplosionEvent.Detonate} event.
         * 
         * @param world The level the explosion happens in.
         * @param explosion The explosion happening.
         * @param affectedEntities The entities affected by the explosion.
         */
        void explode(Level world, Explosion explosion, List<Entity> affectedEntities);
    }
}
