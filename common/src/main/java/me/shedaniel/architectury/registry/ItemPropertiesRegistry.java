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

package me.shedaniel.architectury.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * Registry for registering item properties used for model predicates.
 *
 * @see net.minecraft.client.renderer.item.ItemProperties
 */
@Environment(EnvType.CLIENT)
public final class ItemPropertiesRegistry {
    private ItemPropertiesRegistry() {
    }
    
    public static ItemPropertyFunction registerGeneric(ResourceLocation propertyId, ItemPropertyFunction function) {
        return ItemProperties.registerGeneric(propertyId, function);
    }
    
    public static ItemPropertyFunction register(Item item, ResourceLocation propertyId, ItemPropertyFunction function) {
        ItemProperties.register(item, propertyId, function);
        return function;
    }
}
