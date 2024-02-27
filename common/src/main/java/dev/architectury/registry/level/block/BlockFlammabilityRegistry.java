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

package dev.architectury.registry.level.block;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockFlammabilityRegistry {
    /**
     * Gets the burn odds for the given block.
     *
     * @param level     the level
     * @param state     the block state
     * @param pos       the position of the block
     * @param direction the direction of where the fire is coming from
     * @return the burn odds
     */
    @ExpectPlatform
    public static int getBurnOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        throw new AssertionError();
    }
    
    /**
     * Gets the flame odds for the given block.
     *
     * @param level     the level
     * @param state     the block state
     * @param pos       the position of the block
     * @param direction the direction of where the fire is spreading from
     * @return the flame odds
     */
    @ExpectPlatform
    public static int getFlameOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        throw new AssertionError();
    }
    
    /**
     * Registers the flammability for the given blocks for a given fire block.
     *
     * @param fireBlock       the specific fire block
     * @param burnOdds        the burn odds
     * @param flameOdds       the flame odds
     * @param flammableBlocks the flammable blocks
     */
    @ExpectPlatform
    public static void register(Block fireBlock, int burnOdds, int flameOdds, Block... flammableBlocks) {
        throw new AssertionError();
    }
    
    /**
     * Registers the flammability for a given block tag for a given fire block.
     *
     * @param fireBlock       the specific fire block
     * @param burnOdds        the burn odds
     * @param flameOdds       the flame odds
     * @param flammableBlocks the flammable block tag
     */
    @ExpectPlatform
    public static void register(Block fireBlock, int burnOdds, int flameOdds, TagKey<Block> flammableBlocks) {
        throw new AssertionError();
    }
}
