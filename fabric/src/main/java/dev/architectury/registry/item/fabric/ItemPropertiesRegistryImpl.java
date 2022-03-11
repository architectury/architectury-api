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

package dev.architectury.registry.item.fabric;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public class ItemPropertiesRegistryImpl {
    public static ClampedItemPropertyFunction registerGeneric(ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        FabricModelPredicateProviderRegistry.register(propertyId, function);
        return function;
    }
    
    public static ClampedItemPropertyFunction register(ItemLike item, ResourceLocation propertyId, ClampedItemPropertyFunction function) {
        FabricModelPredicateProviderRegistry.register(item.asItem(), propertyId, function);
        return function;
    }
}
