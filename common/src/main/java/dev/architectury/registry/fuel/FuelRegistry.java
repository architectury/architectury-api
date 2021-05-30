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

package dev.architectury.registry.fuel;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public final class FuelRegistry {
    private FuelRegistry() {
    }
    
    /**
     * Registers a burn time for items.
     *
     * @param time  the new burn time, use {@code 0} for non-fuel items,
     *              and {@code -1} to use vanilla logic
     * @param items the array of items to register for
     */
    @ExpectPlatform
    public static void register(int time, ItemLike... items) {
        throw new AssertionError();
    }
    
    /**
     * Returns the burn time of an {@link ItemStack}.
     *
     * @param stack the stack
     * @return the burn time of the stack, returns {@code 0} if not a fuel
     */
    @ExpectPlatform
    public static int get(ItemStack stack) {
        throw new AssertionError();
    }
}
