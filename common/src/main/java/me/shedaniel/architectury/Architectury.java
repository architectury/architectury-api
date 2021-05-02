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

package me.shedaniel.architectury;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApiStatus.Internal
public class Architectury {
    private static final String MOD_LOADER;
    private static final ImmutableMap<String,String> MOD_LOADERS = ImmutableMap.<String,String>builder()
            .put("net.fabricmc.loader.FabricLoader", "fabric")
            .put("net.minecraftforge.fml.common.Mod", "forge")
            .build();
    
    public static String getModLoader() {
        return MOD_LOADER;
    }
    
    static {
        List<String> loader = new ArrayList<>();
        for (Map.Entry<String, String> entry : MOD_LOADERS.entrySet()) {
            try {
                Class.forName(entry.getKey(), false, Architectury.class.getClassLoader());
                loader.add(entry.getValue());
                break;
            } catch (ClassNotFoundException ignored) {}
        }
        if (loader.isEmpty())
            throw new IllegalStateException("No detected mod loader!");
        if (loader.size() >= 2)
            LogManager.getLogger().error("Detected multiple mod loaders! Something is wrong on the classpath! " + String.join(", ", loader));
        MOD_LOADER = loader.get(0);
    }
}
