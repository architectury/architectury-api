/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022, 2023 architectury
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

package dev.architectury.hooks.data.fabric;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySynchronization;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DataPackRegistryHooksImpl {
    
    private static final List<RegistryDataLoader.RegistryData<?>> DATA_REGISTRIES = new ArrayList<>();
    private static final Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> NETWORKABLE_DATA_REGISTRIES = new HashMap<>();
    private static final Set<ResourceLocation> REGISTRIES_TO_PREFIX = new HashSet<>();
    
    public static <T> void addRegistryCodec(ResourceKey<? extends Registry<T>> key, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        DATA_REGISTRIES.add(new RegistryDataLoader.RegistryData<>(key, codec));
        REGISTRIES_TO_PREFIX.add(key.location());
        if (networkCodec != null)
            NETWORKABLE_DATA_REGISTRIES.put(key, new RegistrySynchronization.NetworkedRegistryData<>(key, networkCodec));
    }
    
    public static List<RegistryDataLoader.RegistryData<?>> getDataRegistries() {
        return DATA_REGISTRIES;
    }
    
    public static Map<ResourceKey<? extends Registry<?>>, RegistrySynchronization.NetworkedRegistryData<?>> getNetworkableDataRegistries() {
        return NETWORKABLE_DATA_REGISTRIES;
    }
    
    public static boolean shouldPrefix(ResourceLocation l) {
        return !l.getNamespace().equals("minecraft") && REGISTRIES_TO_PREFIX.contains(l);
    }
}
