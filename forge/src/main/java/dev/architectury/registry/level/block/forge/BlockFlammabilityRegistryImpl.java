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

package dev.architectury.registry.level.block.forge;

import dev.architectury.registry.ReloadListenerRegistry;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BlockFlammabilityRegistryImpl {
    public static int getBurnOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        return state.getFlammability(level, pos, direction);
    }
    
    public static int getFlameOdds(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        return state.getFireSpreadSpeed(level, pos, direction);
    }
    
    private static final Reference2ObjectMap<Block, Data> DATAS = new Reference2ObjectOpenHashMap<>();
    
    private static Data getData(Block fireBlock) {
        if (fireBlock instanceof FireBlock) {
            return DATAS.computeIfAbsent(fireBlock, $ -> new Data());
        } else {
            throw new IllegalArgumentException("Expected fire block, got " + fireBlock);
        }
    }
    
    private static class Data {
        private final Reference2IntMap<Block> burnOdds = new Reference2IntOpenHashMap<>();
        private final Reference2IntMap<Block> flameOdds = new Reference2IntOpenHashMap<>();
        private final Reference2IntMap<TagKey<Block>> burnTagOdds = new Reference2IntOpenHashMap<>();
        private final Reference2IntMap<TagKey<Block>> flameTagOdds = new Reference2IntOpenHashMap<>();
        private final Reference2IntMap<Block> burnCollectedOdds = new Reference2IntOpenHashMap<>();
        private final Reference2IntMap<Block> flameCollectedOdds = new Reference2IntOpenHashMap<>();
        
        int getBurnOdds(Block block) {
            return burnCollectedOdds.getOrDefault(block, -1);
        }
        
        int getFlameOdds(Block block) {
            return flameCollectedOdds.getOrDefault(block, -1);
        }
    }
    
    static {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, (preparationBarrier, resourceManager, profilerFiller, profiler2, executor, executor2) -> {
            return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
                profiler2.startTick();
                profiler2.push("handle_arch_flammables");
                for (Data data : DATAS.values()) {
                    data.burnCollectedOdds.clear();
                    data.flameCollectedOdds.clear();
                    data.burnCollectedOdds.putAll(data.burnOdds);
                    data.flameCollectedOdds.putAll(data.flameOdds);
                    
                    for (Reference2IntMap.Entry<TagKey<Block>> entry : data.burnTagOdds.reference2IntEntrySet()) {
                        for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(entry.getKey())) {
                            data.burnCollectedOdds.put(holder.value(), entry.getIntValue());
                        }
                    }
                    
                    for (Reference2IntMap.Entry<TagKey<Block>> entry : data.flameTagOdds.reference2IntEntrySet()) {
                        for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(entry.getKey())) {
                            data.flameCollectedOdds.put(holder.value(), entry.getIntValue());
                        }
                    }
                }
                profiler2.pop();
                profiler2.endTick();
            }, executor2);
        });
    }
    
    public static void register(Block fireBlock, int burnOdds, int flameOdds, Block... flammableBlocks) {
        Data data = getData(fireBlock);
        
        for (Block block : flammableBlocks) {
            data.burnOdds.put(block, burnOdds);
            data.flameOdds.put(block, burnOdds);
        }
    }
    
    public static void register(Block fireBlock, int burnOdds, int flameOdds, TagKey<Block> flammableBlocks) {
        Data data = getData(fireBlock);
        
        data.burnTagOdds.put(flammableBlocks, burnOdds);
        data.flameTagOdds.put(flammableBlocks, burnOdds);
    }
    
    public static int handleBurnOddsHook(int previousValue, BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockState fireState = level.getBlockState(pos.relative(direction));
        
        if (fireState.getBlock() instanceof FireBlock fireBlock) {
            int odds = getData(fireBlock).getBurnOdds(state.getBlock());
            if (odds >= 0) {
                return odds;
            }
        }
        
        return previousValue;
    }
    
    public static int handleSpreadOddsHook(int previousValue, BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        BlockState fireState = level.getBlockState(pos.relative(direction));
        
        if (fireState.getBlock() instanceof FireBlock fireBlock) {
            int odds = getData(fireBlock).getFlameOdds(state.getBlock());
            if (odds >= 0) {
                return odds;
            }
        }
        
        return previousValue;
    }
}
