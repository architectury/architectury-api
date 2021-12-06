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

package dev.architectury.impl.transfer.access;

import dev.architectury.transfer.access.BlockLookup;
import dev.architectury.transfer.access.BlockLookupAccess;
import dev.architectury.transfer.access.BlockLookupRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockLookupAccessImpl<T, C> implements BlockLookupAccess<T, C> {
    private final List<BlockLookup<T, C>> lookups = new ArrayList<>();
    private final List<BlockLookupRegistration<T, C>> registrationHandlers = new ArrayList<>();
    
    @Override
    public void addQueryHandler(BlockLookup<T, C> handler) {
        this.lookups.add(0, handler);
    }
    
    @Override
    public void addRegistrationHandler(BlockLookupRegistration<T, C> registration) {
        this.registrationHandlers.add(0, registration);
    }
    
    @Override
    @Nullable
    public T get(Level level, BlockPos pos, C context) {
        for (BlockLookup<T, C> handler : lookups) {
            T result = handler.get(level, pos, context);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }
    
    @Override
    @Nullable
    public T get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context) {
        for (BlockLookup<T, C> handler : lookups) {
            T result = handler.get(level, pos, state, blockEntity, context);
            if (result != null) {
                return result;
            }
        }
        
        return null;
    }
    
    @Override
    public boolean register(ResourceLocation id, BlockAccessProvider<Function<C, T>, @Nullable BlockEntity> provider) {
        for (BlockLookupRegistration<T, C> handler : registrationHandlers) {
            if (handler.register(id, provider)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public boolean registerForBlocks(ResourceLocation id, BlockAccessProvider<Function<C, T>, @Nullable BlockEntity> provider, Block... blocks) {
        for (BlockLookupRegistration<T, C> handler : registrationHandlers) {
            if (handler.registerForBlocks(id, provider, blocks)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public <B extends BlockEntity> boolean registerForBlockEntities(ResourceLocation id, BlockAccessProvider<Function<C, T>, B> provider, BlockEntityType<B>... blockEntityTypes) {
        for (BlockLookupRegistration<T, C> handler : registrationHandlers) {
            if (handler.registerForBlockEntities(id, provider, blockEntityTypes)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    public <B extends BlockEntity> boolean registerForSelf(ResourceLocation id, BlockEntityType<B>... blockEntityTypes) {
        for (BlockLookupRegistration<T, C> handler : registrationHandlers) {
            if (handler.registerForSelf(id, blockEntityTypes)) {
                return true;
            }
        }
        
        return false;
    }
}
