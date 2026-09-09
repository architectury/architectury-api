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

package dev.architectury.hooks.item.tool.fabric;

import net.fabricmc.fabric.api.item.v1.BlockTransformerHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BlockTransformerHooksImpl {
    public static void addStrippable(Block input, Block result) {
        BlockTransformerHelper.registerStripping(input, BlockStateProvider.of(result));
    }
    
    public static void addFlattenable(Block input, BlockState result) {
        BlockTransformerHelper.registerFlattening(input, result);
    }
    
    public static void addTillable(Block input, BlockState result) {
        BlockTransformerHelper.registerTilling(input, result);
    }
}
