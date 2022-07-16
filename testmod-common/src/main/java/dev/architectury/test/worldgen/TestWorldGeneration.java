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

package dev.architectury.test.worldgen;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.test.TestMod;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class TestWorldGeneration {
    public static void initialize() {
        LifecycleEvent.SETUP.register(() -> {
            Holder<ConfiguredFeature<OreConfiguration, ?>> configuredFeature = FeatureUtils.register(TestMod.MOD_ID + ":diamond_blocks", Feature.ORE,
                    new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.DIAMOND_BLOCK.defaultBlockState(), 33));
            Holder<PlacedFeature> placedFeature = PlacementUtils.register(TestMod.MOD_ID + ":diamond_blocks", configuredFeature,
                    List.of(CountPlacement.of(4), InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)),
                            BiomeFilter.biome()));
            BiomeModifications.addProperties((ctx, mutable) -> {
                if (ctx.hasTag(BiomeTags.IS_FOREST)) {
                    mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, placedFeature);
                }
            });
        });
    }
}
