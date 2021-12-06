/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package dev.architectury.transfer.access;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface BlockLookupRegistration<T, Context> {
    /**
     * Registers a lookup registration handler, this is used to provide
     * interop with platform apis.
     *
     * @param id       the id of the lookup registration handler
     * @param provider the provider of the lookup registration handler
     * @return true if the registration was successful
     */
    boolean register(ResourceLocation id, BlockAccessProvider<Function<Context, T>, @Nullable BlockEntity> provider);
    
    boolean registerForBlocks(ResourceLocation id, BlockAccessProvider<Function<Context, T>, @Nullable BlockEntity> provider, Block... blocks);
    
    <B extends BlockEntity> boolean registerForBlockEntities(ResourceLocation id, BlockAccessProvider<Function<Context, T>, B> provider, BlockEntityType<B>... blockEntityTypes);
    
    <B extends BlockEntity> boolean registerForSelf(ResourceLocation id, BlockEntityType<B>... blockEntityTypes);
    
    @FunctionalInterface
    interface BlockAccessProvider<R, B extends BlockEntity> {
        @Nullable
        R get(Level level, BlockPos pos, BlockState state, B blockEntity);
    }
}
