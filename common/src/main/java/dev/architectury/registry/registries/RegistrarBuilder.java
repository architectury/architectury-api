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

package dev.architectury.registry.registries;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

/**
 * A builder to create a new {@link Registrar}.
 *
 * @param <T> The type of registry object
 * @author ebo2022
 * @since
 */
public interface RegistrarBuilder<T> {
    
    /**
     * @return A new registrar with the given properties
     */
    Registrar<T> build();
    
    /**
     * Specifies that the registrar should save its contents to the disk (persist).
     */
    RegistrarBuilder<T> saveToDisc();
    
    /**
     * Specifies that the registrar should sync its contents between servers and clients.
     * <p>If the registrar can {@link #dataPackRegistry(Codec, Codec) load contents via datapack} this will be enabled by default.
     */
    RegistrarBuilder<T> syncToClients();
    
    /**
     * Adds code to run when a new registry entry is added.
     *
     * @param callback The callback to run
     */
    RegistrarBuilder<T> onAdd(OnAddCallback<T> callback);
    
    /**
     * Allows the built registrar to load contents from JSON files located in a directory corresponding to the registry name.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param codec        The codec to (de)serialize registrar entries from JSON
     * @param networkCodec An optional codec to sync contents to the server
     */
    RegistrarBuilder<T> dataPackRegistry(Codec<T> codec, @Nullable Codec<T> networkCodec);
    
    /**
     * Allows the built registrar to load contents from JSON files located in a directory corresponding to the registry name.
     * <p>Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     * <p>The built registrar will not be required to be present on the client when joining a server.
     *
     * @param codec The codec to (de)serialize registrar entries from JSON
     */
    default RegistrarBuilder<T> dataPackRegistry(Codec<T> codec) {
        return this.dataPackRegistry(codec, null);
    }
    
    @FunctionalInterface
    interface OnAddCallback<T> {
        void onAdd(int id, ResourceLocation name, T object);
    }
}
