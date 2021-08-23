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
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class HoeItemHooks {
    private HoeItemHooks() {
    }
    
    /**
     * Adds a new tilling (interact with hoe) interaction to the game.<p>
     *
     * Tilling uses a predicate/consumer pair system:
     * <ul>
     *     <li>First, the game tests the context using the predicate.</li>
     *     <li>Then, if the predicate returns {@code true}, the game will, <em>on the server side only</em>,
     *         invoke the action and then damage the hoe item by 1.</li>
     *     <li>Otherwise, no action will be invoked.</li>
     * </ul>
     *
     * @param input input block
     * @param predicate context predicate
     * @param action action to run
     */
    public static void addTillable(Block input, Predicate<UseOnContext> predicate, Consumer<UseOnContext> action) {
        if (HoeItem.TILLABLES instanceof ImmutableMap) {
            HoeItem.TILLABLES = new HashMap<>(HoeItem.TILLABLES);
        }
        HoeItem.TILLABLES.put(input, new Pair<>(predicate, action));
    }
}
