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

package dev.architectury.core.fluid.forge.imitator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ArchitecturyFlowingFluid extends ForgeFlowingFluid {
    private final ArchitecturyFluidAttributes attributes;
    private final Supplier<FluidType> forgeType;
    
    ArchitecturyFlowingFluid(ArchitecturyFluidAttributes attributes) {
        super(toForgeProperties(attributes));
        this.attributes = attributes;
        this.forgeType = Suppliers.memoize(() -> {
            return new ArchitecturyFluidAttributesForge(FluidType.Properties.create(), this, attributes);
        });
    }
    
    private static Properties toForgeProperties(ArchitecturyFluidAttributes attributes) {
        Properties forge = new Properties(Suppliers.memoize(() -> {
            return new ArchitecturyFluidAttributesForge(FluidType.Properties.create(), attributes.getSourceFluid(), attributes);
        }), attributes::getSourceFluid, attributes::getFlowingFluid);
        forge.slopeFindDistance(attributes.getSlopeFindDistance());
        forge.levelDecreasePerBlock(attributes.getDropOff());
        forge.bucket(() -> MoreObjects.firstNonNull(attributes.getBucketItem(), Items.AIR));
        forge.tickRate(attributes.getTickDelay());
        forge.explosionResistance(attributes.getExplosionResistance());
        forge.block(() -> MoreObjects.firstNonNull(attributes.getBlock(), (LiquidBlock) Blocks.WATER));
        return forge;
    }
    
    @Override
    public FluidType getFluidType() {
        return forgeType.get();
    }
    
    @Override
    public Fluid getFlowing() {
        return attributes.getFlowingFluid();
    }
    
    @Override
    public Fluid getSource() {
        return attributes.getSourceFluid();
    }
    
    @Override
    protected boolean canConvertToSource() {
        return attributes.canConvertToSource();
    }
    
    @Override
    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        // Same implementation as in WaterFluid.
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
    public Item getBucket() {
        Item item = attributes.getBucketItem();
        return item == null ? Items.AIR : item;
    }
    
    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        // Same implementation as in WaterFluid.
        return direction == Direction.DOWN && !this.isSame(fluid);
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
    
    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == getSource() || fluid == getFlowing();
    }
    
    public static class Source extends ArchitecturyFlowingFluid {
        public Source(ArchitecturyFluidAttributes attributes) {
            super(attributes);
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
        public Flowing(ArchitecturyFluidAttributes attributes) {
            super(attributes);
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
