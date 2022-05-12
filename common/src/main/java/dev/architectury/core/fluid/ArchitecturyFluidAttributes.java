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

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

/**
 * Attributes of a fluid.
 *
 * @see SimpleArchitecturyFluidAttributes
 */
public interface ArchitecturyFluidAttributes {
    /**
     * Returns the translation key of the name of this fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the translation key
     */
    @Nullable
    String getTranslationKey(@Nullable FluidStack stack);
    
    /**
     * Returns the translation key of the name of this fluid.
     *
     * @return the translation key
     */
    @Nullable
    default String getTranslationKey() {
        return getTranslationKey(null);
    }
    
    /**
     * Returns the name of this fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the name
     */
    default Component getName(@Nullable FluidStack stack) {
        return new TranslatableComponent(getTranslationKey(stack));
    }
    
    /**
     * Returns the name of this fluid.
     *
     * @return the name
     */
    default Component getName() {
        return getName(null);
    }
    
    /**
     * Returns the flowing fluid.
     *
     * @return the flowing fluid
     */
    Fluid getFlowingFluid();
    
    /**
     * Returns the still fluid.
     *
     * @return the still fluid
     */
    Fluid getSourceFluid();
    
    /**
     * Returns whether this fluid can be converted to a source block when a flowing fluid is adjacent to two source blocks.
     * A fluid that can be converted to a source block means that the fluid can be multiplied infinitely.
     *
     * @return whether this fluid can be converted to a source block
     */
    boolean canConvertToSource();
    
    /**
     * Returns the maximum distance this fluid will consider as a flowable hole candidate.
     *
     * @param level the level, can be {@code null}
     * @return the maximum distance
     * @see net.minecraft.world.level.material.WaterFluid#getSlopeFindDistance(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getSlopeFindDistance(LevelReader)
     */
    int getSlopeFindDistance(@Nullable LevelReader level);
    
    /**
     * Returns the maximum distance this fluid will consider as a flowable hole candidate.
     *
     * @return the maximum distance
     * @see net.minecraft.world.level.material.WaterFluid#getSlopeFindDistance(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getSlopeFindDistance(LevelReader)
     */
    default int getSlopeFindDistance() {
        return getSlopeFindDistance(null);
    }
    
    /**
     * Returns the drop in fluid level per block travelled.
     *
     * @param level the level, can be {@code null}
     * @return the drop in fluid level per block travelled
     * @see net.minecraft.world.level.material.WaterFluid#getDropOff(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getDropOff(LevelReader)
     */
    int getDropOff(@Nullable LevelReader level);
    
    /**
     * Returns the drop in fluid level per block travelled.
     *
     * @return the drop in fluid level per block travelled
     * @see net.minecraft.world.level.material.WaterFluid#getDropOff(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getDropOff(LevelReader)
     */
    default int getDropOff() {
        return getDropOff(null);
    }
    
    /**
     * Returns the filled bucket item for this fluid.
     *
     * @return the filled bucket item
     */
    @Nullable
    Item getBucketItem();
    
    /**
     * Returns the tick delay between each flow update.
     *
     * @param level the level, can be {@code null}
     * @return the tick delay
     * @see net.minecraft.world.level.material.WaterFluid#getTickDelay(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getTickDelay(LevelReader)
     */
    int getTickDelay(@Nullable LevelReader level);
    
    /**
     * Returns the tick delay between each flow update.
     *
     * @return the tick delay
     * @see net.minecraft.world.level.material.WaterFluid#getTickDelay(LevelReader)
     * @see net.minecraft.world.level.material.LavaFluid#getTickDelay(LevelReader)
     */
    default int getTickDelay() {
        return getTickDelay(null);
    }
    
    /**
     * Returns the explosion resistance of this fluid.
     *
     * @return the explosion resistance
     * @see net.minecraft.world.level.material.WaterFluid#getExplosionResistance()
     * @see net.minecraft.world.level.material.LavaFluid#getExplosionResistance()
     */
    float getExplosionResistance();
    
    /**
     * Returns the block form of this fluid.
     *
     * @return the block form
     * @see net.minecraft.world.level.block.Blocks#WATER
     * @see net.minecraft.world.level.block.Blocks#LAVA
     */
    @Nullable
    LiquidBlock getBlock();
    
    /**
     * Returns the texture location of this fluid in its source form.
     * <p>
     * The vanilla water location is {@code "block/water_still"}.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the texture location
     */
    ResourceLocation getSourceTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the texture location of this fluid in its source form.
     * <p>
     * The vanilla water location is {@code "block/water_still"}.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the texture location
     */
    default ResourceLocation getSourceTexture(@Nullable FluidStack stack) {
        return getSourceTexture(stack, null, null);
    }
    
    /**
     * Returns the texture location of this fluid in its source form.
     * <p>
     * The vanilla water location is {@code "block/water_still"}.
     *
     * @return the texture location
     */
    default ResourceLocation getSourceTexture() {
        return getSourceTexture(null);
    }
    
    /**
     * Returns the texture location of this fluid in its flowing form.
     * <p>
     * The vanilla water location is {@code "block/water_flow"}.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the texture location
     */
    ResourceLocation getFlowingTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the texture location of this fluid in its flowing form.
     * <p>
     * The vanilla water location is {@code "block/water_flow"}.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the texture location
     */
    default ResourceLocation getFlowingTexture(@Nullable FluidStack stack) {
        return getFlowingTexture(stack, null, null);
    }
    
