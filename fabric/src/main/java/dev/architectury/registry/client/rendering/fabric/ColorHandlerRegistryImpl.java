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

package dev.architectury.registry.client.rendering.fabric;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.function.Supplier;

public class ColorHandlerRegistryImpl {
    @SafeVarargs
    public static void registerItemColors(ItemColor itemColor, Supplier<ItemLike>... items) {
        Objects.requireNonNull(itemColor, "color is null!");
        ColorProviderRegistry.ITEM.register(itemColor, unpackItems(items));
    }
    
    @SafeVarargs
    public static void registerBlockColors(BlockColor blockColor, Supplier<Block>... blocks) {
        Objects.requireNonNull(blockColor, "color is null!");
        ColorProviderRegistry.BLOCK.register(blockColor, unpackBlocks(blocks));
    }
    
    private static ItemLike[] unpackItems(Supplier<ItemLike>[] items) {
        ItemLike[] array = new ItemLike[items.length];
        for (int i = 0; i < items.length; i++) {
            array[i] = Objects.requireNonNull(items[i].get());
        }
        return array;
    }
    
    private static Block[] unpackBlocks(Supplier<Block>[] blocks) {
        Block[] array = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            array[i] = Objects.requireNonNull(blocks[i].get());
        }
        return array;
    }
}
