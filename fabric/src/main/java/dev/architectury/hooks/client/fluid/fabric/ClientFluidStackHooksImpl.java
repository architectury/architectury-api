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

package dev.architectury.hooks.client.fluid.fabric;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.core.fluid.fabric.ArchitecturyFlowingFluidImpl;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class ClientFluidStackHooksImpl {
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        return getSprite(state.getType(), true, state, level, pos);
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        return getSprite(stack.getFluid(), true, stack);
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        return getSprite(fluid, true, null);
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        return getSprite(state.getType(), false, state, level, pos);
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        return getSprite(stack.getFluid(), false, stack);
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        return getSprite(fluid, false, null);
    }
    
    public static int getColor(@Nullable BlockAndLightGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        ArchitecturyFluidAttributes attributes = ArchitecturyFlowingFluidImpl.getAttributes(state.getType());
        return attributes == null ? -1 : attributes.getColor(state, level, pos);
    }
    
    public static int getColor(FluidStack stack) {
        return FluidVariantRendering.getColor(FluidStackHooksFabric.toFabric(stack));
    }
    
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
    return FluidVariantRendering.getColor(FluidVariant.of(fluid));
    }
    
    @Nullable
    private static TextureAtlasSprite getSprite(Fluid fluid, boolean still, @Nullable FluidStack stack) {
        ArchitecturyFluidAttributes attributes = ArchitecturyFlowingFluidImpl.getAttributes(fluid);
        if (attributes == null || stack == null) return null;
        return getBlocksAtlas().getSprite(still ? attributes.getSourceTexture(stack.getFluid().defaultFluidState()) : attributes.getFlowingTexture(stack.getFluid().defaultFluidState()));
    }
    
    @Nullable
    private static TextureAtlasSprite getSprite(Fluid fluid, boolean still, FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (fluid == Fluids.EMPTY) return null;
        ArchitecturyFluidAttributes attributes = ArchitecturyFlowingFluidImpl.getAttributes(fluid);
        if (attributes == null) return null;
        return getBlocksAtlas().getSprite(still ? attributes.getSourceTexture(state, level, pos) : attributes.getFlowingTexture(state, level, pos));
    }
    
    private static TextureAtlas getBlocksAtlas() {
        return (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS);
    }
}
