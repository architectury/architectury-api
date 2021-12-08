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

package me.shedaniel.architectury.test.particle;

import me.shedaniel.architectury.event.events.client.ClientLifecycleEvent;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.ParticleProviderRegistry;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.test.TestMod;
import me.shedaniel.architectury.utils.Env;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

public class TestParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(TestMod.MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);
    
    public static final RegistrySupplier<SimpleParticleType> TEST_PARTICLE = PARTICLE_TYPES.register("test_particle", () ->
            new SimpleParticleType(false) { });
    
    public static void initialize() {
        PARTICLE_TYPES.register();
        if (Platform.getEnvironment() == Env.CLIENT) {
            ClientLifecycleEvent.CLIENT_SETUP.register(instance -> {
                ParticleProviderRegistry.register(TEST_PARTICLE.get(), HeartParticle.Provider::new);
            });
        }
    }
}
