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

package dev.architectury.registry.level.block.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFlammabilityRegistryImpl {
    public static int getBurnOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        BlockState fireState = level.getBlockState(pos.relative(direction));
        
        if (fireState.getBlock() instanceof FireBlock fireBlock) {
            FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getInstance(fireBlock).get(state.getBlock());
            return entry.getBurnChance();
        } else {
            FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
            return entry.getBurnChance();
        }
    }
    
    public static int getFlameOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        BlockState fireState = level.getBlockState(pos.relative(direction));
        
        if (fireState.getBlock() instanceof FireBlock fireBlock) {
            FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getInstance(fireBlock).get(state.getBlock());
            return entry.getSpreadChance();
        } else {
            FlammableBlockRegistry.Entry entry = FlammableBlockRegistry.getDefaultInstance().get(state.getBlock());
            return entry.getSpreadChance();
        }
    }
    
    public static void register(Block fireBlock, int burnOdds, int flameOdds, Block... flammableBlocks) {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getInstance(fireBlock);
        for (Block block : flammableBlocks) {
            registry.add(block, burnOdds, flameOdds);
        }
    }
    
    public static void register(Block fireBlock, int burnOdds, int flameOdds, TagKey<Block> flammableBlocks) {
        FlammableBlockRegistry registry = FlammableBlockRegistry.getInstance(fireBlock);
        registry.add(flammableBlocks, burnOdds, flameOdds);
    }
}
