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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public interface LightningEvent {
    
    // TODO Pre - Invoked before a lightning bolt entity is added to the world. (cancellable)
    /**
     * @see Strike#onStrike(LightningBolt, Level, Vec3, List)
     */
    Event<Strike> STRIKE = EventFactory.createLoop();
    // TODO Post - Invoked before a lightning bolt entity is removed from the world.
    
    interface Strike {
        /**
         * Invoked after the lightning has gathered a list of entities to strike.
         *
         * @param bolt     The lightning bolt.
         * @param level    The level the lighting is spawned in.
         * @param pos      The position the lightning strikes.
         * @param toStrike A list of all entities the lightning affects.
         */
        void onStrike(LightningBolt bolt, Level level, Vec3 pos, List<Entity> toStrike);
    }
}
