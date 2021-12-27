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

package dev.architectury.registry.level.biome.fabric;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import dev.architectury.hooks.level.biome.*;
import dev.architectury.registry.level.biome.BiomeModifications.BiomeContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.biome.Biome.TemperatureModifier;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static net.fabricmc.fabric.api.biome.v1.BiomeModificationContext.*;

public class BiomeModificationsImpl {
    private static final ResourceLocation FABRIC_MODIFICATION = new ResourceLocation("architectury", "fabric_modification");
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> ADDITIONS = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> POST_PROCESSING = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> REMOVALS = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> REPLACEMENTS = Lists.newArrayList();
    
    public static void addProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        ADDITIONS.add(Pair.of(predicate, modifier));
    }
    
    public static void postProcessProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        POST_PROCESSING.add(Pair.of(predicate, modifier));
    }
    
    public static void removeProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        REMOVALS.add(Pair.of(predicate, modifier));
    }
    
    public static void replaceProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        REPLACEMENTS.add(Pair.of(predicate, modifier));
    }
    
    static {
        var modification = net.fabricmc.fabric.api.biome.v1.BiomeModifications.create(FABRIC_MODIFICATION);
        registerModification(modification, ModificationPhase.ADDITIONS, ADDITIONS);
        registerModification(modification, ModificationPhase.POST_PROCESSING, POST_PROCESSING);
        registerModification(modification, ModificationPhase.REMOVALS, REMOVALS);
        registerModification(modification, ModificationPhase.REPLACEMENTS, REPLACEMENTS);
    }
    
    private static void registerModification(BiomeModification modification, ModificationPhase phase, List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> list) {
        modification.add(phase, Predicates.alwaysTrue(), (biomeSelectionContext, biomeModificationContext) -> {
            var biomeContext = wrapSelectionContext(biomeSelectionContext);
            var mutableBiome = wrapMutableBiome(biomeSelectionContext.getBiome(), biomeModificationContext);
            for (var pair : list) {
                if (pair.getLeft().test(biomeContext)) {
                    pair.getRight().accept(biomeContext, mutableBiome);
                }
            }
        });
    }
    
    private static BiomeContext wrapSelectionContext(BiomeSelectionContext context) {
        return new BiomeContext() {
            BiomeProperties properties = BiomeHooks.getBiomeProperties(context.getBiome());
            
            @Override
            @NotNull
            public ResourceLocation getKey() {
                return context.getBiomeKey().location();
            }
            
            @Override
            @NotNull
            public BiomeProperties getProperties() {
                return properties;
            }
        };
    }
    
    private static BiomeProperties.Mutable wrapMutableBiome(Biome biome, BiomeModificationContext context) {
        return new BiomeHooks.MutableBiomeWrapped(
                biome,
                wrapWeather(biome, context.getWeather()),
                wrapEffects(biome, context.getEffects()),
                new MutableGenerationProperties(biome, context.getGenerationSettings()),
                new MutableSpawnProperties(biome, context.getSpawnSettings())
        ) {
            @Override
            @NotNull
            public BiomeProperties.Mutable setCategory(@NotNull BiomeCategory category) {
                context.setCategory(category);
                return this;
            }
        };
    }
    
    private static class MutableGenerationProperties extends BiomeHooks.GenerationSettingsWrapped implements GenerationProperties.Mutable {
        protected final GenerationSettingsContext context;
        
        public MutableGenerationProperties(Biome biome, GenerationSettingsContext context) {
            super(biome);
            this.context = context;
        }
        
        @Override
        public Mutable addFeature(GenerationStep.Decoration decoration, PlacedFeature feature) {
            this.context.addBuiltInFeature(decoration, feature);
            return this;
        }
        
        @Override
        public Mutable addCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            context.addBuiltInCarver(carving, feature);
            return this;
        }
        
        /*@Override
        public Mutable addStructure(ConfiguredStructureFeature<?, ?> feature) {
            context.addBuiltInStructure(feature);
            return this;
        }*/
        
        @Override
        public Mutable removeFeature(GenerationStep.Decoration decoration, PlacedFeature feature) {
            context.removeBuiltInFeature(decoration, feature);
            return this;
        }
        
        @Override
        public Mutable removeCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            context.removeBuiltInCarver(carving, feature);
            return this;
        }
        
        /*@Override
        public Mutable removeStructure(ConfiguredStructureFeature<?, ?> feature) {
            context.removeBuiltInStructure(feature);
            return this;
        }*/
    }
    
    private static class MutableSpawnProperties extends BiomeHooks.SpawnSettingsWrapped implements SpawnProperties.Mutable {
        protected final SpawnSettingsContext context;
        
        public MutableSpawnProperties(Biome biome, SpawnSettingsContext context) {
            super(biome);
            this.context = context;
        }
        
        @Override
        public @NotNull Mutable setCreatureProbability(float probability) {
            context.setCreatureSpawnProbability(probability);
            return this;
        }
        
        @Override
        public Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
            context.addSpawn(category, data);
            return this;
        }
        
        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            return context.removeSpawns(predicate);
        }
        
        @Override
        public Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
            context.setSpawnCost(entityType, cost.getCharge(), cost.getEnergyBudget());
            return this;
        }
        
        @Override
        public Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
            context.setSpawnCost(entityType, mass, gravityLimit);
            return this;
        }
        
        @Override
        public Mutable clearSpawnCost(EntityType<?> entityType) {
            context.clearSpawnCost(entityType);
            return this;
        }
    }
    
    private static ClimateProperties.Mutable wrapWeather(Biome biome, WeatherContext context) {
        return new BiomeHooks.ClimateWrapped(biome) {
            @Override
            @NotNull
            public ClimateProperties.Mutable setPrecipitation(@NotNull Precipitation precipitation) {
                context.setPrecipitation(precipitation);
                return this;
            }
            
            @Override
            @NotNull
            public ClimateProperties.Mutable setTemperature(float temperature) {
                context.setTemperature(temperature);
                return this;
            }
            
            @Override
            @NotNull
            public ClimateProperties.Mutable setTemperatureModifier(@NotNull TemperatureModifier temperatureModifier) {
                context.setTemperatureModifier(temperatureModifier);
                return this;
            }
            
            @Override
            @NotNull
            public ClimateProperties.Mutable setDownfall(float downfall) {
                context.setDownfall(downfall);
                return this;
            }
        };
    }
    
    private static EffectsProperties.Mutable wrapEffects(Biome biome, EffectsContext context) {
        return new BiomeHooks.EffectsWrapped(biome) {
            @Override
            @NotNull
            public EffectsProperties.Mutable setFogColor(int color) {
                context.setFogColor(color);
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setWaterColor(int color) {
                context.setWaterColor(color);
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setWaterFogColor(int color) {
                context.setWaterFogColor(color);
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setSkyColor(int color) {
                context.setSkyColor(color);
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
                context.setFoliageColor(Optional.ofNullable(colorOverride));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
                context.setGrassColor(Optional.ofNullable(colorOverride));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setGrassColorModifier(@NotNull GrassColorModifier modifier) {
                context.setGrassColorModifier(modifier);
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
                context.setParticleConfig(Optional.ofNullable(settings));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setAmbientLoopSound(@Nullable SoundEvent sound) {
                context.setAmbientSound(Optional.ofNullable(sound));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
                context.setMoodSound(Optional.ofNullable(settings));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
                context.setAdditionsSound(Optional.ofNullable(settings));
                return this;
            }
            
            @Override
            @NotNull
            public EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music) {
                context.setMusic(Optional.ofNullable(music));
                return this;
            }
        };
    }
    
}
