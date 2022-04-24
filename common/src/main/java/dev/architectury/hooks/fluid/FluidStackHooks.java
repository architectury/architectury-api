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

package dev.architectury.hooks.fluid;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class FluidStackHooks {
    private FluidStackHooks() {
    }
    
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
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(FluidStack stack) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Environment(EnvType.CLIENT)
    public static int getColor(Fluid fluid) {
        throw new AssertionError();
    }
    
    /**
     * Returns the luminosity of the fluid.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the luminosity of the fluid, this ranges from 0 to 15
     */
    @ExpectPlatform
    public static int getLuminosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
    
    /**
     * Returns the luminosity of the fluid.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the luminosity of the fluid, this ranges from 0 to 15
     */
    @ExpectPlatform
    public static int getLuminosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
    
    /**
     * Returns the temperature of the fluid.
     * The temperature is in kelvin, for example, 300 kelvin is equal to room temperature.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the temperature of the fluid
     */
    @ExpectPlatform
    public static int getTemperature(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
    
    /**
     * Returns the temperature of the fluid.
     * The temperature is in kelvin, for example, 300 kelvin is equal to room temperature.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the temperature of the fluid
     */
    @ExpectPlatform
    public static int getTemperature(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
    
    /**
     * Returns the viscosity of the fluid. A lower viscosity means that the fluid will flow faster.
     * The default value is 1000 for water.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the viscosity of the fluid
     */
    @ExpectPlatform
    public static int getViscosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
    
    /**
     * Returns the viscosity of the fluid. A lower viscosity means that the fluid will flow faster.
     * The default value is 1000 for water.
     *
     * @param fluid the fluid
     * @param level the level, can be {@code null}
     * @param pos   the block position, can be {@code null}
     * @return the viscosity of the fluid
     */
    @ExpectPlatform
    public static int getViscosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        throw new AssertionError();
    }
}
