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
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

class ArchitecturyFluidAttributesForge extends FluidAttributes {
    private final ArchitecturyFluidAttributes attributes;
    private final String defaultTranslationKey;
    
    public ArchitecturyFluidAttributesForge(Builder builder, Fluid fluid, ArchitecturyFluidAttributes attributes) {
        super(addArchIntoBuilder(builder, attributes), fluid);
        this.attributes = attributes;
        this.defaultTranslationKey = Util.makeDescriptionId("fluid", fluid.getRegistryName());
    }
    
    private static Builder addArchIntoBuilder(Builder builder, ArchitecturyFluidAttributes attributes) {
        builder.luminosity(attributes.getLuminosity())
                .density(attributes.getDensity())
                .temperature(attributes.getTemperature())
                .viscosity(attributes.getViscosity());
        if (attributes.isLighterThanAir()) builder.gaseous();
        return builder;
    }
    
    @Override
    public ResourceLocation getStillTexture() {
        return attributes.getStillTexture();
    }
    
    @Override
    public ResourceLocation getStillTexture(FluidStack stack) {
        return attributes.getStillTexture(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public ResourceLocation getStillTexture(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getStillTexture(null, level, pos);
    }
    
    @Override
    public ResourceLocation getFlowingTexture() {
        return attributes.getFlowingTexture();
    }
    
    @Override
    public ResourceLocation getFlowingTexture(FluidStack stack) {
        return attributes.getFlowingTexture(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public ResourceLocation getFlowingTexture(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getFlowingTexture(null, level, pos);
    }
    
    @Override
    public int getColor() {
        return attributes.getColor();
    }
    
    @Override
    public int getColor(FluidStack stack) {
        return attributes.getColor(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getColor(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getColor(null, level, pos);
    }
    
    @Override
    public int getLuminosity(FluidStack stack) {
        return attributes.getLuminosity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getLuminosity(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getLuminosity(null, level, pos);
    }
    
    @Override
    public int getDensity(FluidStack stack) {
        return attributes.getDensity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getDensity(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getDensity(null, level, pos);
    }
    
    @Override
    public int getTemperature(FluidStack stack) {
        return attributes.getTemperature(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getTemperature(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getTemperature(null, level, pos);
    }
    
    @Override
    public int getViscosity(FluidStack stack) {
        return attributes.getViscosity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getViscosity(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getViscosity(null, level, pos);
    }
    
    @Override
    public boolean isGaseous(FluidStack stack) {
        return attributes.isLighterThanAir(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public boolean isGaseous(BlockAndTintGetter level, BlockPos pos) {
        return attributes.isLighterThanAir(null, level, pos);
    }
    
    @Override
    public Rarity getRarity() {
        return attributes.getRarity();
    }
    
    @Override
    public Rarity getRarity(FluidStack stack) {
        return attributes.getRarity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public Rarity getRarity(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getRarity(null, level, pos);
    }
    
    @Override
    public Component getDisplayName(FluidStack stack) {
        return attributes.getName(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public String getTranslationKey() {
        return MoreObjects.firstNonNull(attributes.getTranslationKey(), defaultTranslationKey);
    }
    
    @Override
    public String getTranslationKey(FluidStack stack) {
        return MoreObjects.firstNonNull(attributes.getTranslationKey(stack == null ? null : FluidStackHooksForge.fromForge(stack)), defaultTranslationKey);
    }
    
    @Override
    public SoundEvent getFillSound() {
        return attributes.getFillSound();
    }
    
    @Override
    public SoundEvent getFillSound(FluidStack stack) {
        return attributes.getFillSound(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public SoundEvent getFillSound(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getFillSound(null, level, pos);
    }
    
    @Override
    public SoundEvent getEmptySound() {
        return attributes.getEmptySound();
    }
    
    @Override
    public SoundEvent getEmptySound(FluidStack stack) {
        return attributes.getEmptySound(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public SoundEvent getEmptySound(BlockAndTintGetter level, BlockPos pos) {
        return attributes.getEmptySound(null, level, pos);
    }
}
