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

package dev.architectury.hooks.block;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.types.Type;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class BlockEntityHooks {
    private BlockEntityHooks() {
    }
    
    /**
     * @deprecated Use the Builder from vanilla directly.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static <T extends BlockEntity> TypeBuilder<T> builder(Constructor<? extends T> constructor, Block... blocks) {
        return new TypeBuilder<>(constructor, ImmutableSet.copyOf(blocks));
    }
    
    public static class TypeBuilder<T extends BlockEntity> {
        private final Constructor<? extends T> constructor;
        private final Set<Block> validBlocks;
        
        private TypeBuilder(Constructor<? extends T> constructor, Set<Block> validBlocks) {
            this.constructor = constructor;
            this.validBlocks = validBlocks;
        }
        
        public BlockEntityType<T> build(@Nullable Type<?> type) {
            return new BlockEntityType<>(this.constructor::create, this.validBlocks, type);
        }
    }
    
    /**
     * Sync data to the clients.
     */
    @ExpectPlatform
    public static void syncData(BlockEntity entity) {
        throw new AssertionError();
    }
    
    @FunctionalInterface
    public interface Constructor<T extends BlockEntity> {
        T create(BlockPos pos, BlockState state);
    }
}
