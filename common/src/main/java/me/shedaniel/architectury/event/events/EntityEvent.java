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
import net.minecraft.world.InteractionResultHolder;
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
     * @see LivingDeath#die(LivingEntity, DamageSource)
     */
    Event<LivingDeath> LIVING_DEATH = EventFactory.createInteractionResult();
    /**
     * @see LivingAttack#attack(LivingEntity, DamageSource, float)
     */
    Event<LivingAttack> LIVING_ATTACK = EventFactory.createInteractionResult();
    /**
     * @see LivingCheckSpawn#canSpawn(LivingEntity, LevelAccessor, double, double, double, MobSpawnType, BaseSpawner)
     */
    Event<LivingCheckSpawn> LIVING_CHECK_SPAWN = EventFactory.createEventResult();
    /**
     * @see Add#add(Entity, Level)
     */
    Event<Add> ADD = EventFactory.createInteractionResult();
    /**
     * @see EnterChunk#enterChunk(Entity, int, int, int, int)
     */
    Event<EnterChunk> ENTER_CHUNK = EventFactory.createLoop();
    
    /**
     * @deprecated use {@link BlockEvent#PLACE}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0")
    Event<PlaceBlock> PLACE_BLOCK = EventFactory.createInteractionResult();
    
    interface LivingDeath {
        /**
         * Invoked before a living entity dies.
         * Equivalent to Forge's {@code LivingDeathEvent} event.
         *
         * @param entity The entity that is about to die.
         * @param source The source of damage triggering the death.
         * @return Returning {@link InteractionResult#FAIL} prevents the entity from dying.
         */
        InteractionResult die(LivingEntity entity, DamageSource source);
    }
    
    interface LivingAttack {
        /**
         * Invoked before an entity is hurt by a damage source.
         * Equivalent to Forge's {@code LivingAttackEvent} event.
         *
         * <p>You currently cannot override the amount of damage the entity receives.
         *
         * @param entity The entity that is attacked.
         * @param source The reason why the entity takes damage.
         * @param amount The amount of damage the entity takes.
         * @return Returning {@link InteractionResult#FAIL} prevents the entity from taking damage.
         */
        InteractionResult attack(LivingEntity entity, DamageSource source, float amount);
    }
    
    interface LivingCheckSpawn {
        /**
         * Invoked before an entity is spawned into the world.
         * This specifically concerns <i>spawning</i> through either a {@link BaseSpawner} or during world generation.
         * Equivalent to Forge's {@code LivingSpawnEvent.CheckSpawn} event.
         *
         * @param entity  The entity that is about to spawn.
         * @param world   The level the entity wants to spawn in.
         * @param x       The x-coordinate of the spawn position.
         * @param y       The y-coordinate of the spawn position.
         * @param z       The z-coordinate the spawn position.
         * @param type    The source of spawning.
         * @param spawner The spawner. Can be {@code null}.
         * @return A {@link InteractionResultHolder} determining the outcome of the event,
         * if an outcome is set, the vanilla result is overridden.
         */
        EventResult canSpawn(LivingEntity entity, LevelAccessor world, double x, double y, double z, MobSpawnType type, @Nullable BaseSpawner spawner);
    }
    
    interface Add {
        /**
         * Invoked when an entity is about to be added to the world.
         * Equivalent to Forge's {@code EntityJoinWorldEvent} event.
         *
         * @param entity The entity to add to the level.
         * @param world  The level the entity is added to.
         * @return Returning {@link InteractionResult#FAIL} prevents the addition of the entity to the world.
         */
        InteractionResult add(Entity entity, Level world);
    }
    
    @Deprecated
    interface PlaceBlock {
        InteractionResult placeBlock(Level world, BlockPos pos, BlockState state, @Nullable Entity placer);
    }
    
    interface EnterChunk {
        /**
         * Invoked whenever an entity enters a chunk.
         * Equivalent to Forge's {@code EnteringChunk} event.
         *
         * @param entity The entity moving to a different chunk.
         * @param chunkX The chunk x-coordinate.
         * @param chunkZ The chunk z-coordinate.
         * @param prevX  The previous chunk x-coordinate.
         * @param prevZ  The previous chunk z-coordinate.
         */
        void enterChunk(Entity entity, int chunkX, int chunkZ, int prevX, int prevZ);
    }
    
}
