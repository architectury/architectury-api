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

package me.shedaniel.architectury.compat.fabric;

import com.google.common.collect.Maps;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.architectury.platform.fabric.PlatformImpl;
import me.shedaniel.architectury.platform.Mod;

import java.util.Map;

public class ModMenuCompatibility implements ModMenuApi {
    private static final Map<String, ConfigScreenFactory<?>> FACTORIES = Maps.newHashMap();
    
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        validateMap();
        return FACTORIES;
    }
    
    private void validateMap() {
        for (Map.Entry<String, Mod.ConfigurationScreenProvider> entry : PlatformImpl.CONFIG_SCREENS.entrySet()) {
            if (!FACTORIES.containsKey(entry.getKey())) {
                FACTORIES.put(entry.getKey(), entry.getValue()::provide);
            }
        }
    }
}
