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

package dev.architectury.registry.fuel.fabric;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FuelRegistryImpl {
    private static final List<FuelRegistryEvents.BuildCallback> CALLBACKS = new ArrayList<>();
    private static final List<FuelRegistryEvents.ExclusionsCallback> EXCLUSIONS_CALLBACKS = new ArrayList<>();
    
    public static void register(int time, ItemLike... items) {
        CALLBACKS.add((builder, context) -> {
            for (var item : items) {
                if (time >= 0) {
                    builder.add(item, time);
                }
            }
        });
        EXCLUSIONS_CALLBACKS.add((builder, context) -> {
            for (var item : items) {
                if (time < 0) {
                    builder.values.keySet().remove(item.asItem());
                }
            }
        });
    }
    
    public static int get(ItemStack stack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        return fuelValues.burnDuration(stack);
    }
    
    static {
        FuelRegistryEvents.BUILD.register((builder, context) -> {
            for (var callback : CALLBACKS) {
                callback.build(builder, context);
            }
        });
        FuelRegistryEvents.EXCLUSIONS.register((builder, context) -> {
            for (var callback : EXCLUSIONS_CALLBACKS) {
                callback.buildExclusions(builder, context);
            }
        });
    }
}
