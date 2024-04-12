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

package dev.architectury.registry.client.particle;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.List;

/**
 * A utility class for registering custom {@link ParticleProvider}s for particle types.
 * <p>
 * This class's methods should be invoked <b>before</b> {@link ClientLifecycleEvent#CLIENT_SETUP},
 * as doing so afterwards will result in the providers not being registered properly on Forge, causing crashes on startup.
 * <p>
 * Generally speaking, you should either listen to the registration of your particle type yourself and use either
 * {@link #register(ParticleType, ParticleProvider)} or {@link #register(ParticleType, DeferredParticleProvider)} to register the provider,
 * or use the helper methods {@link #register(RegistrySupplier, ParticleProvider)} and {@link #register(RegistrySupplier, DeferredParticleProvider)},
 * which will automatically handle the listening for you.
 */
@Environment(EnvType.CLIENT)
public final class ParticleProviderRegistry {
    public interface ExtendedSpriteSet extends SpriteSet {
        TextureAtlas getAtlas();
        
        List<TextureAtlasSprite> getSprites();
    }
    
    public static <T extends ParticleOptions> void register(RegistrySupplier<? extends ParticleType<T>> supplier, ParticleProvider<T> provider) {
        supplier.listen(it -> register(it, provider));
    }
    
    public static <T extends ParticleOptions> void register(RegistrySupplier<? extends ParticleType<T>> supplier, DeferredParticleProvider<T> provider) {
        supplier.listen(it -> register(it, provider));
    }
    
    // @ExpectPlatform
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        // throw new AssertionError();
    }
    
    // @ExpectPlatform
    public static <T extends ParticleOptions> void register(ParticleType<T> type, DeferredParticleProvider<T> provider) {
        // throw new AssertionError();
    }
    
    @FunctionalInterface
    public interface DeferredParticleProvider<T extends ParticleOptions> {
        ParticleProvider<T> create(ExtendedSpriteSet spriteSet);
    }
}
