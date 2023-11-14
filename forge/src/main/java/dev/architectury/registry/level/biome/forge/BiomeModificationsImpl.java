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

package dev.architectury.registry.level.biome.forge;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import dev.architectury.hooks.level.biome.*;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.level.biome.BiomeModifications.BiomeContext;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BiomeModificationsImpl {
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> ADDITIONS = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> POST_PROCESSING = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> REMOVALS = Lists.newArrayList();
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> REPLACEMENTS = Lists.newArrayList();
    @Nullable
    private static Codec<BiomeModifierImpl> noneBiomeModCodec = null;
    
    public static void init() {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID,bus -> {
            bus.<RegisterEvent>addListener(event -> {
                event.register(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, registry -> {
                    registry.register(new ResourceLocation(ArchitecturyConstants.MOD_ID, "none_biome_mod_codec"),
                            noneBiomeModCodec = Codec.unit(BiomeModifierImpl.INSTANCE));
                });
            });
        });
    }
    
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
    
    private static class BiomeModifierImpl implements BiomeModifier {
        // cry about it
        private static final BiomeModifierImpl INSTANCE = new BiomeModifierImpl();
        
        @Override
        public void modify(Holder<Biome> arg, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> list = switch (phase) {
                case ADD -> ADDITIONS;
                case REMOVE -> REMOVALS;
                case MODIFY -> REPLACEMENTS;
                case AFTER_EVERYTHING -> POST_PROCESSING;
                default -> null;
            };
            
            if (list == null) return;
            BiomeContext biomeContext = wrapSelectionContext(arg.unwrapKey(), builder);
            BiomeProperties.Mutable mutableBiome = new MutableBiomeWrapped(builder);
            for (var pair : list) {
                if (pair.getLeft().test(biomeContext)) {
                    pair.getRight().accept(biomeContext, mutableBiome);
                }
            }
        }
        
        @Override
        public Codec<? extends BiomeModifier> codec() {
            if (noneBiomeModCodec != null) {
                return noneBiomeModCodec;
            } else {
                return Codec.unit(INSTANCE);
            }
        }
    }
    
    private static BiomeContext wrapSelectionContext(Optional<ResourceKey<Biome>> biomeResourceKey, ModifiableBiomeInfo.BiomeInfo.Builder event) {
        return new BiomeContext() {
            BiomeProperties properties = new BiomeWrapped(event);
            
            @Override
            public Optional<ResourceLocation> getKey() {
                return biomeResourceKey.map(ResourceKey::location);
            }
            
            @Override
            public BiomeProperties getProperties() {
                return properties;
            }
            
            @Override
            public boolean hasTag(TagKey<Biome> tag) {
                MinecraftServer server = GameInstance.getServer();
                if (server != null) {
                    Optional<? extends Registry<Biome>> registry = server.registryAccess().registry(Registries.BIOME);
                    if (registry.isPresent()) {
                        Optional<Holder.Reference<Biome>> holder = registry.get().getHolder(biomeResourceKey.get());
                        if (holder.isPresent()) {
                            return holder.get().is(tag);
                        }
                    }
                }
                return false;
            }
        };
    }
    
    public static class BiomeWrapped implements BiomeProperties {
        protected final ModifiableBiomeInfo.BiomeInfo.Builder event;
        protected final ClimateProperties climateProperties;
        protected final EffectsProperties effectsProperties;
        protected final GenerationProperties generationProperties;
        protected final SpawnProperties spawnProperties;
        
        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event) {
            this(event,
                    new MutableClimatePropertiesWrapped(event.getClimateSettings()),
                    new MutableEffectsPropertiesWrapped(event.getSpecialEffects()),
                    new GenerationSettingsBuilderWrapped(event.getGenerationSettings()),
                    new SpawnSettingsBuilderWrapped(event.getMobSpawnSettings())
            );
        }
        
        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event, ClimateProperties climateProperties, EffectsProperties effectsProperties, GenerationProperties generationProperties, SpawnProperties spawnProperties) {
            this.event = event;
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
    
    private static class GenerationSettingsBuilderWrapped implements GenerationProperties {
        protected final BiomeGenerationSettingsBuilder generation;
        
        public GenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {
            this.generation = generation;
        }
        
        @Override
        public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
            return generation.getCarvers(carving);
        }
        
        @Override
        public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
            return generation.getFeatures(decoration);
        }
        
        @Override
        public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
            return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) generation.features;
        }
    }
    
    private static class SpawnSettingsBuilderWrapped implements SpawnProperties {
        protected final MobSpawnSettingsBuilder builder;
        
        public SpawnSettingsBuilderWrapped(MobSpawnSettingsBuilder builder) {
            this.builder = builder;
        }
        
        @Override
        public float getCreatureProbability() {
            return builder.getProbability();
        }
        
        @Override
        public Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
            return builder.spawners;
        }
        
        @Override
        public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return builder.mobSpawnCosts;
        }
    }
    
    public static class MutableBiomeWrapped extends BiomeWrapped implements BiomeProperties.Mutable {
        public MutableBiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder event) {
            super(event,
                    new MutableClimatePropertiesWrapped(event.getClimateSettings()),
                    new MutableEffectsPropertiesWrapped(event.getSpecialEffects()),
                    new MutableGenerationSettingsBuilderWrapped(event.getGenerationSettings()),
                    new MutableSpawnSettingsBuilderWrapped(event.getMobSpawnSettings())
            );
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
    
    public static class MutableClimatePropertiesWrapped implements ClimateProperties.Mutable {
        public ClimateSettingsBuilder builder;
        
        public MutableClimatePropertiesWrapped(ClimateSettingsBuilder builder) {
            this.builder = builder;
        }
        
        @Override
        public boolean hasPrecipitation() {
            return builder.hasPrecipitation();
        }
        
        @Override
        public float getTemperature() {
            return builder.getTemperature();
        }
        
        @Override
        public Biome.TemperatureModifier getTemperatureModifier() {
            return builder.getTemperatureModifier();
        }
        
        @Override
        public float getDownfall() {
            return builder.getDownfall();
        }
        
        @Override
        public Mutable setHasPrecipitation(boolean hasPrecipitation) {
            this.builder.setHasPrecipitation(hasPrecipitation);
            return this;
        }
        
        @Override
        public Mutable setTemperature(float temperature) {
            this.builder.setTemperature(temperature);
            return this;
        }
        
        @Override
        public Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            this.builder.setTemperatureModifier(temperatureModifier);
            return this;
        }
        
        @Override
        public Mutable setDownfall(float downfall) {
            this.builder.setDownfall(downfall);
            return this;
        }
        
    }
    
    public static class MutableEffectsPropertiesWrapped implements EffectsProperties.Mutable {
        public BiomeSpecialEffects.Builder builder;
        
        public MutableEffectsPropertiesWrapped(BiomeSpecialEffects.Builder builder) {
            this.builder = builder;
        }
        
        @Override
        public int getFogColor() {
            if (builder instanceof BiomeSpecialEffectsBuilder b) return b.getFogColor();
            return builder.fogColor.orElse(-1);
        }
        
        @Override
        public int getWaterColor() {
            if (builder instanceof BiomeSpecialEffectsBuilder b) return b.getWaterFogColor();
            return builder.waterColor.orElse(-1);
        }
        
        @Override
        public int getWaterFogColor() {
            if (builder instanceof BiomeSpecialEffectsBuilder b) return b.getWaterFogColor();
            return builder.waterFogColor.orElse(-1);
        }
        
        @Override
        public int getSkyColor() {
            if (builder instanceof BiomeSpecialEffectsBuilder b) return b.getSkyColor();
            return builder.skyColor.orElse(-1);
        }
        
        @Override
        public OptionalInt getFoliageColorOverride() {
            return builder.foliageColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }
        
        @Override
        public OptionalInt getGrassColorOverride() {
            return builder.grassColorOverride.map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }
        
        @Override
        public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
            return builder.grassColorModifier;
        }
        
        @Override
        public Optional<AmbientParticleSettings> getAmbientParticle() {
            return builder.ambientParticle;
        }
        
        @Override
        public Optional<Holder<SoundEvent>> getAmbientLoopSound() {
            return builder.ambientLoopSoundEvent;
        }
        
        @Override
        public Optional<AmbientMoodSettings> getAmbientMoodSound() {
            return builder.ambientMoodSettings;
        }
        
        @Override
        public Optional<AmbientAdditionsSettings> getAmbientAdditionsSound() {
            return builder.ambientAdditionsSettings;
        }
        
        @Override
        public Optional<Music> getBackgroundMusic() {
            return builder.backgroundMusic;
        }
        
        @Override
        public Mutable setFogColor(int color) {
            builder.fogColor(color);
            return this;
        }
        
        @Override
        public Mutable setWaterColor(int color) {
            builder.waterColor(color);
            return this;
        }
        
        @Override
        public Mutable setWaterFogColor(int color) {
            builder.waterFogColor(color);
            return this;
        }
        
        @Override
        public Mutable setSkyColor(int color) {
            builder.skyColor(color);
            return this;
        }
        
        @Override
        public Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            builder.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }
        
        @Override
        public Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            builder.foliageColorOverride = Optional.ofNullable(colorOverride);
            return this;
        }
        
        @Override
        public Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            builder.grassColorModifier(modifier);
            return this;
        }
        
        @Override
        public Mutable setAmbientParticle(@Nullable AmbientParticleSettings settings) {
            builder.ambientParticle = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public Mutable setAmbientLoopSound(@Nullable Holder<SoundEvent> sound) {
            builder.ambientLoopSoundEvent = Optional.ofNullable(sound);
            return this;
        }
        
        @Override
        public Mutable setAmbientMoodSound(@Nullable AmbientMoodSettings settings) {
            builder.ambientMoodSettings = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public Mutable setAmbientAdditionsSound(@Nullable AmbientAdditionsSettings settings) {
            builder.ambientAdditionsSettings = Optional.ofNullable(settings);
            return this;
        }
        
        @Override
        public Mutable setBackgroundMusic(@Nullable Music music) {
            builder.backgroundMusic = Optional.ofNullable(music);
            return this;
        }
    }
    
    private static class MutableGenerationSettingsBuilderWrapped extends GenerationSettingsBuilderWrapped implements GenerationProperties.Mutable {
        public MutableGenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {
            super(generation);
        }
        
        @Override
        public Mutable addFeature(GenerationStep.Decoration decoration, Holder<PlacedFeature> feature) {
            generation.addFeature(decoration, feature);
            return this;
        }
        
        @Override
        public Mutable addFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                Optional<? extends Registry<PlacedFeature>> registry = server.registryAccess().registry(Registries.PLACED_FEATURE);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<PlacedFeature>> holder = registry.get().getHolder(feature);
                    if (holder.isPresent()) {
                        return addFeature(decoration, holder.get());
                    } else {
                        throw new IllegalArgumentException("Unknown feature: " + feature);
                    }
                }
            }
            return this;
        }
        
        @Override
        public Mutable addCarver(GenerationStep.Carving carving, Holder<ConfiguredWorldCarver<?>> feature) {
            generation.addCarver(carving, feature);
            return this;
        }
        
        @Override
        public Mutable addCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                Optional<? extends Registry<ConfiguredWorldCarver<?>>> registry = server.registryAccess().registry(Registries.CONFIGURED_CARVER);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<ConfiguredWorldCarver<?>>> holder = registry.get().getHolder(feature);
                    if (holder.isPresent()) {
                        return addCarver(carving, holder.get());
                    } else {
                        throw new IllegalArgumentException("Unknown carver: " + feature);
                    }
                }
            }
            return this;
        }
        
        @Override
        public Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            generation.getFeatures(decoration).removeIf(supplier -> supplier.is(feature));
            return this;
        }
        
        @Override
        public Mutable removeCarver(GenerationStep.Carving carving, ResourceKey<ConfiguredWorldCarver<?>> feature) {
            generation.getCarvers(carving).removeIf(supplier -> supplier.is(feature));
            return this;
        }
    }
    
    private static class MutableSpawnSettingsBuilderWrapped extends SpawnSettingsBuilderWrapped implements SpawnProperties.Mutable {
        public MutableSpawnSettingsBuilderWrapped(MobSpawnSettingsBuilder builder) {
            super(builder);
        }
        
        @Override
        public Mutable setCreatureProbability(float probability) {
            builder.creatureGenerationProbability(probability);
            return this;
        }
        
        @Override
        public Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data) {
            builder.addSpawn(category, data);
            return this;
        }
        
        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            boolean removed = false;
            for (MobCategory type : builder.getSpawnerTypes()) {
                if (builder.getSpawner(type).removeIf(data -> predicate.test(type, data))) {
                    removed = true;
                }
            }
            return removed;
        }
        
        @Override
        public Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost) {
            builder.addMobCharge(entityType, cost.charge(), cost.energyBudget());
            return this;
        }
        
        @Override
        public Mutable setSpawnCost(EntityType<?> entityType, double charge, double energyBudget) {
            builder.addMobCharge(entityType, charge, energyBudget);
            return this;
        }
        
        @Override
        public Mutable clearSpawnCost(EntityType<?> entityType) {
            getMobSpawnCosts().remove(entityType);
            return this;
        }
    }
}
