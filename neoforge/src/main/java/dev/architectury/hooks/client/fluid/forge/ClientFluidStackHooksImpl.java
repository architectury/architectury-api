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

package dev.architectury.hooks.client.fluid.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

public class ClientFluidStackHooksImpl {
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(state).getStillTexture(state, level, pos);
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(state).getFlowingTexture(state, level, pos);
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(stack.getFluid()).getFlowingTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        Identifier texture = IClientFluidTypeExtensions.of(fluid).getFlowingTexture();
        return Minecraft.getInstance().getAtlasManager().get(new Material(TextureAtlas.LOCATION_BLOCKS, texture));
    }
    
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(state).getTintColor(state, level, pos);
    }
    
    public static int getColor(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(FluidStackHooksForge.toForge(stack));
    }
    
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(fluid).getTintColor();
    }
}
