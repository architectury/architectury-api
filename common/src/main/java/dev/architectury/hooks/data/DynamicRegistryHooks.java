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

package dev.architectury.hooks.data;

import com.mojang.serialization.Codec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class DynamicRegistryHooks {
    
    private DynamicRegistryHooks() {
    }
    
    /**
     * Registers a world-specific dynamic registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param id           The name of the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param networkCodec An optional codec to sync registry contents to clients
     * @param <T> The registry object type
     * @return The key to find registry contents on a server's {@link RegistryAccess}
     */
    @ExpectPlatform
    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        throw new AssertionError();
    }
    
    /**
     * Registers a world-specific dynamic registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param id           The name of the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param <T> The registry object type
     * @return The key to find registry contents on a server's {@link RegistryAccess}
     */
    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec) {
        return create(id, codec, null);
    }
}
