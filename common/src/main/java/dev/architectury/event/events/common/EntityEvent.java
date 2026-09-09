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
import dev.architectury.utils.value.DoubleValue;
import dev.architectury.utils.value.FloatValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public interface EntityEvent {
    /**
     * @see LivingDeath#die(LivingEntity, DamageSource)
     */
    Event<LivingDeath> LIVING_DEATH = EventFactory.createEventResult();
    /**
     * @see LivingHurt#hurt(LivingEntity, DamageSource, float)
     */
    Event<LivingHurt> LIVING_HURT = EventFactory.createEventResult();
    /**
     * @see LivingCheckSpawn#canSpawn(LivingEntity, LevelAccessor, double, double, double, EntitySpawnReason, BaseSpawner)
     */
    Event<LivingCheckSpawn> LIVING_CHECK_SPAWN = EventFactory.createEventResult();
    /**
     * @see LivingDamagePost#damage(LivingEntity, DamageSource, float, float, boolean)
     */
    Event<LivingDamagePost> LIVING_DAMAGE_POST = EventFactory.createLoop();
    /**
     * @see Add#add(Entity, Level)
     */
    Event<Add> ADD = EventFactory.createEventResult();
    /**
     * @see Remove#remove(Entity, Level)
     */
    Event<Remove> REMOVE = EventFactory.createLoop();
    /**
     * @see EquipmentChange#change(LivingEntity, EquipmentSlot, ItemStack, ItemStack)
     */
    Event<EquipmentChange> EQUIPMENT_CHANGE = EventFactory.createLoop();
    /**
     * @see StartTracking#startTracking(Entity, ServerPlayer)
     */
    Event<StartTracking> START_TRACKING = EventFactory.createLoop();
    /**
     * @see StopTracking#stopTracking(Entity, ServerPlayer)
     */
    Event<StopTracking> STOP_TRACKING = EventFactory.createLoop();
    /**
     * @see EnterSection#enterSection(Entity, int, int, int, int, int, int)
     */
    Event<EnterSection> ENTER_SECTION = EventFactory.createLoop();
    /**
     * @see AnimalTame#tame(Animal, Player)
     */
    Event<AnimalTame> ANIMAL_TAME = EventFactory.createEventResult();
    
    /**
     * @see LivingFall#fall(LivingEntity, DoubleValue, FloatValue)
     */
    Event<LivingFall> LIVING_FALL = EventFactory.createEventResult();
    /**
     * @see Mount#mount(Entity, Entity, boolean)
     */
    Event<Mount> MOUNT = EventFactory.createEventResult();
    
    interface LivingDeath {
        /**
         * Invoked before a living entity dies.
         * Equivalent to Forge's {@code LivingDeathEvent} event.
         *
         * @param entity The entity that is about to die.
         * @param source The source of damage triggering the death.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the entity death may be cancelled by the result.
         */
        EventResult die(LivingEntity entity, DamageSource source);
    }
    
    interface LivingHurt {
        /**
         * Invoked before an entity is hurt by a damage source.
         * Equivalent to NeoForge's {@code LivingIncomingDamageEvent} or Forge's {@code LivingAttackEvent} event.
         *
         * <p>You currently cannot override the amount of damage the entity receives.
         *
         * @param entity The entity that is attacked.
         * @param source The reason why the entity takes damage.
         * @param amount The amount of damage the entity takes.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the entity attack may be cancelled by the result.
         */
        EventResult hurt(LivingEntity entity, DamageSource source, float amount);
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
         * @return A {@link EventResult} determining the outcome of the event,
         * if an outcome is set, the vanilla result is overridden.
         */
        EventResult canSpawn(LivingEntity entity, LevelAccessor world, double x, double y, double z, EntitySpawnReason type, @Nullable BaseSpawner spawner);
    }
    
    interface Add {
        /**
         * Invoked when an entity is about to be added to the world.
         * Equivalent to Forge's {@code EntityJoinWorldEvent} event.
         *
         * @param entity The entity to add to the level.
         * @param world  The level the entity is added to.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the entity addition may be cancelled by the result.
         */
        EventResult add(Entity entity, Level world);
    }

    interface Remove {
        /**
         * Invoked when an entity is removed from the world.
         * Equivalent to NeoForge's {@code EntityLeaveLevelEvent} event.
         *
         * <p>This is the counterpart to {@link Add#add(Entity, Level)} and fires on both the client and the server.
         *
         * @param entity The entity being removed from the level.
         * @param world  The level the entity is removed from.
         */
        void remove(Entity entity, Level world);
    }

    interface EquipmentChange {
        /**
         * Invoked when a living entity's equipment changes in any slot.
         * Equivalent to NeoForge's {@code LivingEquipmentChangeEvent} event.
         *
         * <p>Only fires on the server.
         *
         * @param entity        The entity whose equipment changed.
         * @param slot          The slot that changed.
         * @param previousStack The stack that was previously in the slot.
         * @param currentStack  The stack that is now in the slot.
         */
        void change(LivingEntity entity, EquipmentSlot slot, ItemStack previousStack, ItemStack currentStack);
    }

    interface StartTracking {
        /**
         * Invoked when an entity starts being tracked by a player.
         * Equivalent to NeoForge's {@code PlayerEvent.StartTracking} event.
         *
         * @param entity The entity that started being tracked.
         * @param player The player now tracking the entity.
         */
        void startTracking(Entity entity, ServerPlayer player);
    }

    interface StopTracking {
        /**
         * Invoked when an entity stops being tracked by a player.
         * Equivalent to NeoForge's {@code PlayerEvent.StopTracking} event.
         *
         * @param entity The entity that stopped being tracked.
         * @param player The player that is no longer tracking the entity.
         */
        void stopTracking(Entity entity, ServerPlayer player);
    }

    interface LivingDamagePost {
        /**
         * Invoked after damage has been applied to a living entity.
         * Equivalent to NeoForge's {@code LivingDamageEvent.Post} event.
         *
         * <p>Unlike {@link LivingHurt}, this fires once the damage is final and cannot be cancelled
         * or modified. Only fires on the server.
         *
         * @param entity         The entity that was damaged.
         * @param source         The source of the damage.
         * @param originalDamage The damage amount before any reductions were applied.
         * @param appliedDamage  The damage amount that was actually taken off the entity's health.
         * @param blocked        Whether the damage was blocked, for example by a shield.
         */
        void damage(LivingEntity entity, DamageSource source, float originalDamage, float appliedDamage, boolean blocked);
    }

    interface EnterSection {
        /**
         * Invoked whenever an entity enters a chunk.
         * Equivalent to Forge's {@code EnteringChunk} event.
         *
         * @param entity   The entity moving to a different chunk.
         * @param sectionX The chunk x-coordinate.
         * @param sectionY The chunk y-coordinate.
         * @param sectionZ The chunk z-coordinate.
         * @param prevX    The previous chunk x-coordinate.
         * @param prevY    The previous chunk y-coordinate.
         * @param prevZ    The previous chunk z-coordinate.
         */
        void enterSection(Entity entity, int sectionX, int sectionY, int sectionZ, int prevX, int prevY, int prevZ);
    }
    
    interface AnimalTame {
        /**
         * Invoked before a tamable animal is tamed.
         * This event only works on vanilla mobs. Mods implementing their own entities may want to make their own events or invoke this.
         * Equivalent to Forge's {@code AnimalTameEvent} event.
         *
         * @param animal The animal being tamed.
         * @param player The tamer.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        EventResult tame(Animal animal, Player player);
    }
    
    interface LivingFall {
        /**
         * Invoked when a living entity lands and fall damage is about to be worked out, from
         * {@link LivingEntity#causeFallDamage(double, float, net.minecraft.world.damagesource.DamageSource)}.
         * Both values are pre-filled with what vanilla would use and may be changed to scale the resulting damage.
         *
         * <p>Equivalent to NeoForge's {@code LivingFallEvent}; Architectury supplies it with a mixin on Fabric.
         *
         * @param entity           The entity that fell.
         * @param distance         The distance fallen, in blocks.
         * @param damageMultiplier The multiplier applied to the fall damage.
         * @return A {@link EventResult} determining the outcome of the event. An interrupted false result cancels the
         * fall entirely, so no fall damage is dealt and no fall side effects (such as farmland trampling) happen.
         */
        EventResult fall(LivingEntity entity, DoubleValue distance, FloatValue damageMultiplier);
    }
    
    interface Mount {
        /**
         * Invoked when an entity starts or stops riding another entity, from {@link Entity#startRiding(Entity)} and
         * {@link Entity#removeVehicle()}. Fires on both the logical client and the logical server.
         *
         * <p>Equivalent to NeoForge's {@code EntityMountEvent}; Architectury supplies it with a mixin on Fabric.
         *
         * @param entity   The entity mounting or dismounting.
         * @param vehicle  The entity being mounted or dismounted. When dismounting this is the vehicle being left.
         * @param mounting {@code true} when mounting, {@code false} when dismounting.
         * @return A {@link EventResult} determining the outcome of the event. An interrupted false result stops the
         * entity from mounting, or keeps it on its vehicle when dismounting.
         */
        EventResult mount(Entity entity, Entity vehicle, boolean mounting);
    }
}
