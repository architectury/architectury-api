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

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class ArchitecturyFluidProperties {
    private final RegistrySupplier<Fluid> flowingFluid;
    private final RegistrySupplier<Fluid> sourceFluid;
    private boolean canConvertToSource = false;
    private int slopeFindDistance = 4;
    private int dropOff = 1;
    private RegistrySupplier<Item> bucketItem;
    private int tickDelay = 5;
    private float explosionResistance = 100.0F;
    private RegistrySupplier<? extends LiquidBlock> block;
    private final ArchitecturyFluidAttributes attributes;
    
    public ArchitecturyFluidProperties(RegistrySupplier<Fluid> flowingFluid, RegistrySupplier<Fluid> sourceFluid, ArchitecturyFluidAttributes attributes) {
        this.flowingFluid = flowingFluid;
        this.sourceFluid = sourceFluid;
        this.attributes = attributes;
    }
    
    public ArchitecturyFluidProperties convertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;
        return this;
    }
    
    public ArchitecturyFluidProperties slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;
        return this;
    }
    
    public ArchitecturyFluidProperties dropOff(int dropOff) {
        this.dropOff = dropOff;
        return this;
    }
    
    public ArchitecturyFluidProperties bucketItem(RegistrySupplier<Item> bucketItem) {
        this.bucketItem = bucketItem;
        return this;
    }
    
    public ArchitecturyFluidProperties tickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }
    
    public ArchitecturyFluidProperties explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }
    
    public ArchitecturyFluidProperties block(RegistrySupplier<? extends LiquidBlock> block) {
        this.block = block;
        return this;
    }
    
    public RegistrySupplier<Fluid> getFlowingFluid() {
        return flowingFluid;
    }
    
    public RegistrySupplier<Fluid> getSourceFluid() {
        return sourceFluid;
    }
    
    public boolean canConvertToSource() {
        return canConvertToSource;
    }
    
    public int getSlopeFindDistance() {
        return slopeFindDistance;
    }
    
    public int getDropOff() {
        return dropOff;
    }
    
    @Nullable
    public RegistrySupplier<Item> getBucketItem() {
        return bucketItem;
    }
    
    public int getTickDelay() {
        return tickDelay;
    }
    
    public float getExplosionResistance() {
        return explosionResistance;
    }
    
    @Nullable
    public RegistrySupplier<? extends LiquidBlock> getBlock() {
        return block;
    }
    
    public ArchitecturyFluidAttributes getAttributes() {
        return attributes;
    }
}
