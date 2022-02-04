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

package dev.architectury.transfer.access.forge;

import dev.architectury.transfer.access.BlockLookup;
import dev.architectury.transfer.access.BlockLookupAccess;
import dev.architectury.transfer.forge.ForgeBlockLookupRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PlatformLookupImpl {
    public static <O, T> void attachBlockQuery(BlockLookupAccess<T, Direction> access, Object lookup, Function<O, T> wrapper) {
        if (!(lookup instanceof Capability)) {
            throw new IllegalArgumentException("Lookup must be an instance of Capability on Forge!");
        }
        access.addQueryHandler(instantiateBlockLookup((Capability<O>) lookup, wrapper));
    }
    
    public static <O, T> void attachBlockRegistration(BlockLookupAccess<T, Direction> access, Object lookup, BiFunction<T, Direction, O> unwrapper) {
        if (!(lookup instanceof Capability)) {
            throw new IllegalArgumentException("Lookup must be an instance of Capability on Forge!");
        }
        access.addRegistrationHandler(ForgeBlockLookupRegistration.create((Capability<O>) lookup,
                (level, pos, state, blockEntity) -> unwrapper));
    }
    
    public static <O, T> BlockLookup<T, Direction> instantiateBlockLookup(Capability<O> capability, Function<O, T> wrapper) {
        return new BlockLookup<T, Direction>() {
            @Override
            @Nullable
            public T get(Level level, BlockPos pos, Direction direction) {
                return get(level, pos, level.getBlockState(pos), null, direction);
            }
            
            @Override
            @Nullable
            public T get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                Block block = state.getBlock();
                O handler = null;
                if (state.hasBlockEntity()) {
                    if (blockEntity == null) {
                        blockEntity = level.getBlockEntity(pos);
                    }
                    if (blockEntity != null) {
                        handler = blockEntity.getCapability(capability, direction).resolve().orElse(null);
                    }
                }
                return wrapper.apply(handler);
            }
        };
    }
}
