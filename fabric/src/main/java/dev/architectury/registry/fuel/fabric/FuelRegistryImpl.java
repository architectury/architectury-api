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

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FuelRegistryImpl {
    /**
     * The cooking fuel component to force onto each item's default components. A {@code null} value means the
     * item is made a non-fuel by removing the component; items registered with a negative burn time are absent
     * from the map entirely, so they keep whatever they declare themselves.
     */
    private static final Map<Item, @Nullable CookingFuel> FUELS = new LinkedHashMap<>();
    
    public static void register(int time, float speedMultiplier, ItemLike... items) {
        for (var item : items) {
            if (time < 0) {
                FUELS.remove(item.asItem());
            } else {
                FUELS.put(item.asItem(), time == 0 ? null : new CookingFuel(new ResolvableInt.Constant(time),
                        new ResolvableFloat.Constant(speedMultiplier)));
            }
        }
    }
    
    public static int get(ItemStack stack, ServerLevel level) {
        return ResolvableInt.getFromItem(stack, DataComponents.COOKING_FUEL, CookingFuel::burnTime, createLootContext(level), 0);
    }
    
    private static LootContext createLootContext(ServerLevel level) {
        return new LootContext.Builder(new LootParams.Builder(level).create(LootContextParamSets.EMPTY))
                .create(Optional.empty());
    }
    
    static {
        DefaultItemComponentEvents.MODIFY.register(context -> {
            FUELS.forEach((item, fuel) -> context.modify(item, builder -> builder.set(DataComponents.COOKING_FUEL, fuel)));
        });
    }
}
