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

import com.google.common.base.Suppliers;
import dev.architectury.fluid.FluidStack;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SimpleArchitecturyFluidAttributes implements ArchitecturyFluidAttributes {
    private final RegistrySupplier<Fluid> flowingFluid;
    private final RegistrySupplier<Fluid> sourceFluid;
    private boolean canConvertToSource = false;
    private int slopeFindDistance = 4;
    private int dropOff = 1;
    @Nullable
    private RegistrySupplier<Item> bucketItem;
    private int tickDelay = 5;
    private float explosionResistance = 100.0F;
    @Nullable
    private RegistrySupplier<? extends LiquidBlock> block;
    @Nullable
    private ResourceLocation sourceTexture;
    @Nullable
    private ResourceLocation flowingTexture;
    private int color = 0xffffff;
    private int luminosity = 0;
    private int density = 1000;
    private int temperature = 300;
    private int viscosity = 1000;
    private boolean lighterThanAir = false;
    private Rarity rarity = Rarity.COMMON;
    @Nullable
    private SoundEvent fillSound = SoundEvents.BUCKET_FILL;
    @Nullable
    private SoundEvent emptySound = SoundEvents.BUCKET_EMPTY;
    private final Supplier<String> defaultTranslationKey = Suppliers.memoize(() -> Util.makeDescriptionId("fluid", Registries.getId(getSourceFluid(), Registry.FLUID_REGISTRY)));
    
    public SimpleArchitecturyFluidAttributes(RegistrySupplier<Fluid> flowingFluid, RegistrySupplier<Fluid> sourceFluid) {
        this.flowingFluid = flowingFluid;
        this.sourceFluid = sourceFluid;
    }
    
    public SimpleArchitecturyFluidAttributes convertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes dropOff(int dropOff) {
        this.dropOff = dropOff;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes bucketItem(RegistrySupplier<Item> bucketItem) {
        this.bucketItem = bucketItem;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes tickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes block(RegistrySupplier<? extends LiquidBlock> block) {
        this.block = block;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes sourceTexture(ResourceLocation sourceTexture) {
        this.sourceTexture = sourceTexture;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes flowingTexture(ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes color(int color) {
        this.color = color;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes luminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes density(int density) {
        this.density = density;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes lighterThanAir(boolean lighterThanAir) {
        this.lighterThanAir = lighterThanAir;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes fillSound(SoundEvent fillSound) {
        this.fillSound = fillSound;
        return this;
    }
    
    public SimpleArchitecturyFluidAttributes emptySound(SoundEvent emptySound) {
        this.emptySound = emptySound;
        return this;
    }
    
    @Override
    @Nullable
    public String getTranslationKey(@Nullable FluidStack stack) {
        return defaultTranslationKey.get();
    }
    
    @Override
    public final Fluid getFlowingFluid() {
        return flowingFluid.get();
    }
    
    @Override
    public final Fluid getSourceFluid() {
        return sourceFluid.get();
    }
    
    @Override
    public boolean canConvertToSource() {
        return canConvertToSource;
    }
    
    @Override
    public int getSlopeFindDistance(@Nullable LevelReader level) {
        return slopeFindDistance;
    }
    
    @Override
    public int getDropOff(@Nullable LevelReader level) {
        return dropOff;
    }
    
    @Override
    @Nullable
    public Item getBucketItem() {
        return bucketItem == null ? null : bucketItem.orElse(null);
    }
    
    @Override
    public int getTickDelay(@Nullable LevelReader level) {
        return tickDelay;
    }
    
    @Override
    public float getExplosionResistance() {
        return explosionResistance;
    }
    
    @Override
    @Nullable
    public LiquidBlock getBlock() {
        return block == null ? null : block.orElse(null);
    }
    
    @Override
    public ResourceLocation getSourceTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return sourceTexture;
    }
    
    @Override
    public ResourceLocation getFlowingTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return flowingTexture;
    }
    
    @Override
    public int getColor(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return color;
    }
    
    @Override
    public int getLuminosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return luminosity;
    }
    
    @Override
    public int getDensity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return density;
    }
    
    @Override
    public int getTemperature(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return temperature;
    }
    
    @Override
    public int getViscosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return viscosity;
    }
    
    @Override
    public boolean isLighterThanAir(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return lighterThanAir;
    }
    
    @Override
    public Rarity getRarity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return rarity;
    }
    
    @Override
    @Nullable
    public SoundEvent getFillSound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return fillSound;
    }
    
    @Override
    @Nullable
    public SoundEvent getEmptySound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        return emptySound;
    }
}
