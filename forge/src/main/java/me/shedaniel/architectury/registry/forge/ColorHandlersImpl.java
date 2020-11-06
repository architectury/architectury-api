/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.registry.ColorHandlers;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ColorHandlersImpl implements ColorHandlers.Impl {
    private static final List<Pair<IItemColor, IItemProvider[]>> ITEM_COLORS = Lists.newArrayList();
    private static final List<Pair<IBlockColor, Block[]>> BLOCK_COLORS = Lists.newArrayList();
    
    public ColorHandlersImpl() {
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
    
    @Override
    public void registerItemColors(IItemColor itemColor, IItemProvider... items) {
        if (Minecraft.getInstance().getItemColors() == null) {
            ITEM_COLORS.add(Pair.of(itemColor, items));
        } else {
            Minecraft.getInstance().getItemColors().register(itemColor, items);
        }
    }
    
    @Override
    public void registerBlockColors(IBlockColor blockColor, Block... blocks) {
        if (Minecraft.getInstance().getBlockColors() == null) {
            BLOCK_COLORS.add(Pair.of(blockColor, blocks));
        } else {
            Minecraft.getInstance().getBlockColors().register(blockColor, blocks);
        }
    }
}
