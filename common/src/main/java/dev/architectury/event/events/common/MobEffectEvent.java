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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events fired as mob effects are applied to and removed from entities.
 *
 * <p>All events here fire on the server only.
 */
public interface MobEffectEvent {
    /**
     * @see AllowAdd#allowAdd(LivingEntity, MobEffectInstance)
     */
    Event<AllowAdd> ALLOW_ADD = EventFactory.createEventResult();
    /**
     * @see AfterAdd#afterAdd(LivingEntity, MobEffectInstance)
     */
    Event<AfterAdd> AFTER_ADD = EventFactory.createLoop();
    /**
     * @see AllowRemove#allowRemove(LivingEntity, MobEffectInstance)
     */
    Event<AllowRemove> ALLOW_REMOVE = EventFactory.createEventResult();

    interface AllowAdd {
        /**
         * Invoked to check whether a mob effect may be applied to an entity.
         * Equivalent to NeoForge's {@code MobEffectEvent.Applicable} event and
         * Fabric's {@code ServerMobEffectEvents#ALLOW_ADD}.
         *
         * @param entity The entity the effect is being applied to.
         * @param effect The effect instance being applied.
         * @return A {@link EventResult} determining the outcome of the event,
         * the application of the effect may be cancelled by the result.
         */
        EventResult allowAdd(LivingEntity entity, MobEffectInstance effect);
    }

    interface AfterAdd {
        /**
         * Invoked after a mob effect has been applied to an entity.
         * Equivalent to NeoForge's {@code MobEffectEvent.Added} event and
         * Fabric's {@code ServerMobEffectEvents#AFTER_ADD}.
         *
         * @param entity The entity the effect was applied to.
         * @param effect The effect instance that was applied.
         */
        void afterAdd(LivingEntity entity, MobEffectInstance effect);
    }

    interface AllowRemove {
        /**
         * Invoked to check whether a mob effect may be removed from an entity before it expires.
         * Equivalent to NeoForge's {@code MobEffectEvent.Remove} event and
         * Fabric's {@code ServerMobEffectEvents#ALLOW_EARLY_REMOVE}.
         *
         * <p>This does not fire when an effect runs out on its own; it covers early removals such as
         * drinking milk, using a totem of undying, or {@code /effect clear}.
         *
         * @param entity The entity the effect is being removed from.
         * @param effect The effect instance being removed.
         * @return A {@link EventResult} determining the outcome of the event,
         * the removal of the effect may be cancelled by the result.
         */
        EventResult allowRemove(LivingEntity entity, MobEffectInstance effect);
    }
}
