/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.hooks;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import me.shedaniel.architectury.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidStackHooks {
    private FluidStackHooks() {}
    
    @ExpectPlatform
    public static Component getName(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static String getTranslationKey(FluidStack stack) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static FluidStack read(FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack read.
     */
    @ExpectPlatform
    public static FluidStack read(CompoundTag tag) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific FluidStack write.
     */
    @ExpectPlatform
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        throw new AssertionError();
    }
    
    /**
     * Platform-specific bucket amount.
     * Forge: 1000
     * Fabric: 81000
     */
    @ExpectPlatform
    public static long bucketAmount() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@NotNull FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@NotNull Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@NotNull FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@NotNull Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(@NotNull FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(@NotNull Fluid fluid) {
        throw new AssertionError();
    }
}
