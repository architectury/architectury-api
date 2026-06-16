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

package dev.architectury.registry.level.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public final class EntityDataSerializerRegistry {
    private EntityDataSerializerRegistry() {
    }

    /**
     * Registers a custom {@link EntityDataSerializer} under the given identifier.
     * <p>
     * On Fabric this delegates to {@code FabricEntityDataRegistry.register}.
     * On NeoForge the serializer is registered into {@code NeoForgeRegistries.ENTITY_DATA_SERIALIZERS}
     * during the appropriate registration phase.
     *
     * @param id         the registry identifier; its namespace is used as the owning mod id on NeoForge
     * @param serializer the serializer to register
     */
    @ExpectPlatform
    public static <T> void register(Identifier id, EntityDataSerializer<T> serializer) {
        throw new AssertionError();
    }
}
