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

package me.shedaniel.architectury.registry.forge;

import me.shedaniel.architectury.forge.ArchitecturyForge;
import me.shedaniel.architectury.mixin.forge.ParticleEngineAccessor;
import me.shedaniel.architectury.registry.ParticleFactories;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ArchitecturyForge.MOD_ID)
public class ParticleFactoriesImpl {
    private static final class ExtendedSpriteSetImpl implements ParticleFactories.ExtendedSpriteSet {
        private final ParticleEngine engine;
        private final SpriteSet delegate;
    
        private ExtendedSpriteSetImpl(ParticleEngine engine, SpriteSet delegate) {
            this.engine = engine;
            this.delegate = delegate;
        }

        @Override
        public TextureAtlas getAtlas() {
            return ((ParticleEngineAccessor) engine).getTextureAtlas();
        }

        @Override
        public List<TextureAtlasSprite> getSprites() {
            return ((ParticleEngineAccessor.MutableSpriteSetAccessor) delegate).getSprites();
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

    private static ArrayList<Runnable> deferred = new ArrayList<>();

    private static <T extends ParticleOptions> void _register(ParticleType<T> type, ParticleProvider<T> provider) {
        Minecraft.getInstance().particleEngine.register(type, provider);
    }

    private static <T extends ParticleOptions> void _register(ParticleType<T> type, ParticleFactories.PendingParticleProvider<T> constructor) {
        Minecraft.getInstance().particleEngine.register(type, arg ->
                constructor.create(new ExtendedSpriteSetImpl(Minecraft.getInstance().particleEngine, arg)));
    }

    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        if (deferred == null)
            _register(type, provider);
        else
            deferred.add(() -> _register(type, provider));
    }

    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleFactories.PendingParticleProvider<T> constructor) {
        if (deferred == null)
            _register(type, constructor);
        else
            deferred.add(() -> _register(type, constructor));
    }

    @SubscribeEvent
    public static void onParticleFactoryRegister(ParticleFactoryRegisterEvent unused) {
        if (deferred != null) {
            // run all deferred registrations
            for (Runnable r : deferred)
                r.run();
            // yeet deferred list - register immediately from now on
            deferred = null;
        }
    }
}
