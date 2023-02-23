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
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class DataPackRegistryHooks {
    
    private DataPackRegistryHooks() {
    }
    
    /**
     * Registers a world-specific data registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param key          The key to identify the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param networkCodec An optional codec to sync registry contents to clients
     * @param <T> The registry object type
     */
    @ExpectPlatform
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        throw new AssertionError();
    }
    
    /**
     * Registers a world-specific data registry that loads its contents from Json using the provided {@link Codec}.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param key          The key to identify the registry
     * @param codec        A codec to (de)serialize registry entries from JSON
     * @param <T> The registry object type
     */
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec) {
        addRegistryCodec(key, codec, null);
    }
}
