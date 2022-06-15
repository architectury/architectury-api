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

package dev.architectury.core.fluid.fabric;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRenderHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
class ArchitecturyFluidRenderingFabric implements FluidVariantRenderHandler, FluidRenderHandler {
    private final ArchitecturyFluidAttributes attributes;
    private final TextureAtlasSprite[] sprites = new TextureAtlasSprite[3];
    private final TextureAtlasSprite[] spritesOther = new TextureAtlasSprite[3];
    
    public ArchitecturyFluidRenderingFabric(ArchitecturyFluidAttributes attributes) {
        this.attributes = attributes;
    }
    
    @Override
    @Nullable
    public TextureAtlasSprite[] getSprites(FluidVariant variant) {
        FluidStack stack = FluidStackHooksFabric.fromFabric(variant, FluidStack.bucketAmount());
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
        sprites[0] = atlas.apply(attributes.getSourceTexture(stack));
        sprites[1] = atlas.apply(attributes.getFlowingTexture(stack));
        ResourceLocation overlayTexture = attributes.getOverlayTexture(stack);
        sprites[2] = overlayTexture == null ? null : atlas.apply(overlayTexture);
        return sprites;
    }
    
    @Override
    public int getColor(FluidVariant variant, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        return attributes.getColor(FluidStackHooksFabric.fromFabric(variant, FluidStack.bucketAmount()), view, pos);
    }
    
    @Override
    public TextureAtlasSprite[] getFluidSprites(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
        spritesOther[0] = atlas.apply(attributes.getSourceTexture(state, view, pos));
        spritesOther[1] = atlas.apply(attributes.getFlowingTexture(state, view, pos));
        ResourceLocation overlayTexture = attributes.getOverlayTexture(state, view, pos);
        spritesOther[2] = overlayTexture == null ? null : atlas.apply(overlayTexture);
        return spritesOther;
    }
    
    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return attributes.getColor(state, view, pos);
    }
}
