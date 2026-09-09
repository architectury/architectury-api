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

package dev.architectury.hooks.item.tool;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Adds interactions to the vanilla axe, shovel and hoe
 * {@linkplain net.minecraft.core.component.BlockTransformer block transformers}.
 *
 * <p>Since 26.3 these interactions are data driven, and flattening and tilling are additionally tag driven:
 * adding a block to {@code minecraft:turns_into_dirt_path} or {@code minecraft:turns_into_farmland} in a data
 * pack is enough to make it flattenable or tillable, needs no code at all, and behaves identically on every
 * loader. Prefer that; {@link #addFlattenable} and {@link #addTillable} exist for the cases those tags cannot
 * express, namely a result state other than dirt path or farmland.
 *
 * <p>Stripping has no such tag, because every input block maps to a different result, so {@link #addStrippable}
 * is the only way to add one without replacing the whole vanilla axe transformer.
 */
public final class BlockTransformerHooks {
    private BlockTransformerHooks() {
    }
    
    /**
     * Adds a stripping (interact with axe) interaction to the game.
     *
     * <p>Block state properties shared by the two blocks, such as
     * {@link net.minecraft.world.level.block.state.properties.BlockStateProperties#AXIS AXIS}, are carried over
     * from the input to the result. Neither block is required to have any particular property.
     *
     * @param input  input block
     * @param result result block
     */
    @ExpectPlatform
    public static void addStrippable(Block input, Block result) {
        throw new AssertionError();
    }
    
    /**
     * Adds a flattening (interact with shovel) interaction to the game.
     *
     * <p>Prefer the {@code minecraft:turns_into_dirt_path} block tag unless you need a result state other than
     * {@linkplain net.minecraft.world.level.block.Blocks#DIRT_PATH dirt path}. Blocks can only be flattened if
     * they have no block above them.
     *
     * @param input  input block
     * @param result result block state
     */
    @ExpectPlatform
    public static void addFlattenable(Block input, BlockState result) {
        throw new AssertionError();
    }
    
    /**
     * Adds a tilling (interact with hoe) interaction to the game.
     *
     * <p>Prefer the {@code minecraft:turns_into_farmland} block tag unless you need a result state other than
     * {@linkplain net.minecraft.world.level.block.Blocks#FARMLAND farmland}. Blocks can only be tilled if they
     * have no block above them.
     *
     * @param input  input block
     * @param result result block state
     */
    @ExpectPlatform
    public static void addTillable(Block input, BlockState result) {
        throw new AssertionError();
    }
}
