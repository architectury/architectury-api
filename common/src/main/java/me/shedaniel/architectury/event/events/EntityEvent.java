/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public interface EntityEvent {
    /**
     * Invoked before LivingEntity#die, equivalent to forge's {@code LivingDeathEvent}.
     */
    Event<LivingDeath> LIVING_DEATH = EventFactory.createInteractionResult(LivingDeath.class);
    /**
     * Invoked before LivingEntity#hurt, equivalent to forge's {@code LivingAttackEvent}.
     */
    Event<LivingAttack> LIVING_ATTACK = EventFactory.createInteractionResult(LivingAttack.class);
    /**
     * Invoked before entity is added to a world, equivalent to forge's {@code EntityJoinWorldEvent}.
     */
    Event<Add> ADD = EventFactory.createInteractionResult(Add.class);
    
    interface LivingDeath {
        InteractionResult die(LivingEntity entity, DamageSource source);
    }
    
    interface LivingAttack {
        InteractionResult attack(LivingEntity entity, DamageSource source, float amount);
    }
    
    interface Add {
        InteractionResult add(Entity entity, Level world);
    }
}
