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

package dev.architectury.hooks.fluid.forge;

import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.RenderProperties;
import org.jetbrains.annotations.Nullable;

public class FluidStackHooksImpl {
    public static Component getName(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescription(FluidStackHooksForge.toForge(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescriptionId(FluidStackHooksForge.toForge(stack));
    }
    
    public static FluidStack read(FriendlyByteBuf buf) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.readFromPacket(buf));
    }
    
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        FluidStackHooksForge.toForge(stack).writeToPacket(buf);
    }
    
    public static FluidStack read(CompoundTag tag) {
        return FluidStackHooksForge.fromForge(net.minecraftforge.fluids.FluidStack.loadFluidStackFromNBT(tag));
    }
    
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        return FluidStackHooksForge.toForge(stack).writeToNBT(tag);
    }
    
    public static long bucketAmount() {
        return 1000;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(state).getStillTexture(state, level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(stack.getFluid()).getStillTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(fluid).getStillTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(state).getFlowingTexture(state, level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(stack.getFluid()).getFlowingTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = RenderProperties.get(fluid).getFlowingTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        return RenderProperties.get(state).getColorTint(state, level, pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return -1;
        return RenderProperties.get(stack.getFluid()).getColorTint(FluidStackHooksForge.toForge(stack));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        return RenderProperties.get(fluid).getColorTint();
    }
    
    public static int getLuminosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getLightLevel(FluidStackHooksForge.toForge(fluid));
    }
    
    @Deprecated(forRemoval = true)
    public static int getLuminosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            var state = level.getFluidState(pos);
            return fluid.getFluidType().getLightLevel(state, level, pos);
        }
        
        return fluid.getFluidType().getLightLevel();
    }
    
    public static int getTemperature(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getTemperature(FluidStackHooksForge.toForge(fluid));
    }
    
    public static int getTemperature(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            var state = level.getFluidState(pos);
            return fluid.getFluidType().getTemperature(state, level, pos);
        }
        
        return fluid.getFluidType().getTemperature();
    }
    
    public static int getViscosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return fluid.getFluid().getFluidType().getViscosity(FluidStackHooksForge.toForge(fluid));
    }
    
    public static int getViscosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        if (level != null && pos != null) {
            var state = level.getFluidState(pos);
            return fluid.getFluidType().getViscosity(state, level, pos);
        }
        
        return fluid.getFluidType().getViscosity();
    }
}
