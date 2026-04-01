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

package dev.architectury.registry.client.rendering.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ColorHandlerRegistryImpl {
    @SafeVarargs
    public static void registerBlockColors(BlockTintSource blockColor, Supplier<? extends Block>... blocks) {
        Objects.requireNonNull(blockColor, "color is null!");
        BlockColorRegistry.register(List.of(blockColor), unpackBlocks(blocks));
    }
    
    private static Block[] unpackBlocks(Supplier<? extends Block>[] blocks) {
        var array = new Block[blocks.length];
        for (var i = 0; i < blocks.length; i++) {
            array[i] = Objects.requireNonNull(blocks[i].get());
        }
        return array;
    }
}
