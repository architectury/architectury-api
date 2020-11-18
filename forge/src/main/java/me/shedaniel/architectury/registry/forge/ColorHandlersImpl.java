/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ColorHandlersImpl {
    private static final List<Pair<IItemColor, IItemProvider[]>> ITEM_COLORS = Lists.newArrayList();
    private static final List<Pair<IBlockColor, Block[]>> BLOCK_COLORS = Lists.newArrayList();
    
    static {
        MinecraftForge.EVENT_BUS.<ColorHandlerEvent.Item>addListener(event -> {
            for (Pair<IItemColor, IItemProvider[]> pair : ITEM_COLORS) {
                event.getItemColors().register(pair.getLeft(), pair.getRight());
            }
        });
        MinecraftForge.EVENT_BUS.<ColorHandlerEvent.Block>addListener(event -> {
            for (Pair<IBlockColor, Block[]> pair : BLOCK_COLORS) {
                event.getBlockColors().register(pair.getLeft(), pair.getRight());
            }
        });
    }
    
    public static void registerItemColors(IItemColor itemColor, IItemProvider... items) {
        if (Minecraft.getInstance().getItemColors() == null) {
            ITEM_COLORS.add(Pair.of(itemColor, items));
        } else {
            Minecraft.getInstance().getItemColors().register(itemColor, items);
        }
    }
    
    public static void registerBlockColors(IBlockColor blockColor, Block... blocks) {
        if (Minecraft.getInstance().getBlockColors() == null) {
            BLOCK_COLORS.add(Pair.of(blockColor, blocks));
        } else {
            Minecraft.getInstance().getBlockColors().register(blockColor, blocks);
        }
    }
}
