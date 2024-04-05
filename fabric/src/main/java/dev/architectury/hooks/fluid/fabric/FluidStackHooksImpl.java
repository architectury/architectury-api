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

package dev.architectury.hooks.fluid.fabric;

import com.mojang.logging.LogUtils;
import dev.architectury.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class FluidStackHooksImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static Component getName(FluidStack stack) {
        return FluidVariantAttributes.getName(FluidStackHooksFabric.toFabric(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        var id = BuiltInRegistries.FLUID.getKey(stack.getFluid());
        return "block." + id.getNamespace() + "." + id.getPath();
    }
    
    public static FluidStack read(RegistryFriendlyByteBuf buf) {
        return FluidStack.STREAM_CODEC.decode(buf);
    }
    
    public static void write(FluidStack stack, RegistryFriendlyByteBuf buf) {
        FluidStack.STREAM_CODEC.encode(buf, stack);
    }
    
    public static Optional<FluidStack> read(HolderLookup.Provider provider, Tag tag) {
        return FluidStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), tag)
                .resultOrPartial(string -> LOGGER.error("Tried to load invalid fluid stack: '{}'", string));
    }
    
    public static FluidStack readOptional(HolderLookup.Provider provider, CompoundTag tag) {
        return tag.isEmpty() ? FluidStack.empty() : read(provider, tag).orElse(FluidStack.empty());
    }
    
    public static Tag write(HolderLookup.Provider provider, FluidStack stack, Tag tag) {
        return FluidStack.CODEC.encode(stack, provider.createSerializationContext(NbtOps.INSTANCE), tag).getOrThrow(IllegalStateException::new);
    }
    
    public static long bucketAmount() {
        return 81000;
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        var handler = FluidRenderHandlerRegistry.INSTANCE.get(state.getType());
        if (handler == null) return null;
        var sprites = handler.getFluidSprites(level, pos, state);
        if (sprites == null) return null;
        return sprites[0];
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        var sprites = FluidVariantRendering.getSprites(FluidStackHooksFabric.toFabric(stack));
        return sprites == null ? null : sprites[0];
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        var sprites = FluidVariantRendering.getSprites(FluidVariant.of(fluid));
        return sprites == null ? null : sprites[0];
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        var handler = FluidRenderHandlerRegistry.INSTANCE.get(state.getType());
        if (handler == null) return null;
        var sprites = handler.getFluidSprites(level, pos, state);
        if (sprites == null) return null;
        return sprites[1];
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        var sprites = FluidVariantRendering.getSprites(FluidStackHooksFabric.toFabric(stack));
        return sprites == null ? null : sprites[1];
    }
    
    @Environment(EnvType.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        var sprites = FluidVariantRendering.getSprites(FluidVariant.of(fluid));
        return sprites == null ? null : sprites[1];
    }
    
    @Environment(EnvType.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        var handler = FluidRenderHandlerRegistry.INSTANCE.get(state.getType());
        if (handler == null) return -1;
        return handler.getFluidColor(level, pos, state);
    }
    
    @Environment(EnvType.CLIENT)
    public static int getColor(FluidStack stack) {
         return FluidVariantRendering.getColor(FluidStackHooksFabric.toFabric(stack));
    }
    
    @Environment(EnvType.CLIENT)
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        var handler = FluidRenderHandlerRegistry.INSTANCE.get(fluid);
        if (handler == null) return -1;
        return handler.getFluidColor(null, null, fluid.defaultFluidState());
    }
    
    public static int getLuminosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getLuminance(FluidStackHooksFabric.toFabric(fluid));
    }
    
    public static int getLuminosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getLuminance(FluidVariant.of(fluid));
    }
    
    public static int getTemperature(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getTemperature(FluidStackHooksFabric.toFabric(fluid));
    }
    
    public static int getTemperature(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getTemperature(FluidVariant.of(fluid));
    }
    
    public static int getViscosity(FluidStack fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getViscosity(FluidStackHooksFabric.toFabric(fluid), level);
    }
    
    public static int getViscosity(Fluid fluid, @Nullable Level level, @Nullable BlockPos pos) {
        return FluidVariantAttributes.getViscosity(FluidVariant.of(fluid), level);
    }
}
