/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidStackHooksImpl {
    public static Component getName(FluidStack stack) {
        return stack.getFluid().getAttributes().getDisplayName(FluidStackHooksForge.toForge(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getAttributes().getTranslationKey(FluidStackHooksForge.toForge(stack));
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
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = state.getType().getAttributes().getStillTexture(level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@NotNull FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = stack.getFluid().getAttributes().getStillTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@NotNull Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = fluid.getAttributes().getStillTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = state.getType().getAttributes().getFlowingTexture(level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@NotNull FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = stack.getFluid().getAttributes().getFlowingTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@NotNull Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = fluid.getAttributes().getFlowingTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, @NotNull FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        return state.getType().getAttributes().getColor(level, pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(@NotNull FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return -1;
        return stack.getFluid().getAttributes().getColor(FluidStackHooksForge.toForge(stack));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(@NotNull Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        return fluid.getAttributes().getColor();
    }
}
