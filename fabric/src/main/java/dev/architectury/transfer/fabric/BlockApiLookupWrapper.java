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

package dev.architectury.transfer.fabric;

import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.access.BlockLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BlockApiLookupWrapper<T, F, C> implements BlockLookup<TransferHandler<T>, C> {
    private final BlockApiLookup<F, C> lookup;
    private final Function<@Nullable F, @Nullable TransferHandler<T>> wrapper;
    
    public BlockApiLookupWrapper(BlockApiLookup<F, C> lookup, Function<@Nullable F, @Nullable TransferHandler<T>> wrapper) {
        this.lookup = lookup;
        this.wrapper = wrapper;
    }
    
    @Override
    @Nullable
    public TransferHandler<T> get(Level level, BlockPos pos, C context) {
        return wrapper.apply(lookup.find(level, pos, context));
    }
    
    @Override
    @Nullable
    public TransferHandler<T> get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context) {
        if (blockEntity != null) {
            return wrapper.apply(lookup.find(level, pos, state, blockEntity, context));
        } else {
            return get(level, pos, context);
        }
    }
}
