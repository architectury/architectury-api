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

import dev.architectury.transfer.TransferAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public interface BlockTransferAccess<T, C> extends TransferAccess<T> {
    @Nullable
    T get(Level level, BlockPos pos, C context);
    
    @Nullable
    T get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context);
    
    void register(ResourceLocation id, BlockAccessProvider<T, C> provider);
    
    @FunctionalInterface
    interface BlockAccessProvider<T, C> {
        @Nullable
        Function<C, T> get(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity);
    }
}
