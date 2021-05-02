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
import me.shedaniel.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface EntityEvent {
    /**
     * Invoked before LivingEntity#die, equivalent to forge's {@code LivingDeathEvent}.
     */
    Event<LivingDeath> LIVING_DEATH = EventFactory.createInteractionResult();
    /**
     * Invoked before LivingEntity#hurt, equivalent to forge's {@code LivingAttackEvent}.
     */
    Event<LivingAttack> LIVING_ATTACK = EventFactory.createInteractionResult();
    /**
     * Invoked when an entity is about to be spawned, equivalent to forge's {@code LivingSpawnEvent.CheckSpawn}
     */
    Event<LivingCheckSpawn> LIVING_CHECK_SPAWN = EventFactory.createEventResult();
    /**
     * Invoked before entity is added to a world, equivalent to forge's {@code EntityJoinWorldEvent}.
     */
    Event<Add> ADD = EventFactory.createInteractionResult();
    /**
     * Invoked when an entity enters a chunk,  equivalent to forge's {@code EnteringChunk}
     */
    Event<EnterChunk> ENTER_CHUNK = EventFactory.createLoop();
    
    interface LivingDeath {
        InteractionResult die(LivingEntity entity, DamageSource source);
    }
    
    interface LivingAttack {
        InteractionResult attack(LivingEntity entity, DamageSource source, float amount);
    }
    
    interface LivingCheckSpawn {
        EventResult canSpawn(LivingEntity entity, LevelAccessor world, double x, double y, double z, MobSpawnType type, @Nullable BaseSpawner spawner);
    }
    
    interface Add {
        InteractionResult add(Entity entity, Level world);
    }
    
    interface EnterChunk {
        void enterChunk(Entity entity, int chunkX, int chunkZ, int prevX, int prevZ);
    }
    
}
