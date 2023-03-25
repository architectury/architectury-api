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

package dev.architectury.hooks.data.forge;

import com.mojang.serialization.Codec;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataPackRegistryEvent;
import org.jetbrains.annotations.Nullable;

public class DynamicRegistryHooksImpl {
    
    @SuppressWarnings("unchecked")
    public static <T> ResourceKey<Registry<T>> create(ResourceLocation id, Codec<T> codec, @Nullable Codec<T> networkCodec) {
        ResourceKey<Registry<T>> key = ResourceKey.createRegistryKey(id);
        EventBuses.onRegistered(id.getNamespace(), bus -> bus.<DataPackRegistryEvent.NewRegistry>addListener(e ->
                e.dataPackRegistry(key, codec, networkCodec)
        ));
        return key;
    }
}
