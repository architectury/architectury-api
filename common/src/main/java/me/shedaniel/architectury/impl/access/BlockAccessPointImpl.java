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

package me.shedaniel.architectury.impl.access;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.BlockAccessPoint;
import me.shedaniel.architectury.core.access.specific.BlockAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class BlockAccessPointImpl<T, SELF extends BlockAccessPointImpl<T, SELF>> implements BlockAccessPoint<T, SELF> {
    private final AccessPoint<BlockAccess<T>, ?> parent;
    
    private final Multimap<Block, BlockAccess<T>> blockBasedAccessors = HashMultimap.create();
    private final Multimap<BlockTag, BlockAccess<T>> blockTagBasedAccessors = HashMultimap.create();
    private final Multimap<BlockEntityType<?>, BlockAccess<T>> blockEntityBasedAccessors = HashMultimap.create();
    
    public BlockAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> (level, chunk, pos, state, entity, direction) -> {
            return processIterable(ts, level, chunk, pos, state, entity, direction);
        });
        add((level, chunk, pos, state, entity, direction) -> {
            Collection<BlockAccess<T>> accessors = blockBasedAccessors.get(state.getBlock());
            return processIterable(accessors, level, chunk, pos, state, entity, direction);
        });
        add((level, chunk, pos, state, entity, direction) -> {
            for (Map.Entry<BlockTag, Collection<BlockAccess<T>>> entry : blockTagBasedAccessors.asMap().entrySet()) {
                if (state.is(entry.getKey().tag)) {
                    return processIterable(entry.getValue(), level, chunk, pos, state, entity, direction);
                }
            }
            return null;
        });
        add((level, chunk, pos, state, entity, direction) -> {
            if (entity == null) return null;
            Collection<BlockAccess<T>> accessors = blockEntityBasedAccessors.get(entity.getType());
            return processIterable(accessors, level, chunk, pos, state, entity, direction);
        });
    }
    
    @Override
    public SELF forBlock(Block block, BlockAccess<T> access) {
        blockBasedAccessors.put(block, access);
        return (SELF) this;
    }
    
    @Override
    public SELF forBlock(Tag.Named<Block> tag, BlockAccess<T> access) {
        blockTagBasedAccessors.put(new BlockTag(tag), access);
        return (SELF) this;
    }
    
    @Override
    public SELF forBlockEntity(BlockEntityType<?> type, BlockAccess<T> access) {
        blockEntityBasedAccessors.put(type, access);
        return (SELF) this;
    }
    
    private T processIterable(Iterable<BlockAccess<T>> iterable, Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        for (BlockAccess<T> accessor : iterable) {
            T t = accessor.getByBlock(level, chunk, pos, state, entity, direction);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<BlockAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByBlock(Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        return get().getByBlock(level, chunk, pos, state, entity, direction);
    }
    
    private static class BlockTag {
        private final Tag.Named<Block> tag;
        private final int hash;
        
        public BlockTag(Tag.Named<Block> tag) {
            this.tag = tag;
            this.hash = tag.getName().hashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BlockTag blockTag = (BlockTag) o;
            return Objects.equals(tag.getName(), blockTag.tag.getName());
        }
        
        @Override
        public int hashCode() {
            return hash;
        }
    }
}
