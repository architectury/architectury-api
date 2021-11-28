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

package dev.architectury.core;

import com.google.common.reflect.TypeToken;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * An entry in registries, will implement methods from {@code IForgeRegistryEntry}.
 */
public class RegistryEntry<T> {
    private final TypeToken<T> token = new TypeToken<T>(getClass()) {
    };
    private ResourceLocation registryName = null;
    
    @ApiStatus.Internal
    @PlatformOnly(PlatformOnly.FORGE)
    public T setRegistryName(ResourceLocation name) {
        if (registryName != null) {
            throw new IllegalStateException("Tried to override registry name from previous " + registryName + " to " + name);
        }
        
        registryName = name;
        return (T) this;
    }
    
    @Nullable
    @ApiStatus.Internal
    @PlatformOnly(PlatformOnly.FORGE)
    public ResourceLocation getRegistryName() {
        return registryName;
    }
    
    @ApiStatus.Internal
    @PlatformOnly(PlatformOnly.FORGE)
    public Class<T> getRegistryType() {
        return (Class<T>) token.getRawType();
    }
}
