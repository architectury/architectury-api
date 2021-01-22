/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class ColorHandlers {
    private ColorHandlers() {}
    
    public static void registerItemColors(ItemColor color, ItemLike... items) {
        registerItemColors(color, Arrays.stream(items)
                .map(item -> (Supplier<ItemLike>) () -> item)
                .toArray(Supplier[]::new));
    }
    
    public static void registerBlockColors(BlockColor color, Block... blocks) {
        registerBlockColors(color, Arrays.stream(blocks)
                .map(block -> (Supplier<Block>) () -> block)
                .toArray(Supplier[]::new));
    }
    
    @SafeVarargs
    @ExpectPlatform
    public static void registerItemColors(ItemColor color, Supplier<ItemLike>... items) {
        throw new AssertionError();
    }
    
    @SafeVarargs
    @ExpectPlatform
    public static void registerBlockColors(BlockColor color, Supplier<Block>... blocks) {
        throw new AssertionError();
    }
}
