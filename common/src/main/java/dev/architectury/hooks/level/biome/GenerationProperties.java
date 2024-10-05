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

package dev.architectury.hooks.level.biome;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface GenerationProperties {
    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers();
    
    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration);
    
    List<Iterable<Holder<PlacedFeature>>> getFeatures();
    
    interface Mutable extends GenerationProperties {
        Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);
        
        @ApiStatus.Experimental
        Mutable addFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature);
        
        Mutable addCarver(Holder<ConfiguredWorldCarver<?>> feature);
        
        @ApiStatus.Experimental
        Mutable addCarver(ResourceKey<ConfiguredWorldCarver<?>> feature);
        
        Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature);
        
        Mutable removeCarver(ResourceKey<ConfiguredWorldCarver<?>> feature);
    }
}
