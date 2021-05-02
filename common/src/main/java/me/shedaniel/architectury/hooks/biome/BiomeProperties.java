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

import net.minecraft.world.level.biome.Biome.BiomeCategory;
import org.jetbrains.annotations.NotNull;

public interface BiomeProperties {
    @NotNull
    ClimateProperties getClimateProperties();
    
    @NotNull
    EffectsProperties getEffectsProperties();
    
    @NotNull
    GenerationProperties getGenerationProperties();
    
    @NotNull
    SpawnProperties getSpawnProperties();
    
    @NotNull
    BiomeCategory getCategory();
    
    float getDepth();
    
    float getScale();
    
    interface Mutable extends BiomeProperties {
        @Override
        @NotNull
        ClimateProperties.Mutable getClimateProperties();
        
        @Override
        @NotNull
        EffectsProperties.Mutable getEffectsProperties();
        
        @Override
        @NotNull
        GenerationProperties.Mutable getGenerationProperties();
        
        @Override
        @NotNull
        SpawnProperties.Mutable getSpawnProperties();
        
        @NotNull
        Mutable setCategory(@NotNull BiomeCategory category);
        
        @NotNull
        Mutable setDepth(float depth);
        
        @NotNull
        Mutable setScale(float scale);
    }
}