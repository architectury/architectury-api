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

package me.shedaniel.architectury.registry.entity.forge;

import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EntityAttributesImpl {
    private static final Map<Supplier<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> ATTRIBUTES = new HashMap<>();
    
    public static void register(Supplier<EntityType<? extends LivingEntity>> type, Supplier<AttributeSupplier.Builder> attribute) {
        ATTRIBUTES.put(type, attribute);
    }
    
    static {
        EventBuses.onRegistered(ArchitecturyForge.MOD_ID, bus -> {
            bus.register(EntityAttributesImpl.class);
        });
    }
    
    @SubscribeEvent
    public static void event(EntityAttributeCreationEvent event) {
        for (Map.Entry<Supplier<EntityType<? extends LivingEntity>>, Supplier<AttributeSupplier.Builder>> entry : ATTRIBUTES.entrySet()) {
            event.put(entry.getKey().get(), entry.getValue().get().build());
        }
    }
}
