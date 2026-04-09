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

package dev.architectury.registry.client.rendering.forge;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ColorHandlerRegistryImpl {
    private static final List<Pair<BlockTintSource, Supplier<? extends Block>[]>> BLOCK_COLORS = Lists.newArrayList();

    static {
        FMLJavaModLoadingContext.get().getModBusGroup().register(MethodHandles.lookup(), ColorHandlerRegistryImpl.class);
    }

    @SubscribeEvent
    public static void onBlockColorEvent(RegisterColorHandlersEvent.Block event) {
        for (Pair<BlockTintSource, Supplier<? extends Block>[]> pair : BLOCK_COLORS) {
            event.register(List.of(pair.getLeft()), unpackBlocks(pair.getRight()));
        }
    }

    @SafeVarargs
    public static void registerBlockColors(BlockTintSource blockColor, Supplier<? extends Block>... blocks) {
        Objects.requireNonNull(blockColor, "color is null!");
        if (Minecraft.getInstance().getBlockColors() == null) {
            BLOCK_COLORS.add(Pair.of(blockColor, blocks));
        } else {
            Minecraft.getInstance().getBlockColors().register(List.of(blockColor), unpackBlocks(blocks));
        }
    }

    private static Block[] unpackBlocks(Supplier<? extends Block>[] blocks) {
        Block[] array = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            array[i] = Objects.requireNonNull(blocks[i].get());
        }
        return array;
    }
}
