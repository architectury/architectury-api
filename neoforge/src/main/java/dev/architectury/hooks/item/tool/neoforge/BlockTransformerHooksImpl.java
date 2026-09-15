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

package dev.architectury.hooks.item.tool.neoforge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockTransformerHooksImpl {
    private static final Map<Block, Block> STRIPPABLES = new ConcurrentHashMap<>();
    private static final Map<Block, BlockState> FLATTENABLES = new ConcurrentHashMap<>();
    private static final Map<Block, BlockState> TILLABLES = new ConcurrentHashMap<>();
    
    public static void addStrippable(Block input, Block result) {
        STRIPPABLES.put(input, result);
    }
    
    public static void addFlattenable(Block input, BlockState result) {
        FLATTENABLES.put(input, result);
    }
    
    public static void addTillable(Block input, BlockState result) {
        TILLABLES.put(input, result);
    }
    
    @Nullable
    public static BlockState resolve(UseOnContext context) {
        Holder<BlockTransformer> transformer = context.getItemInHand().get(DataComponents.BLOCK_TRANSFORMER);
        if (transformer == null) return null;
        
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        
        if (transformer.is(BlockTransformers.AXE)) {
            Block result = STRIPPABLES.get(state.getBlock());
            // Carries over the properties the two blocks share, as the stripping the vanilla axe does.
            return result == null ? null : result.withPropertiesOf(state);
        } else if (transformer.is(BlockTransformers.SHOVEL)) {
            return clearAbove(FLATTENABLES, level, pos, state);
        } else if (transformer.is(BlockTransformers.HOE)) {
            return clearAbove(TILLABLES, level, pos, state);
        }
        
        return null;
    }
    
    @Nullable
    private static BlockState clearAbove(Map<Block, BlockState> transforms, Level level, BlockPos pos, BlockState state) {
        BlockState result = transforms.get(state.getBlock());
        if (result == null || !level.getBlockState(pos.above()).isAir()) return null;
        return result;
    }
}
