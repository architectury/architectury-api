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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class SimpleArchitecturyFluidAttributes implements ArchitecturyFluidAttributes {
    private final Supplier<? extends Fluid> flowingFluid;
    private final Supplier<? extends Fluid> sourceFluid;
    private boolean canConvertToSource = false;
    private int slopeFindDistance = 4;
    private int dropOff = 1;
    private Supplier<? extends Optional<Item>> bucketItem = Optional::empty;
    private int tickDelay = 5;
    private float explosionResistance = 100.0F;
    private Supplier<? extends Optional<? extends LiquidBlock>> block = Optional::empty;
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
    
    public SimpleArchitecturyFluidAttributes(Supplier<Fluid> flowingFluid, Supplier<Fluid> sourceFluid) {
        this.flowingFluid = flowingFluid;
        this.sourceFluid = sourceFluid;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#canConvertToSource()
     */
    public SimpleArchitecturyFluidAttributes convertToSource(boolean canConvertToSource) {
        this.canConvertToSource = canConvertToSource;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getSlopeFindDistance(LevelReader)
     */
    public SimpleArchitecturyFluidAttributes slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getDropOff(LevelReader)
     */
    public SimpleArchitecturyFluidAttributes dropOff(int dropOff) {
        this.dropOff = dropOff;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getBucketItem()
     */
    public SimpleArchitecturyFluidAttributes bucketItem(RegistrySupplier<Item> bucketItem) {
        return bucketItem(bucketItem::toOptional);
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getBucketItem()
     */
    public SimpleArchitecturyFluidAttributes bucketItem(Supplier<? extends Optional<Item>> bucketItem) {
        this.bucketItem = Objects.requireNonNull(bucketItem);
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getTickDelay(LevelReader)
     */
    public SimpleArchitecturyFluidAttributes tickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getExplosionResistance()
     */
    public SimpleArchitecturyFluidAttributes explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getBlock()
     */
    public SimpleArchitecturyFluidAttributes block(RegistrySupplier<? extends LiquidBlock> block) {
        return block(block::toOptional);
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getBlock()
     */
    public SimpleArchitecturyFluidAttributes block(Supplier<? extends Optional<? extends LiquidBlock>> block) {
        this.block = Objects.requireNonNull(block);
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getSourceTexture(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes sourceTexture(ResourceLocation sourceTexture) {
        this.sourceTexture = sourceTexture;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getFlowingTexture(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes flowingTexture(ResourceLocation flowingTexture) {
        this.flowingTexture = flowingTexture;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getColor(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes color(int color) {
        this.color = color;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getLuminosity(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes luminosity(int luminosity) {
        this.luminosity = luminosity;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getDensity(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes density(int density) {
        this.density = density;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getTemperature(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes temperature(int temperature) {
        this.temperature = temperature;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getViscosity(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes viscosity(int viscosity) {
        this.viscosity = viscosity;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#isLighterThanAir(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes lighterThanAir(boolean lighterThanAir) {
        this.lighterThanAir = lighterThanAir;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getRarity(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes rarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getFillSound(FluidStack, BlockAndTintGetter, BlockPos)
     */
    public SimpleArchitecturyFluidAttributes fillSound(SoundEvent fillSound) {
        this.fillSound = fillSound;
        return this;
    }
    
    /**
     * @see ArchitecturyFluidAttributes#getEmptySound(FluidStack, BlockAndTintGetter, BlockPos)
     */
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
        return bucketItem.get().orElse(null);
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
        return block.get().orElse(null);
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
