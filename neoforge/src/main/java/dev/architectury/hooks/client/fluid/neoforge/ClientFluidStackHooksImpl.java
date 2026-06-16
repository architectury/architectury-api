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

package dev.architectury.hooks.client.fluid.neoforge;

import dev.architectury.core.fluid.neoforge.imitator.ArchitecturyFluidAttributesForge;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.neoforge.FluidStackHooksForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import org.jetbrains.annotations.Nullable;

public class ClientFluidStackHooksImpl {
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        Identifier texture = sourceTexture(state.getType(), state, level, pos);
        return texture != null ? atlasSprite(texture) : fluidModel(state).stillMaterial().sprite();
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        Identifier texture = sourceTexture(stack.getFluid(), null, null, null);
        return texture != null ? atlasSprite(texture) : fluidModel(stack.getFluid()).stillMaterial().sprite();
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        Identifier texture = sourceTexture(fluid, null, null, null);
        return texture != null ? atlasSprite(texture) : fluidModel(fluid).stillMaterial().sprite();
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        Identifier texture = flowingTexture(state.getType(), state, level, pos);
        return texture != null ? atlasSprite(texture) : fluidModel(state).flowingMaterial().sprite();
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        Identifier texture = flowingTexture(stack.getFluid(), null, null, null);
        return texture != null ? atlasSprite(texture) : fluidModel(stack.getFluid()).flowingMaterial().sprite();
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        Identifier texture = flowingTexture(fluid, null, null, null);
        return texture != null ? atlasSprite(texture) : fluidModel(fluid).flowingMaterial().sprite();
    }
    
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        return color(state.getType(), state, level, pos, null);
    }
    
    public static int getColor(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return -1;
        return color(stack.getFluid(), null, null, null, stack);
    }
    
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        return color(fluid, null, null, null, null);
    }
    
    @Nullable
    private static Identifier sourceTexture(Fluid fluid, @Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (fluid.getFluidType() instanceof ArchitecturyFluidAttributesForge archType) {
            return state != null ? archType.getAttributes().getSourceTexture(state, level, pos) : archType.getAttributes().getSourceTexture();
        }
        return null;
    }
    
    @Nullable
    private static Identifier flowingTexture(Fluid fluid, @Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos) {
        if (fluid.getFluidType() instanceof ArchitecturyFluidAttributesForge archType) {
            return state != null ? archType.getAttributes().getFlowingTexture(state, level, pos) : archType.getAttributes().getFlowingTexture();
        }
        return null;
    }
    
    private static int color(Fluid fluid, @Nullable FluidState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @Nullable FluidStack stack) {
        if (fluid.getFluidType() instanceof ArchitecturyFluidAttributesForge archType) {
            return state != null ? archType.getAttributes().getColor(state, level, pos) : archType.getAttributes().getColor();
        }
        
        FluidState fluidState = state != null ? state : fluid.defaultFluidState();
        @Nullable FluidTintSource tint = fluidModel(fluidState).fluidTintSource();
        if (tint == null) {
            return -1;
        }
        
        if (stack != null) {
            return tint.colorAsStack(FluidStackHooksForge.toForge(stack));
        } else if (level != null && pos != null) {
            return tint.colorInWorld(fluidState, fluidState.createLegacyBlock(), level, pos);
        } else {
            return tint.color(fluidState);
        }
    }
    
    private static FluidModel fluidModel(Fluid fluid) {
        return fluidModel(fluid.defaultFluidState());
    }
    
    private static FluidModel fluidModel(FluidState state) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(state);
    }
    
    @Nullable
    private static TextureAtlasSprite atlasSprite(@Nullable Identifier texture) {
        if (texture == null) {
            return null;
        }
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(texture);
    }
}