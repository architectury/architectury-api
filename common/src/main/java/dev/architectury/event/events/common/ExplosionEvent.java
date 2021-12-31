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

package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ExplosionEvent {
    /**
     * @see Pre#explode(Level, Explosion)
     */
    Event<Pre> PRE = EventFactory.createEventResult();
    /**
     * @see Detonate#explode(Level, Explosion, List)
     */
    Event<Detonate> DETONATE = EventFactory.createLoop();
    
    interface Pre {
        /**
         * Invoked before an explosion happens.
         * Equivalent to Forge's {@code ExplosionEvent.Start} event.
         *
         * @param level     The level the explosion is happening in.
         * @param explosion The explosion.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla explosion may be cancelled by the result.
         */
        EventResult explode(Level level, Explosion explosion);
    }
    
    interface Detonate {
        /**
         * Invoked when an explosion is detonating.
         * Equivalent to Forge's {@code ExplosionEvent.Detonate} event.
         *
         * @param level            The level the explosion happens in.
         * @param explosion        The explosion happening.
         * @param affectedEntities The entities affected by the explosion.
         */
        void explode(Level level, Explosion explosion, List<Entity> affectedEntities);
    }
}
