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

package dev.architectury.registry.fuel.forge;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.jetbrains.annotations.Nullable;

public class FuelRegistryImpl {
    private static final Object2IntMap<ItemLike> ITEMS = new Object2IntLinkedOpenHashMap<>();
    
    static {
        NeoForge.EVENT_BUS.register(FuelRegistryImpl.class);
    }
    
    public static void register(int time, ItemLike... items) {
        for (ItemLike item : items) {
            ITEMS.put(item, time);
        }
    }
    
    public static int get(ItemStack stack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        return stack.getBurnTime(recipeType, fuelValues);
    }
    
    @SubscribeEvent
    public static void event(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().isEmpty()) return;
        int time = ITEMS.getOrDefault(event.getItemStack().getItem(), Integer.MIN_VALUE);
        if (time != Integer.MIN_VALUE) {
            event.setBurnTime(time);
        }
    }
}
