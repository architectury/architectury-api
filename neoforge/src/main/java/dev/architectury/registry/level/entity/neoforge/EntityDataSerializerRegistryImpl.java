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

package dev.architectury.registry.level.entity.neoforge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityDataSerializerRegistryImpl {
    private static final Map<Identifier, EntityDataSerializer<?>> SERIALIZERS = new LinkedHashMap<>();

    public static <T> void register(Identifier id, EntityDataSerializer<T> serializer) {
        SERIALIZERS.put(id, serializer);
    }

    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> bus.register(EntityDataSerializerRegistryImpl.class));
    }

    @SubscribeEvent
    public static void event(RegisterEvent event) {
        event.register(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS.key(), helper -> {
            for (Map.Entry<Identifier, EntityDataSerializer<?>> entry : SERIALIZERS.entrySet()) {
                helper.register(entry.getKey(), entry.getValue());
            }
        });
    }
}
