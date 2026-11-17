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

package dev.architectury.core.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A fluid backed by {@link ArchitecturyFluidAttributes}.
 * <p>
 * Since 26.4, a fluid is a single {@link Fluid} whose states hold both the source and the flowing
 * forms, through the {@link FlowingFluid#SOURCE} and {@link FlowingFluid#LEVEL} properties.
 * Register one instance of this class per fluid.
 */
public class ArchitecturyFlowingFluid extends FlowingFluid {
    private final ArchitecturyFluidAttributes attributes;
    
    public ArchitecturyFlowingFluid(ArchitecturyFluidAttributes attributes) {
        super(checkPlatform(Fluid.Properties.of()));
        this.attributes = attributes;
        if (Platform.isFabric()) {
            addFabricFluidAttributes(this, attributes);
        }
    }
    
    private static <T> T checkPlatform(T obj) {
        if (Platform.isNeoForge()) {
            throw new IllegalStateException("This class should've been replaced on NeoForge!");
        }
        
        return obj;
    }
    
    
    @ExpectPlatform
    private static void addFabricFluidAttributes(FlowingFluid fluid, ArchitecturyFluidAttributes properties) {
        throw new AssertionError();
    }
    
    @Override
    protected boolean canConvertToSource(ServerLevel level) {
        return attributes.canConvertToSource();
    }
    
    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }
    
    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return attributes.getSlopeFindDistance(level);
    }
    
    @Override
    protected int getDropOff(LevelReader level) {
        return attributes.getDropOff(level);
    }
    
    @Override
    public ResourceKey<Item> getBucket() {
        Item item = attributes.getBucketItem();
        return BuiltInRegistries.ITEM.getResourceKey(item == null ? Items.AIR : item)
                .orElseGet(() -> BuiltInRegistries.ITEM.getResourceKey(Items.AIR).orElseThrow());
    }
    
    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, FluidState other, Direction direction) {
        // Same implementation as in WaterFluid.
        return direction == Direction.DOWN && !other.is(this);
    }
    
    @Override
    public int getTickDelay(LevelReader level) {
        return attributes.getTickDelay(level);
    }
    
    @Override
    protected float getExplosionResistance() {
        return attributes.getExplosionResistance();
    }
    
    @Override
    public float getDrag() {
        return attributes.getDrag();
    }
    
    @Override
    public FogType getFogType() {
        return attributes.getFogType();
    }
    
    @Override
    public float getFallDistanceMultiplier() {
        return attributes.getFallDistanceMultiplier();
    }
    
    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        LiquidBlock block = attributes.getBlock();
        if (block == null) return Blocks.AIR.defaultBlockState();
        return block.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }
    
    @NotNull
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(attributes.getFillSound());
    }
}
