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

package me.shedaniel.architectury.mixin.forge;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {
    @Accessor
    TextureAtlas getTextureAtlas();
    
    // this is actually a Map<ResourceLocation, ParticleEngine.MutableSpriteSet>, but luckily type erasure saves the day
    @Accessor
    Map<ResourceLocation, SpriteSet> getProviders();
    
    @Mixin(targets = "net/minecraft/client/particle/ParticleEngine$MutableSpriteSet")
    interface MutableSpriteSetAccessor {
        @Accessor
        List<TextureAtlasSprite> getSprites();
    }
}
