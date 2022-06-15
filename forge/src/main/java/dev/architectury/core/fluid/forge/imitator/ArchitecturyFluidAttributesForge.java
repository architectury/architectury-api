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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraftforge.common.SoundActions.BUCKET_EMPTY;
import static net.minecraftforge.common.SoundActions.BUCKET_FILL;

class ArchitecturyFluidAttributesForge extends FluidType {
    private final ArchitecturyFluidAttributes attributes;
    private final String defaultTranslationKey;
    
    public ArchitecturyFluidAttributesForge(Properties builder, Fluid fluid, ArchitecturyFluidAttributes attributes) {
        super(addArchIntoBuilder(builder, attributes));
        this.attributes = attributes;
        this.defaultTranslationKey = Util.makeDescriptionId("fluid", ForgeRegistries.FLUIDS.getKey(fluid));
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
    public void initializeClient(Consumer<IFluidTypeRenderProperties> consumer) {
        consumer.accept(new IFluidTypeRenderProperties() {
            @Override
            public int getColorTint() {
                return attributes.getColor();
            }
            
            @Override
            public ResourceLocation getStillTexture() {
                return attributes.getSourceTexture();
            }
            
            @Override
            public ResourceLocation getFlowingTexture() {
                return attributes.getFlowingTexture();
            }
            
            @Override
            public ResourceLocation getStillTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getSourceTexture(null, getter, pos);
            }
            
            @Override
            public ResourceLocation getFlowingTexture(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getFlowingTexture(null, getter, pos);
            }
            
            @Override
            public int getColorTint(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return attributes.getColor(null, getter, pos);
            }
            
            @Override
            public int getColorTint(FluidStack stack) {
                return attributes.getColor(convertSafe(stack));
            }
            
            @Override
            public ResourceLocation getStillTexture(FluidStack stack) {
                return attributes.getSourceTexture(convertSafe(stack));
            }
            
            @Override
            public ResourceLocation getFlowingTexture(FluidStack stack) {
                return attributes.getFlowingTexture(convertSafe(stack));
            }
        });
    }
    
    @Override
    public int getLightLevel(FluidStack stack) {
        return attributes.getLuminosity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getLightLevel(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return attributes.getLuminosity(null, level, pos);
    }
    
    @Override
    public int getDensity(FluidStack stack) {
        return attributes.getDensity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getDensity(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return attributes.getDensity(null, level, pos);
    }
    
    @Override
    public int getTemperature(FluidStack stack) {
        return attributes.getTemperature(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getTemperature(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return attributes.getTemperature(null, level, pos);
    }
    
    @Override
    public int getViscosity(FluidStack stack) {
        return attributes.getViscosity(stack == null ? null : FluidStackHooksForge.fromForge(stack));
    }
    
    @Override
    public int getViscosity(FluidState state, BlockAndTintGetter level, BlockPos pos) {
        return attributes.getViscosity(null, level, pos);
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
    public @Nullable SoundEvent getSound(SoundAction action) {
        return getSound((FluidStack) null, action);
    }
    
    @Override
    public @Nullable SoundEvent getSound(@Nullable FluidStack stack, SoundAction action) {
        var archStack = convertSafe(stack);
        if (BUCKET_FILL.equals(action)) {
            return attributes.getFillSound(archStack);
        } else if (BUCKET_EMPTY.equals(action)) {
            return attributes.getEmptySound(archStack);
        }
        return null;
    }
    
    @Override
    public @Nullable SoundEvent getSound(@Nullable Player player, BlockGetter getter, BlockPos pos, SoundAction action) {
        if (getter instanceof BlockAndTintGetter level) {
            if (BUCKET_FILL.equals(action)) {
                return attributes.getFillSound(null, level, pos);
            } else if (BUCKET_EMPTY.equals(action)) {
                return attributes.getEmptySound(null, level, pos);
            }
        }
        return null;
    }
    
    public dev.architectury.fluid.FluidStack convertSafe(@Nullable FluidStack stack) {
        return stack == null ? null : FluidStackHooksForge.fromForge(stack);
    }
}
