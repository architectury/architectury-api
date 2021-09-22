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

package dev.architectury.hooks.level.biome;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface GenerationProperties {
    Optional<Supplier<ConfiguredSurfaceBuilder<?>>> getSurfaceBuilder();
    
    List<Supplier<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving);
    
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();
    
    interface Mutable extends GenerationProperties {
        Mutable setSurfaceBuilder(ConfiguredSurfaceBuilder<?> builder);
        
        Mutable addFeature(GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature);
        
        Mutable addCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature);
        
        Mutable addStructure(ConfiguredStructureFeature<?, ?> feature);
        
        Mutable removeFeature(GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature);
        
        Mutable removeCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature);
        
        Mutable removeStructure(ConfiguredStructureFeature<?, ?> feature);
    }
}
