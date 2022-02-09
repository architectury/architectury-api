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

package dev.architectury.registry.client.particle.forge;

import dev.architectury.event.events.client.ClientParticleFactoryRegisterEvent;
import dev.architectury.forge.ArchitecturyForge;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ArchitecturyForge.MOD_ID, value = Dist.CLIENT)
public class ParticleProviderRegistryImpl {
    private static final class ExtendedSpriteSetImpl implements ParticleProviderRegistry.ExtendedSpriteSet {
        private final ParticleEngine engine;
        private final SpriteSet delegate;
        
        private ExtendedSpriteSetImpl(ParticleEngine engine, SpriteSet delegate) {
            this.engine = engine;
            this.delegate = delegate;
        }
        
        @Override
        public TextureAtlas getAtlas() {
            return engine.textureAtlas;
        }
        
        @Override
        public List<TextureAtlasSprite> getSprites() {
            return ((ParticleEngine.MutableSpriteSet) delegate).sprites;
        }
        
        @Override
        public TextureAtlasSprite get(int i, int j) {
            return delegate.get(i, j);
        }
        
        @Override
        public TextureAtlasSprite get(Random random) {
            return delegate.get(random);
        }
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        ClientParticleFactoryRegisterEvent.REGISTER.register(() -> Minecraft.getInstance().particleEngine.register(type, provider));
        
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        ClientParticleFactoryRegisterEvent.REGISTER.register(() -> Minecraft.getInstance().particleEngine.register(type, sprites ->
                provider.create(new ExtendedSpriteSetImpl(Minecraft.getInstance().particleEngine, sprites))));
    }
}
