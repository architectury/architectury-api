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
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ArchitecturyFlowingFluid extends FlowingFluid {
    private final ArchitecturyFluidProperties properties;
    
    protected ArchitecturyFlowingFluid(ArchitecturyFluidProperties properties) {
        this.properties = properties;
        if (Platform.isFabric()) {
            addFabricFluidAttributes(this, properties);
        }
    }
    
    @ExpectPlatform
    private static void addFabricFluidAttributes(FlowingFluid fluid, ArchitecturyFluidProperties properties) {
        throw new AssertionError();
    }
    
    @Override
    public Fluid getFlowing() {
        return properties.getFlowingFluid().get();
    }
    
    @Override
    public Fluid getSource() {
        return properties.getSourceFluid().get();
    }
    
    @Override
    protected boolean canConvertToSource() {
        return properties.canConvertToSource();
    }
    
    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        // Same implementation as in WaterFluid.
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }
    
    @Override
    protected int getSlopeFindDistance(LevelReader level) {
        return properties.getSlopeFindDistance();
    }
    
    @Override
    protected int getDropOff(LevelReader level) {
        return properties.getDropOff();
    }
    
    @Override
    public Item getBucket() {
        RegistrySupplier<Item> item = properties.getBucketItem();
        return item == null ? Items.AIR : item.orElse(Items.AIR);
    }
    
    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        // Same implementation as in WaterFluid.
        return direction == Direction.DOWN && !this.isSame(fluid);
    }
    
    @Override
    public int getTickDelay(LevelReader level) {
        return properties.getTickDelay();
    }
    
    @Override
    protected float getExplosionResistance() {
        return properties.getExplosionResistance();
    }
    
    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        RegistrySupplier<? extends LiquidBlock> block = properties.getBlock();
        if (block == null || !block.isPresent()) return Blocks.AIR.defaultBlockState();
        return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }
    
    @NotNull
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.ofNullable(properties.getAttributes().getFillSound());
    }
    
    public static class Source extends ArchitecturyFlowingFluid {
        public Source(ArchitecturyFluidProperties properties) {
            super(properties);
        }
        
        @Override
        public int getAmount(FluidState state) {
            return 8;
        }
        
        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
    
    public static class Flowing extends ArchitecturyFlowingFluid {
        public Flowing(ArchitecturyFluidProperties properties) {
            super(properties);
            this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 7));
        }
        
        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
        
        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }
        
        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }
}
