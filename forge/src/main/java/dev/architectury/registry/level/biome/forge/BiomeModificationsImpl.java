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
import com.mojang.serialization.MapCodec;
import dev.architectury.hooks.forgelike.ForgeLikeHooks;
import dev.architectury.hooks.level.biome.*;
import dev.architectury.registry.level.biome.BiomeModifications.BiomeContext;
import dev.architectury.utils.ArchitecturyConstants;
import dev.architectury.utils.GameInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.*;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
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
    private static MapCodec<BiomeModifierImpl> noneBiomeModCodec = null;

    public static void init() {
        ForgeLikeHooks.registerBiomeModifier(
                Identifier.fromNamespaceAndPath(ArchitecturyConstants.MOD_ID, "none_biome_mod_codec"),
                () -> MapCodec.unit(BiomeModifierImpl.INSTANCE).codec()
        );
        noneBiomeModCodec = MapCodec.unit(BiomeModifierImpl.INSTANCE);
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
        private static final BiomeModifierImpl INSTANCE = new BiomeModifierImpl();

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> list = switch (phase) {
                case ADD -> ADDITIONS;
                case REMOVE -> REMOVALS;
                case MODIFY -> REPLACEMENTS;
                case AFTER_EVERYTHING -> POST_PROCESSING;
                default -> null;
            };

            if (list == null) {
                return;
            }

            BiomeContext biomeContext = wrapSelectionContext(biome.unwrapKey(), builder);
            BiomeProperties.Mutable mutableBiome = new MutableBiomeWrapped(builder);
            for (var pair : list) {
                if (pair.getLeft().test(biomeContext)) {
                    pair.getRight().accept(biomeContext, mutableBiome);
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return noneBiomeModCodec != null ? noneBiomeModCodec : MapCodec.unit(INSTANCE);
        }
    }

    private static BiomeContext wrapSelectionContext(Optional<ResourceKey<Biome>> biomeResourceKey, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        return new BiomeContext() {
            private final BiomeProperties properties = new BiomeWrapped(builder);

            @Override
            public Optional<Identifier> getKey() {
                return biomeResourceKey.map(ResourceKey::identifier);
            }

            @Override
            public BiomeProperties getProperties() {
                return properties;
            }

            @Override
            public boolean hasTag(TagKey<Biome> tag) {
                if (biomeResourceKey.isEmpty()) {
                    return false;
                }
                MinecraftServer server = GameInstance.getServer();
                if (server == null) {
                    return false;
                }

                Optional<? extends Registry<Biome>> registry = server.registryAccess().lookup(Registries.BIOME);
                if (registry.isEmpty()) {
                    return false;
                }

                return registry.get().get(biomeResourceKey.get()).map(holder -> holder.is(tag)).orElse(false);
            }
        };
    }

    public static class BiomeWrapped implements BiomeProperties {
        protected final ModifiableBiomeInfo.BiomeInfo.Builder builder;
        protected final ClimateProperties climateProperties;
        protected final EffectsProperties effectsProperties;
        protected final GenerationProperties generationProperties;
        protected final SpawnProperties spawnProperties;

        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            this(
                    builder,
                    new MutableClimatePropertiesWrapped(builder.getClimateSettings()),
                    new MutableEffectsPropertiesWrapped(builder.getSpecialEffects()),
                    new GenerationSettingsBuilderWrapped(builder.getGenerationSettings()),
                    new SpawnSettingsBuilderWrapped(builder.getMobSpawnSettings())
            );
        }

        public BiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder builder, ClimateProperties climateProperties, EffectsProperties effectsProperties, GenerationProperties generationProperties, SpawnProperties spawnProperties) {
            this.builder = builder;
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
        protected final BiomeGenerationSettings.PlainBuilder generation;

        public GenerationSettingsBuilderWrapped(BiomeGenerationSettings.PlainBuilder generation) {
            this.generation = generation;
        }

        @Override
        public Iterable<Holder<ConfiguredWorldCarver<?>>> getCarvers() {
            return generationCarvers(generation);
        }

        @Override
        public Iterable<Holder<PlacedFeature>> getFeatures(GenerationStep.Decoration decoration) {
            return generation.getFeatures(decoration);
        }

        @Override
        public List<Iterable<Holder<PlacedFeature>>> getFeatures() {
            return (List<Iterable<Holder<PlacedFeature>>>) (List<?>) generationFeatures(generation);
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
        public Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> getSpawners() {
            return spawnEntries(builder);
        }

        @Override
        public Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return spawnCosts(builder);
        }
    }

    public static class MutableBiomeWrapped extends BiomeWrapped implements BiomeProperties.Mutable {
        public MutableBiomeWrapped(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            super(
                    builder,
                    new MutableClimatePropertiesWrapped(builder.getClimateSettings()),
                    new MutableEffectsPropertiesWrapped(builder.getSpecialEffects()),
                    new MutableGenerationSettingsBuilderWrapped(builder.getGenerationSettings()),
                    new MutableSpawnSettingsBuilderWrapped(builder.getMobSpawnSettings())
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
        private final ClimateSettingsBuilder builder;

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
            builder.setHasPrecipitation(hasPrecipitation);
            return this;
        }

        @Override
        public Mutable setTemperature(float temperature) {
            builder.setTemperature(temperature);
            return this;
        }

        @Override
        public Mutable setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            builder.setTemperatureModifier(temperatureModifier);
            return this;
        }

        @Override
        public Mutable setDownfall(float downfall) {
            builder.setDownfall(downfall);
            return this;
        }
    }

    public static class MutableEffectsPropertiesWrapped implements EffectsProperties.Mutable {
        private final BiomeSpecialEffectsBuilder builder;

        public MutableEffectsPropertiesWrapped(BiomeSpecialEffectsBuilder builder) {
            this.builder = builder;
        }

        @Override
        public int getWaterColor() {
            return builder.waterColor().orElse(-1);
        }

        @Override
        public OptionalInt getFoliageColorOverride() {
            return builder.foliageColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public OptionalInt getDryFoliageColorOverride() {
            return builder.dryFoliageColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public OptionalInt getGrassColorOverride() {
            return builder.grassColorOverride().map(OptionalInt::of).orElseGet(OptionalInt::empty);
        }

        @Override
        public BiomeSpecialEffects.GrassColorModifier getGrassColorModifier() {
            return builder.getGrassColorModifier();
        }

        @Override
        public Mutable setWaterColor(int color) {
            builder.waterColor(color);
            return this;
        }

        @Override
        public Mutable setFoliageColorOverride(@Nullable Integer colorOverride) {
            setField(builder, "foliageColorOverride", Optional.ofNullable(colorOverride));
            return this;
        }

        @Override
        public Mutable setDryFoliageColorOverride(@Nullable Integer colorOverride) {
            setField(builder, "dryFoliageColorOverride", Optional.ofNullable(colorOverride));
            return this;
        }

        @Override
        public Mutable setGrassColorOverride(@Nullable Integer colorOverride) {
            setField(builder, "grassColorOverride", Optional.ofNullable(colorOverride));
            return this;
        }

        @Override
        public Mutable setGrassColorModifier(BiomeSpecialEffects.GrassColorModifier modifier) {
            builder.grassColorModifier(modifier);
            return this;
        }
    }

    private static class MutableGenerationSettingsBuilderWrapped extends GenerationSettingsBuilderWrapped implements GenerationProperties.Mutable {
        public MutableGenerationSettingsBuilderWrapped(BiomeGenerationSettings.PlainBuilder generation) {
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
                Optional<? extends Registry<PlacedFeature>> registry = server.registryAccess().lookup(Registries.PLACED_FEATURE);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<PlacedFeature>> holder = registry.get().get(feature);
                    if (holder.isPresent()) {
                        return addFeature(decoration, holder.get());
                    }
                    throw new IllegalArgumentException("Unknown feature: " + feature);
                }
            }
            return this;
        }

        @Override
        public Mutable addCarver(Holder<ConfiguredWorldCarver<?>> feature) {
            generation.addCarver(feature);
            return this;
        }

        @Override
        public Mutable addCarver(ResourceKey<ConfiguredWorldCarver<?>> feature) {
            MinecraftServer server = GameInstance.getServer();
            if (server != null) {
                Optional<? extends Registry<ConfiguredWorldCarver<?>>> registry = server.registryAccess().lookup(Registries.CONFIGURED_CARVER);
                if (registry.isPresent()) {
                    Optional<Holder.Reference<ConfiguredWorldCarver<?>>> holder = registry.get().get(feature);
                    if (holder.isPresent()) {
                        return addCarver(holder.get());
                    }
                    throw new IllegalArgumentException("Unknown carver: " + feature);
                }
            }
            return this;
        }

        @Override
        public Mutable removeFeature(GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature) {
            generation.getFeatures(decoration).removeIf(holder -> holder.is(feature));
            return this;
        }

        @Override
        public Mutable removeCarver(ResourceKey<ConfiguredWorldCarver<?>> feature) {
            generationCarvers(generation).removeIf(holder -> holder.is(feature));
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
        public Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data, int weight) {
            builder.addSpawn(category, weight, data);
            return this;
        }

        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            boolean removed = false;
            for (MobCategory type : builder.getSpawnerTypes()) {
                var weightedSpawnerList = builder.getSpawner(type);
                int before = weightedSpawnerList.build().unwrap().size();
                weightedSpawnerList.removeIf(data -> predicate.test(type, data));
                if (weightedSpawnerList.build().unwrap().size() != before) {
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
            spawnCosts(builder).remove(entityType);
            return this;
        }
    }

    @SuppressWarnings("unchecked")
    private static List<Holder<ConfiguredWorldCarver<?>>> generationCarvers(BiomeGenerationSettings.PlainBuilder builder) {
        return (List<Holder<ConfiguredWorldCarver<?>>>) getField(builder, "carvers");
    }

    @SuppressWarnings("unchecked")
    private static List<List<Holder<PlacedFeature>>> generationFeatures(BiomeGenerationSettings.PlainBuilder builder) {
        return (List<List<Holder<PlacedFeature>>>) getField(builder, "features");
    }

    @SuppressWarnings("unchecked")
    private static Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>> spawnEntries(MobSpawnSettingsBuilder builder) {
        return (Map<MobCategory, WeightedList.Builder<MobSpawnSettings.SpawnerData>>) getField(builder, "spawners");
    }

    @SuppressWarnings("unchecked")
    private static Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts(MobSpawnSettingsBuilder builder) {
        return (Map<EntityType<?>, MobSpawnSettings.MobSpawnCost>) getField(builder, "mobSpawnCosts");
    }

    private static Object getField(Object target, String name) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Failed to access field '" + name + "' on " + target.getClass(), exception);
            }
        }
        throw new IllegalStateException("Could not find field '" + name + "' on " + target.getClass());
    }

    private static void setField(Object target, String name, Object value) {
        Class<?> current = target.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Failed to set field '" + name + "' on " + target.getClass(), exception);
            }
        }
        throw new IllegalStateException("Could not find field '" + name + "' on " + target.getClass());
    }
}
