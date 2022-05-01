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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.Nullable;

public interface ArchitecturyFluidAttributes {
    @Nullable
    default String getTranslationKey(@Nullable FluidStack stack) {
        return null;
    }
    
    @Nullable
    default String getTranslationKey() {
        return getTranslationKey(null);
    }
    
    Component getName(@Nullable FluidStack stack);
    
    ResourceLocation getStillTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default ResourceLocation getStillTexture(@Nullable FluidStack stack) {
        return getStillTexture(stack, null, null);
    }
    
    default ResourceLocation getStillTexture() {
        return getStillTexture(null);
    }
    
    ResourceLocation getFlowingTexture(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default ResourceLocation getFlowingTexture(@Nullable FluidStack stack) {
        return getFlowingTexture(stack, null, null);
    }
    
    default ResourceLocation getFlowingTexture() {
        return getFlowingTexture(null);
    }
    
    int getColor(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default int getColor(@Nullable FluidStack stack) {
        return getColor(stack, null, null);
    }
    
    default int getColor() {
        return getColor(null);
    }
    
    int getLuminosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default int getLuminosity(@Nullable FluidStack stack) {
        return getLuminosity(stack, null, null);
    }
    
    default int getLuminosity() {
        return getLuminosity(null);
    }
    
    int getDensity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default int getDensity(@Nullable FluidStack stack) {
        return getDensity(stack, null, null);
    }
    
    default int getDensity() {
        return getDensity(null);
    }
    
    int getTemperature(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default int getTemperature(@Nullable FluidStack stack) {
        return getTemperature(stack, null, null);
    }
    
    default int getTemperature() {
        return getTemperature(null);
    }
    
    int getViscosity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default int getViscosity(@Nullable FluidStack stack) {
        return getViscosity(stack, null, null);
    }
    
    default int getViscosity() {
        return getViscosity(null);
    }
    
    boolean isLighterThanAir(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default boolean isLighterThanAir(@Nullable FluidStack stack) {
        return isLighterThanAir(stack, null, null);
    }
    
    default boolean isLighterThanAir() {
        return isLighterThanAir(null);
    }
    
    Rarity getRarity(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    default Rarity getRarity(@Nullable FluidStack stack) {
        return getRarity(stack, null, null);
    }
    
    default Rarity getRarity() {
        return getRarity(null);
    }
    
    @Nullable
    SoundEvent getFillSound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    @Nullable
    default SoundEvent getFillSound(@Nullable FluidStack stack) {
        return getFillSound(stack, null, null);
    }
    
    @Nullable
    default SoundEvent getFillSound() {
        return getFillSound(null);
    }
    
    @Nullable
    SoundEvent getEmptySound(@Nullable FluidStack stack, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos);
    
    @Nullable
    default SoundEvent getEmptySound(@Nullable FluidStack stack) {
        return getEmptySound(stack, null, null);
    }
    
    @Nullable
    default SoundEvent getEmptySound() {
        return getEmptySound(null);
    }
}
