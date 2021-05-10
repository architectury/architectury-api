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

package me.shedaniel.architectury.hooks.biome;

import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import org.jetbrains.annotations.NotNull;

public interface ClimateProperties {
    Precipitation getPrecipitation();
    
    float getTemperature();
    
    TemperatureModifier getTemperatureModifier();
    
    float getDownfall();
    
    interface Mutable extends ClimateProperties {
        Mutable setPrecipitation(Precipitation precipitation);
        
        Mutable setTemperature(float temperature);
        
        Mutable setTemperatureModifier(TemperatureModifier temperatureModifier);
        
        Mutable setDownfall(float downfall);
    }
}
