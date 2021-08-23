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

package dev.architectury.hooks.item.tool;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;

public final class ShovelItemHooks {
    private ShovelItemHooks() {
    }
    
    /**
     * Adds a new flattening (interact with shovel) interaction to the game.<p>
     *
     * <b>Notes:</b>
     * <ul>
     *     <li>Blocks can only be flattened if they have no block above them.</li>
     *     <li>{@linkplain net.minecraft.world.level.block.CampfireBlock Campfires} have a special case for being extinguished by a shovel,
     *     though you <em>can</em> override that using this method due to the check order.</li>
     * </ul>
     *
     * @param input input block
     * @param result result block state
     */
    public static void addFlattenable(Block input, BlockState result) {
        if (ShovelItem.FLATTENABLES instanceof ImmutableMap) {
            ShovelItem.FLATTENABLES = new HashMap<>(ShovelItem.FLATTENABLES);
        }
        ShovelItem.FLATTENABLES.put(input, result);
    }
}
