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

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.biome.BiomeSpecialEffects.GrassColorModifier;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class BiomeHooks {
    public static BiomeProperties getBiomeProperties(Biome biome) {
        return new BiomeWrapped(biome);
    }
    
    public static class BiomeWrapped implements BiomeProperties {
        protected final Biome biome;
        protected final ClimateProperties climateProperties;
        protected final EffectsProperties effectsProperties;
        protected final GenerationProperties generationProperties;
        protected final SpawnProperties spawnProperties;
        
        public BiomeWrapped(Biome biome) {
            this(biome,
                    new ClimateWrapped(biome),
                    new EffectsWrapped(biome),
                    new GenerationSettingsWrapped(biome),
                    new SpawnSettingsWrapped(biome));
        }
        
        public BiomeWrapped(Biome biome,
                            ClimateProperties climateProperties,
                            EffectsProperties effectsProperties,
                            GenerationProperties generationProperties,
                            SpawnProperties spawnProperties) {
            this.biome = biome;
            this.climateProperties = climateProperties;
            this.effectsProperties = effectsProperties;
            this.generationProperties = generationProperties;
            this.spawnProperties = spawnProperties;
        }
        
        @Override
        public ClimateProperties getClimateProperties() {
            return climateProperties;
        }
        
        @Override
        public EffectsProperties getEffectsProperties() {
            return effectsProperties;
        }
        
        @Override
        public GenerationProperties getGenerationProperties() {
            return generationProperties;
        }
        
        @Override
        public SpawnProperties getSpawnProperties() {
            return spawnProperties;
        }
    }
    
    public static class MutableBiomeWrapped extends BiomeWrapped implements BiomeProperties.Mutable {
        public MutableBiomeWrapped(Biome biome,
                                   GenerationProperties.Mutable generationProperties,
                                   SpawnProperties.Mutable spawnProperties) {
            this(biome,
                    new ClimateWrapped(extractClimateSettings(biome)),
                    new EffectsWrapped(biome.getSpecialEffects()),
                    generationProperties,
                    spawnProperties);
        }
        
        public MutableBiomeWrapped(Biome biome,
                                   ClimateProperties.Mutable climateProperties,
                                   EffectsProperties.Mutable effectsProperties,
                                   GenerationProperties.Mutable generationProperties,
                                   SpawnProperties.Mutable spawnProperties) {
            super(biome,
                    climateProperties,
                    effectsProperties,
                    generationProperties,
                    spawnProperties);
        }
        
        @Override
        public ClimateProperties.Mutable getClimateProperties() {
            return (ClimateProperties.Mutable) super.getClimateProperties();
        }
        
        @Override
        public EffectsProperties.Mutable getEffectsProperties() {
            return (EffectsProperties.Mutable) super.getEffectsProperties();
        }
        
        @Override
        public GenerationProperties.Mutable getGenerationProperties() {
            return (GenerationProperties.Mutable) super.getGenerationProperties();
        }
        
        @Override
        public SpawnProperties.Mutable getSpawnProperties() {
            return (SpawnProperties.Mutable) super.getSpawnProperties();
        }
    }
    
    @ExpectPlatform
    private static Biome.ClimateSettings extractClimateSettings(Biome biome) {
        return null;
    }
    
    public static class ClimateWrapped implements ClimateProperties.Mutable {
        
        protected final Biome.ClimateSettings climateSettings;
        
        public ClimateWrapped(Biome biome) {
            this(extractClimateSettings(biome));
        }
        
        public ClimateWrapped(Biome.ClimateSettings climateSettings) {
            this.climateSettings = climateSettings;
        }
        
        @Override
        public Mutable setPrecipitation(Biome.Precipitation precipitation) {
            climateSettings.precipitation = precipitation;
            return this;
        }
        
        @Override
        public Mutable setTemperature(float temperature) {
            climateSettings.temperature = temperature;
            return this;
        }
        
        @Override
        public Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            climateSettings.temperatureModifier = temperatureModifier;
            return this;
        }
        
        @Override
        public Mutable setDownfall(float downfall) {
            climateSettings.downfall = downfall;
            return this;
        }
        
        @Override
        public Biome.Precipitation getPrecipitation() {
            return climateSettings.precipitation;
        }
        
        @Override
        public float getTemperature() {
            return climateSettings.temperature;
        }
        
        @Override
        public Biome.TemperatureModifier getTemperatureModifier() {
            return climateSettings.temperatureModifier;
        }
        
        @Override
        public float getDownfall() {
            return climateSettings.downfall;
        }
    }
    
    public static class EffectsWrapped implements EffectsProperties.Mutable {
        protected final BiomeSpecialEffects effects;
        
        public EffectsWrapped(Biome biome) {
            this(biome.getSpecialEffects());
        }
        
        public EffectsWrapped(BiomeSpecialEffects effects) {
            this.effects = effects;
        }
        
        @Override
        public EffectsProperties.Mutable setFogColor(int color) {
            effects.fogColor = color;
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setWaterColor(int color) {
            effects.waterColor = color;
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setWaterFogColor(int color) {
            effects.waterFogColor = color;
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setSkyColor(int color) {
            effects.skyColor = color;
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            effects.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            effects.grassColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setGrassColorModifier(GrassColorModifier modifier) {
            effects.grassColorModifier = modifier;
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            effects.ambientParticleSettings = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
            effects.ambientLoopSoundEvent = Optional.ofNullable(sound);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            effects.ambientMoodSettings = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            effects.ambientAdditionsSettings = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public EffectsProperties.Mutable setBackgroundMusic(@Nullable Music music) {
            effects.backgroundMusic = Optional.ofNullable(music);
            return this;
        }
        
        @Override
        public int getFogColor() {
            return effects.fogColor;
        }
        
        @Override
        public int getWaterColor() {
            return effects.waterColor;
        }
        
        @Override
        public int getWaterFogColor() {
            return effects.waterFogColor;
        }
        
        @Override
        public int getSkyColor() {
            return effects.skyColor;
        }
        
        @Override
        public OptionalInt getFoliageColorOverride() {
            return effects.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }
        
        @Override
        public OptionalInt getGrassColorOverride() {
            return effects.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }
        
        @Override
        public GrassColorModifier getGrassColorModifier() {
            return effects.grassColorModifier;
        }
        
        @Override
        public Optional<AmbientParticleSettings> getAmbientParticle() {
            return effects.ambientParticleSettings;
        }
        
        @Override
        public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
            return effects.ambientLoopSoundEvent;
        }
        
        @Override
        public Optional<AmbientMoodSettings> getAmbientMoodSound() {
            return effects.ambientMoodSettings;
        }
        
        @Override
        public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
            return effects.ambientAdditionsSettings;
        }
        
        @Override
        public Optional<Music> getBackgroundMusic() {
            return effects.backgroundMusic;
        }
    }
    
    public static class GenerationSettingsWrapped implements GenerationProperties {
        protected final BiomeGenerationSettings settings;
        
        public GenerationSettingsWrapped(Biome biome) {
            this(biome.getGenerationSettings());
        }
        
        public GenerationSettingsWrapped(BiomeGenerationSettings settings) {
            this.settings = settings;
        }
        
        @Override
        public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
            return settings.getCarvers(carving);
        }
        
        @Override
        public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
            if (decoration.ordinal() >= settings.features().size()) {
                return Collections.emptyList();
            }
            return settings.features().get(decoration.ordinal());
        }
        
        @Override
        public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
            return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) settings.features();
        }
    }
    
    public static class SpawnSettingsWrapped implements SpawnProperties {
        protected final MobSpawnSettings settings;
        
        public SpawnSettingsWrapped(Biome biome) {
            this(biome.getMobSettings());
        }
        
        public SpawnSettingsWrapped(MobSpawnSettings settings) {
            this.settings = settings;
        }
        
        @Override
        public float getCreatureProbability() {
            return this.settings.getCreatureProbability();
        }
        
        @Override
        public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
            return null;
        }
        
        @Override
        public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return null;
        }
    }
}
