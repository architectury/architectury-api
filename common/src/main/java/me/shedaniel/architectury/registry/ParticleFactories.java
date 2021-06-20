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

package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class ParticleFactories {
    public interface ExtendedSpriteSet extends SpriteSet {
        TextureAtlas getAtlas();
        List<TextureAtlasSprite> getSprites();
    }

    @ExpectPlatform
    <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        throw new AssertionError();
    }

    @ExpectPlatform
    <T extends ParticleOptions> void register(ParticleType<T> type, PendingParticleProvider<T> constructor) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface PendingParticleProvider<T extends ParticleOptions> {
        ParticleProvider<T> create(ExtendedSpriteSet spriteSet);
    }
}
