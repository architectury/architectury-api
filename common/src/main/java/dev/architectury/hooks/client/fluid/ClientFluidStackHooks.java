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

package dev.architectury.hooks.client.fluid;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class ClientFluidStackHooks {
    private ClientFluidStackHooks() {
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static int getColor(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static int getColor(Fluid fluid) {
        throw new AssertionError();
    }
}
