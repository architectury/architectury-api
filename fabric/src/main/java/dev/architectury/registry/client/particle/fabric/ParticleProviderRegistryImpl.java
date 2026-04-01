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

package dev.architectury.registry.client.particle.fabric;

import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteSet;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.RandomSource;

import java.util.List;

public class ParticleProviderRegistryImpl {
    public record ExtendedSpriteSetImpl(
            FabricSpriteSet delegate
    ) implements dev.architectury.registry.client.particle.ParticleProviderRegistry.ExtendedSpriteSet {
        @Override
        public TextureAtlas getAtlas() {
            return delegate.getAtlas();
        }
        
        @Override
        public List<TextureAtlasSprite> getSprites() {
            return delegate.getSprites();
        }
        
        @Override
        public TextureAtlasSprite get(int i, int j) {
            return delegate.get(i, j);
        }
        
        @Override
        public TextureAtlasSprite get(RandomSource random) {
            return delegate.get(random);
        }
        
        @Override
        public TextureAtlasSprite first() {
            return delegate.first();
        }
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        ParticleProviderRegistry.getInstance().register(type, provider);
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, dev.architectury.registry.client.particle.ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        ParticleProviderRegistry.getInstance().register(type, sprites ->
                provider.create(new ExtendedSpriteSetImpl(sprites)));
    }
}
