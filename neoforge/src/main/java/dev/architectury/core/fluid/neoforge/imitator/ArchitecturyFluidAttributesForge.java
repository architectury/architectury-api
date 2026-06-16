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

package dev.architectury.core.fluid.neoforge.imitator;

import com.google.common.base.MoreObjects;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.hooks.fluid.neoforge.FluidStackHooksForge;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class ArchitecturyFluidAttributesForge extends FluidType {
    private final ArchitecturyFluidAttributes attributes;
    private final String defaultTranslationKey;
    
    public ArchitecturyFluidAttributesForge(Properties builder, Fluid fluid, ArchitecturyFluidAttributes attributes) {
        super(addArchIntoBuilder(builder, attributes));
        this.attributes = attributes;
        this.defaultTranslationKey = Util.makeDescriptionId("fluid", BuiltInRegistries.FLUID.getKey(fluid));
    }
    
    private static Properties addArchIntoBuilder(Properties builder, ArchitecturyFluidAttributes attributes) {
        builder.lightLevel(attributes.getLuminosity())
                .density(attributes.getDensity())
                .temperature(attributes.getTemperature())
                .rarity(attributes.getRarity())
                .canConvertToSource(attributes.canConvertToSource())
                .viscosity(attributes.getViscosity());
        return builder;
    }
    
    @Override
    public ItemStack getBucket(FluidStack stack) {
        Item item = attributes.getBucketItem();
        return item == null ? super.getBucket(stack) : new ItemStack(item);
    }
    
    
    
    @Override
    public int getLightLevel(FluidStack stack) {
        return attributes.getLuminosity(convertSafe(stack));
    }
    
    @Override
    public int getLightLevel(FluidState state, BlockAndLightGetter level, BlockPos pos) {
        if (level instanceof BlockAndTintGetter getter) {
            return attributes.getLuminosity(convertSafe(state), getter, pos);
        }
        return super.getLightLevel(state, level, pos);
    }
    
    @Override
    public int getDensity(FluidStack stack) {
        return attributes.getDensity(convertSafe(stack));
    }
    
    @Override
    public int getDensity(FluidState state, BlockAndLightGetter level, BlockPos pos) {
        if (level instanceof BlockAndTintGetter getter) {
            return attributes.getDensity(convertSafe(state), getter, pos);
        }
        return super.getDensity(state, level, pos);
    }
    
    @Override
    public int getTemperature(FluidStack stack) {
        return attributes.getTemperature(convertSafe(stack));
    }
    
    @Override
    public int getTemperature(FluidState state, BlockAndLightGetter level, BlockPos pos) {
        if (level instanceof BlockAndTintGetter getter) {
            return attributes.getTemperature(convertSafe(state), getter, pos);
        }
        return super.getTemperature(state, level, pos);
    }
    
    @Override
    public int getViscosity(FluidStack stack) {
        return attributes.getViscosity(convertSafe(stack));
    }
    
    @Override
    public int getViscosity(FluidState state, BlockAndLightGetter level, BlockPos pos) {
        if (level instanceof BlockAndTintGetter getter) {
            return attributes.getViscosity(convertSafe(state), getter, pos);
        }
        return super.getViscosity(state, level, pos);
    }
    
    @Override
    public Rarity getRarity() {
        return attributes.getRarity();
    }
    
    @Override
    public Rarity getRarity(FluidStack stack) {
        return attributes.getRarity(convertSafe(stack));
    }
    
    @Override
    public Component getDescription() {
        return attributes.getName();
    }
    
    @Override
    public Component getDescription(FluidStack stack) {
        return attributes.getName(convertSafe(stack));
    }
    
    @Override
    public String getDescriptionId() {
        return MoreObjects.firstNonNull(attributes.getTranslationKey(), defaultTranslationKey);
    }
    
    @Override
    public String getDescriptionId(FluidStack stack) {
        return MoreObjects.firstNonNull(attributes.getTranslationKey(convertSafe(stack)), defaultTranslationKey);
    }
    
    @Override
    @Nullable
    public SoundEvent getSound(SoundAction action) {
        return getSound((FluidStack) null, action);
    }
    
    @Override
    @Nullable
    public SoundEvent getSound(@Nullable FluidStack stack, SoundAction action) {
        var archStack = convertSafe(stack);
        if (SoundActions.BUCKET_FILL.equals(action)) {
            return attributes.getFillSound(archStack);
        } else if (SoundActions.BUCKET_EMPTY.equals(action)) {
            return attributes.getEmptySound(archStack);
        }
        return null;
    }
    
    @Override
    @Nullable
    public SoundEvent getSound(@Nullable LivingEntity entity, BlockGetter getter, BlockPos pos, SoundAction action) {
        if (getter instanceof BlockAndTintGetter level) {
            if (SoundActions.BUCKET_FILL.equals(action)) {
                return attributes.getFillSound(null, level, pos);
            } else if (SoundActions.BUCKET_EMPTY.equals(action)) {
                return attributes.getEmptySound(null, level, pos);
            }
        }
        return getSound((FluidStack) null, action);
    }
    
    @Override
    public boolean canConvertToSource(FluidStack stack) {
        return attributes.canConvertToSource();
    }
    
    @Override
    public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
        return attributes.canConvertToSource();
    }
    
    @Nullable
    public dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidStack stack) {
        return stack == null ? null : FluidStackHooksForge.fromForge(stack);
    }
    
    @Nullable
    public dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidState state) {
        return state == null ? null : dev.architectury.fluid.FluidStack.create(state.getType(), dev.architectury.fluid.FluidStack.bucketAmount());
    }
    
    public ArchitecturyFluidAttributes getAttributes() {
        return attributes;
    }
}