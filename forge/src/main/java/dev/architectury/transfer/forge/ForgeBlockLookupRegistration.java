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

package dev.architectury.transfer.forge;

import dev.architectury.transfer.access.BlockLookupRegistration;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface ForgeBlockLookupRegistration<T, Cap> extends BlockLookupRegistration<T, Direction> {
    static <T, Cap> ForgeBlockLookupRegistration<T, Cap> create(Capability<Cap> capability, BlockAccessProvider<BiFunction<Direction, T, Cap>, BlockEntity> transformer) {
        return new ForgeBlockLookupRegistration<T, Cap>() {
            @Override
            public Capability<Cap> getCapability() {
                return capability;
            }
            
            @Override
            public Cap from(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, Direction direction, T handler) {
                return transformer.get(level, blockPos, blockState, blockEntity).apply(direction, handler);
            }
        };
    }
    
    Capability<Cap> getCapability();
    
    Cap from(Level level, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, Direction direction, T handler);
    
    @Override
    default boolean registerForBlocks(ResourceLocation id, BlockAccessProvider<Function<Direction, T>, @Nullable BlockEntity> provider, Block... blocks) {
        ReferenceOpenHashSet<Block> set = new ReferenceOpenHashSet<>(blocks);
        return register(id, (level, pos, state, blockEntity) -> {
            if (set.contains(state.getBlock())) {
                return provider.get(level, pos, state, blockEntity);
            }
            
            return null;
        });
    }
    
    @Override
    default <B extends BlockEntity> boolean registerForBlockEntities(ResourceLocation id, BlockAccessProvider<Function<Direction, T>, B> provider, BlockEntityType<B>... blockEntityTypes) {
        ReferenceOpenHashSet<BlockEntityType<B>> set = new ReferenceOpenHashSet<>(blockEntityTypes);
        return register(id, (level, pos, state, blockEntity) -> {
            if (set.contains(blockEntity.getType())) {
                return provider.get(level, pos, state, (B) blockEntity);
            }
            
            return null;
        });
    }
    
    @Override
    default <B extends BlockEntity> boolean registerForSelf(ResourceLocation id, BlockEntityType<B>... blockEntityTypes) {
        ReferenceOpenHashSet<BlockEntityType<B>> set = new ReferenceOpenHashSet<>(blockEntityTypes);
        return register(id, (level, pos, state, blockEntity) -> {
            if (set.contains(blockEntity.getType())) {
                return direction -> (T) blockEntity;
            }
            
            return null;
        });
    }
    
    @Override
    default boolean register(ResourceLocation id, BlockAccessProvider<Function<Direction, T>, @Nullable BlockEntity> provider) {
        CapabilitiesAttachListeners.add(event -> {
            if (event.getObject() instanceof BlockEntity) {
                BlockEntity blockEntity = (BlockEntity) event.getObject();
                Function<Direction, T> applicator = provider.get(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity);
                if (applicator != null) {
                    event.addCapability(id, new ICapabilityProvider() {
                        @NotNull
                        @Override
                        public <S> LazyOptional<S> getCapability(@NotNull Capability<S> capability, @Nullable Direction arg) {
                            if (capability == ForgeBlockLookupRegistration.this.getCapability()) {
                                T handler = applicator.apply(arg);
                                
                                return handler == null ? LazyOptional.empty() : LazyOptional.of(() -> from(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity, arg, handler)).cast();
                            }
                            
                            return LazyOptional.empty();
                        }
                    });
                }
            }
        });
        
        return true;
    }
}