    /**
     * Returns the texture location of this fluid in its flowing form.
     * <p>
     * The vanilla water location is {@code "block/water_flow"}.
     *
     * @return the texture location
     */
    default ResourceLocation getFlowingTexture() {
        return getFlowingTexture(null);
    }
    
    /**
     * Returns the color of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the color
     */
    int getColor(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the color of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the color
     */
    default int getColor(@Nullable FluidStack stack) {
        return getColor(stack, null, null);
    }
    
    /**
     * Returns the color of the fluid.
     *
     * @return the color
     */
    default int getColor() {
        return getColor(null);
    }
    
    /**
     * Returns the luminosity of the fluid, this is between 0 and 15.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the luminosity
     */
    int getLuminosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the luminosity of the fluid, this is between 0 and 15.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the luminosity
     */
    default int getLuminosity(@Nullable FluidStack stack) {
        return getLuminosity(stack, null, null);
    }
    
    /**
     * Returns the luminosity of the fluid, this is between 0 and 15.
     *
     * @return the luminosity
     */
    default int getLuminosity() {
        return getLuminosity(null);
    }
    
    /**
     * Returns the density of the fluid, this is 1000 for water and 3000 for lava on forge.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the density
     */
    int getDensity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the density of the fluid, this is 1000 for water and 3000 for lava on forge.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the density
     */
    default int getDensity(@Nullable FluidStack stack) {
        return getDensity(stack, null, null);
    }
    
    /**
     * Returns the density of the fluid, this is 1000 for water and 3000 for lava on forge.
     *
     * @return the density
     */
    default int getDensity() {
        return getDensity(null);
    }
    
    /**
     * Returns the temperature of the fluid.
     * The temperature is in kelvin, for example, 300 kelvin is equal to room temperature.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the temperature
     */
    int getTemperature(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the temperature of the fluid.
     * The temperature is in kelvin, for example, 300 kelvin is equal to room temperature.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the temperature
     */
    default int getTemperature(@Nullable FluidStack stack) {
        return getTemperature(stack, null, null);
    }
    
    /**
     * Returns the temperature of the fluid.
     * The temperature is in kelvin, for example, 300 kelvin is equal to room temperature.
     *
     * @return the temperature
     */
    default int getTemperature() {
        return getTemperature(null);
    }
    
    /**
     * Returns the viscosity of the fluid. A lower viscosity means that the fluid will flow faster.
     * The default value is 1000 for water.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the viscosity
     */
    int getViscosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the viscosity of the fluid. A lower viscosity means that the fluid will flow faster.
     * The default value is 1000 for water.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the viscosity
     */
    default int getViscosity(@Nullable FluidStack stack) {
        return getViscosity(stack, null, null);
    }
    
    /**
     * Returns the viscosity of the fluid. A lower viscosity means that the fluid will flow faster.
     * The default value is 1000 for water.
     *
     * @return the viscosity
     */
    default int getViscosity() {
        return getViscosity(null);
    }
    
    /**
     * Returns whether this fluid is lighter than air. This is used to determine whether the fluid should be rendered
     * upside down like gas.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return {@code true} if the fluid is lighter than air
     */
    boolean isLighterThanAir(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns whether this fluid is lighter than air. This is used to determine whether the fluid should be rendered
     * upside down like gas.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return {@code true} if the fluid is lighter than air
     */
    default boolean isLighterThanAir(@Nullable FluidStack stack) {
        return isLighterThanAir(stack, null, null);
    }
    
    /**
     * Returns whether this fluid is lighter than air. This is used to determine whether the fluid should be rendered
     * upside down like gas.
     *
     * @return {@code true} if the fluid is lighter than air
     */
    default boolean isLighterThanAir() {
        return isLighterThanAir(null);
    }
    
    /**
     * Returns the rarity of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the rarity
     */
    Rarity getRarity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the rarity of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the rarity
     */
    default Rarity getRarity(@Nullable FluidStack stack) {
        return getRarity(stack, null, null);
    }
    
    /**
     * Returns the rarity of the fluid.
     *
     * @return the rarity
     */
    default Rarity getRarity() {
        return getRarity(null);
    }
    
    /**
     * Returns the fill sound of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the fill sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL_LAVA
     */
    @Nullable
    SoundEvent getFillSound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the fill sound of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the fill sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL_LAVA
     */
    @Nullable
    default SoundEvent getFillSound(@Nullable FluidStack stack) {
        return getFillSound(stack, null, null);
    }
    
    /**
     * Returns the fill sound of the fluid.
     *
     * @return the fill sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL
     * @see net.minecraft.sounds.SoundEvents#BUCKET_FILL_LAVA
     */
    @Nullable
    default SoundEvent getFillSound() {
        return getFillSound(null);
    }
    
    /**
     * Returns the empty sound of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @param level the level, can be {@code null}
     * @param pos   the position, can be {@code null}
     * @return the empty sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY_LAVA
     */
    @Nullable
    SoundEvent getEmptySound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    /**
     * Returns the empty sound of the fluid.
     *
     * @param stack the fluid stack, can be {@code null}
     * @return the empty sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY_LAVA
     */
    @Nullable
    default SoundEvent getEmptySound(@Nullable FluidStack stack) {
        return getEmptySound(stack, null, null);
    }
    
    /**
     * Returns the empty sound of the fluid.
     *
     * @return the empty sound
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY
     * @see net.minecraft.sounds.SoundEvents#BUCKET_EMPTY_LAVA
     */
    @Nullable
    default SoundEvent getEmptySound() {
        return getEmptySound(null);
    }
}
