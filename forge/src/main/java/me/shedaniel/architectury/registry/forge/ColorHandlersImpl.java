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

package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.ColorHandlerEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ColorHandlersImpl {
    private static final List<Pair<ItemColor, Supplier<ItemLike>[]>> ITEM_COLORS = Lists.newArrayList();
    private static final List<Pair<BlockColor, Supplier<Block>[]>> BLOCK_COLORS = Lists.newArrayList();
    
    static {
        EventBuses.onRegistered(ArchitecturyForge.MOD_ID, bus -> {
            bus.<ColorHandlerEvent.Item>addListener(event -> {
                for (Pair<ItemColor, Supplier<ItemLike>[]> pair : ITEM_COLORS) {
                    event.getItemColors().register(pair.getLeft(), unpackItems(pair.getRight()));
                }
            });
            bus.<ColorHandlerEvent.Block>addListener(event -> {
                for (Pair<BlockColor, Supplier<Block>[]> pair : BLOCK_COLORS) {
                    event.getBlockColors().register(pair.getLeft(), unpackBlocks(pair.getRight()));
                }
            });
        });
    }
    
    @SafeVarargs
    public static void registerItemColors(ItemColor itemColor, Supplier<ItemLike>... items) {
        Objects.requireNonNull(itemColor, "color is null!");
        if (Minecraft.getInstance().getItemColors() == null) {
            ITEM_COLORS.add(Pair.of(itemColor, items));
        } else {
            Minecraft.getInstance().getItemColors().register(itemColor, unpackItems(items));
        }
    }
    
    @SafeVarargs
    public static void registerBlockColors(BlockColor blockColor, Supplier<Block>... blocks) {
        Objects.requireNonNull(blockColor, "color is null!");
        if (Minecraft.getInstance().getBlockColors() == null) {
            BLOCK_COLORS.add(Pair.of(blockColor, blocks));
        } else {
            Minecraft.getInstance().getBlockColors().register(blockColor, unpackBlocks(blocks));
        }
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
