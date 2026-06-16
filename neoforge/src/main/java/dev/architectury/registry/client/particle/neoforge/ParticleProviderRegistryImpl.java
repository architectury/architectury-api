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

package dev.architectury.registry.client.particle.neoforge;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.AtlasIds;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ParticleProviderRegistryImpl {
    public static final Logger LOGGER = LogUtils.getLogger();
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.addListener(ParticleProviderRegistryImpl::onParticleFactoryRegister);
        });
    }
    
    private record ExtendedSpriteSetImpl(ParticleResources.MutableSpriteSet set) implements ParticleProviderRegistry.ExtendedSpriteSet {
        @Override
        public TextureAtlas getAtlas() {
            return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.PARTICLES);
        }
        
        @Override
        public List<TextureAtlasSprite> getSprites() {
            return set.sprites;
        }
        
        @Override
        public TextureAtlasSprite get(int i, int j) {
            return set.get(i, j);
        }
        
        @Override
        public TextureAtlasSprite get(RandomSource random) {
            return set.get(random);
        }
        
        @Override
        public TextureAtlasSprite first() {
            return set.first();
        }
    }
    
    private static List<Consumer<ParticleProviderRegistrar>> deferred = new ArrayList<>();
    
    private static <T extends ParticleOptions> void doRegister(ParticleProviderRegistrar registrar, ParticleType<T> type, ParticleProvider<T> provider) {
        registrar.register(type, provider);
    }
    
    private static <T extends ParticleOptions> void doRegister(ParticleProviderRegistrar registrar, ParticleType<T> type, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        registrar.register(type, sprites ->
                provider.create(new ExtendedSpriteSetImpl((ParticleResources.MutableSpriteSet) sprites)));
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        if (deferred == null) {
            LOGGER.warn("Something is attempting to register particle providers at a later point than intended! This might cause issues!", new Throwable());
            doRegister(ParticleProviderRegistrar.ofFallback(), type, provider);
        } else {
            deferred.add(registrar -> doRegister(registrar, type, provider));
        }
    }
    
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        if (deferred == null) {
            LOGGER.warn("Something is attempting to register particle providers at a later point than intended! This might cause issues!", new Throwable());
            doRegister(ParticleProviderRegistrar.ofFallback(), type, provider);
        } else {
            deferred.add(registrar -> doRegister(registrar, type, provider));
        }
    }
    
    public static void onParticleFactoryRegister(RegisterParticleProvidersEvent event) {
        if (deferred != null) {
            ParticleProviderRegistrar registrar = ParticleProviderRegistrar.ofForge(event);
            // run all deferred registrations
            for (Consumer<ParticleProviderRegistrar> consumer : deferred) {
                consumer.accept(registrar);
            }
            // yeet deferred list - register immediately from now on
            deferred = null;
        }
    }
    
    private interface ParticleProviderRegistrar {
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider);
        
        <T extends ParticleOptions> void register(ParticleType<T> type, ParticleResources.SpriteParticleRegistration<T> registration);
        
        static ParticleProviderRegistrar ofForge(RegisterParticleProvidersEvent event) {
            return new ParticleProviderRegistrar() {
                @Override
                public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
                    event.registerSpecial(type, provider);
                }
                
                @Override
                public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleResources.SpriteParticleRegistration<T> registration) {
                    event.registerSpriteSet(type, registration);
                }
            };
        }
        
        static ParticleProviderRegistrar ofFallback() {
            return new ParticleProviderRegistrar() {
                @Override
                public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
                    Minecraft.getInstance().particleEngine.resourceManager.register(type, provider);
                }
                
                @Override
                public <T extends ParticleOptions> void register(ParticleType<T> type, ParticleResources.SpriteParticleRegistration<T> registration) {
                    Minecraft.getInstance().particleEngine.resourceManager.register(type, registration);
                }
            };
        }
    }
}
