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

import dev.architectury.transfer.access.BlockLookupRegistration;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

public class FabricBlockLookupRegistration<T, A, Context> implements BlockLookupRegistration<T, Context> {
    private final Function<T, A> unwrapper;
    private final BlockApiLookup<A, Context> lookup;
    
    public static <T, A, Context> FabricBlockLookupRegistration<T, A, Context> create(BlockApiLookup<A, Context> lookup, Function<T, A> unwrapper) {
        return new FabricBlockLookupRegistration<>(unwrapper, lookup);
    }
    
    private FabricBlockLookupRegistration(Function<T, A> unwrapper, BlockApiLookup<A, Context> lookup) {
        this.unwrapper = unwrapper;
        this.lookup = lookup;
    }
    
    public BlockApiLookup.BlockApiProvider<A, Context> provider(BlockAccessProvider<Function<Context, T>, BlockEntity> provider) {
        return (level, pos, state, blockEntity, context) -> {
            Function<Context, T> function = provider.get(level, pos, state, blockEntity);
            if (function != null) {
                return unwrapper.apply(function.apply(context));
            }
            
            return null;
            
        };
    }
    
    public <B extends BlockEntity> BlockApiLookup.BlockEntityApiProvider<A, Context> provider(Predicate<@Nullable BlockEntityType<?>> beTypePredicate, BlockAccessProvider<Function<Context, T>, B> provider) {
        return (blockEntity, context) -> {
            if (!beTypePredicate.test(blockEntity == null ? null : blockEntity.getType())) {
                return null;
            }
            
            Function<Context, T> function = provider.get(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), (B) blockEntity);
            if (function != null) {
                return unwrapper.apply(function.apply(context));
            }
            
            return null;
        };
    }
    
    @Override
    public boolean register(ResourceLocation id, BlockAccessProvider<Function<Context, T>, @Nullable BlockEntity> provider) {
        lookup.registerFallback(provider(provider));
        return true;
    }
    
    @Override
    public boolean registerForBlocks(ResourceLocation id, BlockAccessProvider<Function<Context, T>, @Nullable BlockEntity> provider, Block... blocks) {
        lookup.registerForBlocks(provider(provider), blocks);
        return true;
    }
    
    @Override
    public <B extends BlockEntity> boolean registerForBlockEntities(ResourceLocation id, BlockAccessProvider<Function<Context, T>, B> provider, BlockEntityType<B>... blockEntityTypes) {
        lookup.registerForBlockEntities(provider(type -> true, provider), blockEntityTypes);
        return true;
    }
    
    @Override
    public <B extends BlockEntity> boolean registerForSelf(ResourceLocation id, BlockEntityType<B>... blockEntityTypes) {
        lookup.registerSelf(blockEntityTypes);
        return false;
    }
}
