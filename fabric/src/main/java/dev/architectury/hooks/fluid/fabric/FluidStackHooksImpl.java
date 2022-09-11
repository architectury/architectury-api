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

import dev.architectury.fluid.FluidStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FluidStackHooksImpl {
    public static Component getName(FluidStack stack) {
        return FluidVariantAttributes.getName(FluidStackHooksFabric.toFabric(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        var id = Registry.FLUID.getKey(stack.getFluid());
        return "block." + id.getNamespace() + "." + id.getPath();
    }
    
    public static FluidStack read(FriendlyByteBuf buf) {
        var fluid = Objects.requireNonNull(Registry.FLUID.get(buf.readResourceLocation()));
        var amount = buf.readVarLong();
        var tag = buf.readNbt();
        if (fluid == Fluids.EMPTY) return FluidStack.empty();
        return FluidStack.create(fluid, amount, tag);
    }
    
    public static void write(FluidStack stack, FriendlyByteBuf buf) {
        buf.writeResourceLocation(Registry.FLUID.getKey(stack.getFluid()));
        buf.writeVarLong(stack.getAmount());
        buf.writeNbt(stack.getTag());
    }
    
    public static FluidStack read(CompoundTag tag) {
        if (tag == null || !tag.contains("id", Tag.TAG_STRING)) {
            return FluidStack.empty();
        }
        
        var fluid = Registry.FLUID.get(new ResourceLocation(tag.getString("id")));
        if (fluid == null || fluid == Fluids.EMPTY) {
            return FluidStack.empty();
        }
        var amount = tag.getLong("amount");
        var stack = FluidStack.create(fluid, amount);
        
        if (tag.contains("tag", Tag.TAG_COMPOUND)) {
            stack.setTag(tag.getCompound("tag"));
        }
        return stack;
    }
    
    public static CompoundTag write(FluidStack stack, CompoundTag tag) {
        tag.putString("id", Registry.FLUID.getKey(stack.getFluid()).toString());
        tag.putLong("amount", stack.getAmount());
        if (stack.hasTag()) {
            tag.put("tag", stack.getTag());
        }
        return tag;
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
