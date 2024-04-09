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

import com.mojang.logging.LogUtils;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Optional;

public class FluidStackHooksImpl {
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static Component getName(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescription(FluidStackHooksForge.toForge(stack));
    }
    
    public static String getTranslationKey(FluidStack stack) {
        return stack.getFluid().getFluidType().getDescriptionId(FluidStackHooksForge.toForge(stack));
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
        return 1000;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(state).getStillTexture(state, level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getStillTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(state).getFlowingTexture(state, level, pos);
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(stack.getFluid()).getFlowingTexture(FluidStackHooksForge.toForge(stack));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static TextureAtlasSprite getFlowingTexture(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return null;
        ResourceLocation texture = IClientFluidTypeExtensions.of(fluid).getFlowingTexture();
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(@Nullable BlockAndTintGetter level, @Nullable BlockPos pos, FluidState state) {
        if (state.getType() == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(state).getTintColor(state, level, pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(FluidStack stack) {
        if (stack.getFluid() == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(FluidStackHooksForge.toForge(stack));
    }
    
    @OnlyIn(Dist.CLIENT)
    public static int getColor(Fluid fluid) {
        if (fluid == Fluids.EMPTY) return -1;
        return IClientFluidTypeExtensions.of(fluid).getTintColor();
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
