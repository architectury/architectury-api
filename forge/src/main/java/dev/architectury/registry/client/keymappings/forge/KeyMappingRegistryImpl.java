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

package dev.architectury.registry.client.keymappings.forge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class KeyMappingRegistryImpl {
    private static final Logger LOGGER = LogManager.getLogger(KeyMappingRegistryImpl.class);
    private static final List<KeyMapping> MAPPINGS = new ArrayList<>();
    private static boolean eventCalled = false;
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.addListener(KeyMappingRegistryImpl::event);
        });
    }
    
    public static void register(KeyMapping mapping) {
        if (eventCalled) {
            Options options = Minecraft.getInstance().options;
            options.keyMappings = ArrayUtils.add(options.keyMappings, mapping);
            LOGGER.warn("Key mapping %s registered after event".formatted(mapping.getName()), new RuntimeException());
        } else {
            MAPPINGS.add(mapping);
        }
    }
    
    public static void event(RegisterKeyMappingsEvent event) {
        MAPPINGS.forEach(event::register);
        eventCalled = true;
    }
}
