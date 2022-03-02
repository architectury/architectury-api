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
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.Optional;

public interface GenerationProperties {
    Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving);
    
    Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration);
    
    List<Iterable<Holder<PlacedFeature>>> getFeatures();
    
    interface Mutable extends GenerationProperties {
        @Deprecated
        default Mutable addFeature(GenerationStep.Decoration decoration, PlacedFeature feature) {
            Optional<ResourceKey<PlacedFeature>> key = BuiltinRegistries.PLACED_FEATURE.getResourceKey(feature);
            if (key.isPresent()) {
                return addFeature(decoration, BuiltinRegistries.PLACED_FEATURE.getHolderOrThrow(key.get()));
            } else {
                return addFeature(decoration, Holder.direct(feature));
            }
        }
        
        Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature);
        
        @Deprecated
        default Mutable addCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            Optional<ResourceKey<ConfiguredWorldCarver<?>>> key = BuiltinRegistries.CONFIGURED_CARVER.getResourceKey(feature);
            if (key.isPresent()) {
                return addCarver(carving, BuiltinRegistries.CONFIGURED_CARVER.getHolderOrThrow(key.get()));
            } else {
                return addCarver(carving, Holder.direct(feature));
            }
        }
        
        Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature);
        
        @Deprecated
        default Mutable removeFeature(GenerationStep.Decoration decoration, PlacedFeature feature) {
            Optional<ResourceKey<PlacedFeature>> key = BuiltinRegistries.PLACED_FEATURE.getResourceKey(feature);
            if (key.isPresent()) {
                return removeFeature(decoration, key.get());
            } else {
                // This is dumb
                System.err.println("Attempted to remove a dynamic feature with a deprecated builtin method!");
                return this;
            }
        }
        
        Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature);
        
        @Deprecated
        default Mutable removeCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            Optional<ResourceKey<ConfiguredWorldCarver<?>>> key = BuiltinRegistries.CONFIGURED_CARVER.getResourceKey(feature);
            if (key.isPresent()) {
                return removeCarver(carving, key.get());
            } else {
                // This is dumb
                System.err.println("Attempted to remove a dynamic carver with a deprecated builtin method!");
                return this;
            }
        }
        
        Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature);
    }
}
