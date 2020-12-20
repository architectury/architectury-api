package me.shedaniel.architectury.registry.forge;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.hooks.biome.*;
import me.shedaniel.architectury.mixin.forge.BiomeGenerationSettingsBuilderAccessor;
import me.shedaniel.architectury.mixin.forge.MobSpawnSettingsBuilderAccessor;
import me.shedaniel.architectury.registry.BiomeModifications.BiomeContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class BiomeModificationsImpl {
    private static final List<Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>>> MODIFICATIONS = Lists.newArrayList();
    
    public static void addProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        MODIFICATIONS.add(Pair.of(predicate, modifier));
    }
    
    public static void postProcessProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        MODIFICATIONS.add(Pair.of(predicate, modifier));
    }
    
    public static void removeProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        MODIFICATIONS.add(Pair.of(predicate, modifier));
    }
    
    public static void replaceProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        MODIFICATIONS.add(Pair.of(predicate, modifier));
    }
    
    static {
        MinecraftForge.EVENT_BUS.<BiomeLoadingEvent>addListener(event -> {
            BiomeContext biomeContext = wrapSelectionContext(event);
            BiomeProperties.Mutable mutableBiome = new MutableBiomeWrapped(event);
            for (Pair<Predicate<BiomeContext>, BiConsumer<BiomeContext, BiomeProperties.Mutable>> pair : MODIFICATIONS) {
                if (pair.getLeft().test(biomeContext)) {
                    pair.getRight().accept(biomeContext, mutableBiome);
                }
            }
            MutableClimatePropertiesWrapped climateProperties = (MutableClimatePropertiesWrapped) mutableBiome.getClimateProperties();
            if (climateProperties.dirty) {
                event.setClimate(new Biome.ClimateSettings(climateProperties.precipitation,
                        climateProperties.temperature,
                        climateProperties.temperatureModifier,
                        climateProperties.downfall));
            }
        });
    }
    
    private static BiomeContext wrapSelectionContext(BiomeLoadingEvent event) {
        return new BiomeContext() {
            BiomeProperties properties = new BiomeWrapped(event);
            
            @Override
            @NotNull
            public ResourceLocation getKey() {
                return event.getName();
            }
            
            @Override
            @NotNull
            public BiomeProperties getProperties() {
                return properties;
            }
        };
    }
    
    public static class BiomeWrapped implements BiomeProperties {
        protected final BiomeLoadingEvent event;
        protected final ClimateProperties climateProperties;
        protected final EffectsProperties effectsProperties;
        protected final GenerationProperties generationProperties;
        protected final SpawnProperties spawnProperties;
        
        public BiomeWrapped(BiomeLoadingEvent event) {
            this(event,
                    new BiomeHooks.ClimateWrapped(event.getClimate()),
                    new BiomeHooks.EffectsWrapped(event.getEffects()),
                    new GenerationSettingsBuilderWrapped(event.getGeneration()),
                    new SpawnSettingsBuilderWrapped(event.getSpawns())
            );
        }
        
        public BiomeWrapped(BiomeLoadingEvent event, ClimateProperties climateProperties, EffectsProperties effectsProperties, GenerationProperties generationProperties, SpawnProperties spawnProperties) {
            this.event = event;
            this.climateProperties = climateProperties;
            this.effectsProperties = effectsProperties;
            this.generationProperties = generationProperties;
            this.spawnProperties = spawnProperties;
        }
        
        @NotNull
        @Override
        public ClimateProperties getClimateProperties() {
            return climateProperties;
        }
        
        @NotNull
        @Override
        public EffectsProperties getEffectsProperties() {
            return effectsProperties;
        }
        
        @NotNull
        @Override
        public GenerationProperties getGenerationProperties() {
            return generationProperties;
        }
        
        @NotNull
        @Override
        public SpawnProperties getSpawnProperties() {
            return spawnProperties;
        }
        
        @Override
        public Biome.@NotNull BiomeCategory getCategory() {
            return event.getCategory();
        }
        
        @Override
        public float getDepth() {
            return event.getDepth();
        }
        
        @Override
        public float getScale() {
            return event.getScale();
        }
        
    }
    
    private static class GenerationSettingsBuilderWrapped implements GenerationProperties {
        protected final BiomeGenerationSettingsBuilder generation;
        
        public GenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {this.generation = generation;}
        
        @Override
        public @NotNull Optional<Supplier<ConfiguredSurfaceBuilder<?>>> getSurfaceBuilder() {
            return generation.getSurfaceBuilder();
        }
        
        @Override
        public @NotNull List<Supplier<ConfiguredWorldCarver<?>>> getCarvers(GenerationStep.Carving carving) {
            return generation.getCarvers(carving);
        }
        
        @Override
        public @NotNull List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures() {
            return ((BiomeGenerationSettingsBuilderAccessor) generation).getFeatures();
        }
        
        @Override
        public @NotNull List<Supplier<ConfiguredStructureFeature<?, ?>>> getStructureStarts() {
            return generation.getStructures();
        }
        
    }
    
    private static class SpawnSettingsBuilderWrapped implements SpawnProperties {
        protected final MobSpawnInfoBuilder builder;
        
        public SpawnSettingsBuilderWrapped(MobSpawnInfoBuilder builder) {this.builder = builder;}
        
        @Override
        public float getCreatureProbability() {
            return builder.getProbability();
        }
        
        @Override
        public @NotNull Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners() {
            return ((MobSpawnSettingsBuilderAccessor) builder).getSpawners();
        }
        
        @Override
        public @NotNull Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts() {
            return ((MobSpawnSettingsBuilderAccessor) builder).getMobSpawnCosts();
        }
        
        @Override
        public boolean isPlayerSpawnFriendly() {
            return ((MobSpawnSettingsBuilderAccessor) builder).isPlayerCanSpawn();
        }
    }
    
    public static class MutableBiomeWrapped extends BiomeWrapped implements BiomeProperties.Mutable {
        public MutableBiomeWrapped(BiomeLoadingEvent event) {
            super(event,
                    new MutableClimatePropertiesWrapped(event.getClimate()),
                    new BiomeHooks.EffectsWrapped(event.getEffects()),
                    new MutableGenerationSettingsBuilderWrapped(event.getGeneration()),
                    new MutableSpawnSettingsBuilderWrapped(event.getSpawns())
            );
        }
        
        @Override
        public @NotNull ClimateProperties.Mutable getClimateProperties() {
            return (ClimateProperties.Mutable) super.getClimateProperties();
        }
        
        @Override
        public @NotNull EffectsProperties.Mutable getEffectsProperties() {
            return (EffectsProperties.Mutable) super.getEffectsProperties();
        }
        
        @Override
        public @NotNull GenerationProperties.Mutable getGenerationProperties() {
            return (GenerationProperties.Mutable) super.getGenerationProperties();
        }
        
        @Override
        public @NotNull SpawnProperties.Mutable getSpawnProperties() {
            return (SpawnProperties.Mutable) super.getSpawnProperties();
        }
        
        @Override
        public @NotNull Mutable setCategory(Biome.@NotNull BiomeCategory category) {
            event.setCategory(category);
            return this;
        }
        
        @Override
        public @NotNull Mutable setDepth(float depth) {
            event.setDepth(depth);
            return this;
        }
        
        @Override
        public @NotNull Mutable setScale(float scale) {
            event.setScale(scale);
            return this;
        }
    }
    
    public static class MutableClimatePropertiesWrapped implements ClimateProperties.Mutable {
        public Biome.Precipitation precipitation;
        
        public float temperature;
        public Biome.TemperatureModifier temperatureModifier;
        public float downfall;
        public boolean dirty;
        
        public MutableClimatePropertiesWrapped(Biome.ClimateSettings settings) {
            this(settings.precipitation,
                    settings.temperature,
                    settings.temperatureModifier,
                    settings.downfall);
        }
        
        public MutableClimatePropertiesWrapped(Biome.Precipitation precipitation, float temperature, Biome.TemperatureModifier temperatureModifier, float downfall) {
            this.precipitation = precipitation;
            this.temperature = temperature;
            this.temperatureModifier = temperatureModifier;
            this.downfall = downfall;
        }
        
        @NotNull
        @Override
        public Biome.Precipitation getPrecipitation() {
            return precipitation;
        }
        
        @Override
        public float getTemperature() {
            return temperature;
        }
        
        @NotNull
        @Override
        public Biome.TemperatureModifier getTemperatureModifier() {
            return temperatureModifier;
        }
        
        @Override
        public float getDownfall() {
            return downfall;
        }
        
        @Override
        public @NotNull Mutable setPrecipitation(Biome.@NotNull Precipitation precipitation) {
            this.precipitation = precipitation;
            this.dirty = true;
            return this;
        }
        
        @Override
        public @NotNull Mutable setTemperature(float temperature) {
            this.temperature = temperature;
            this.dirty = true;
            return this;
        }
        
        @Override
        public @NotNull Mutable setTemperatureModifier(Biome.@NotNull TemperatureModifier temperatureModifier) {
            this.temperatureModifier = temperatureModifier;
            this.dirty = true;
            return this;
        }
        
        @Override
        public @NotNull Mutable setDownfall(float downfall) {
            this.downfall = downfall;
            this.dirty = true;
            return this;
        }
        
    }
    
    private static class MutableGenerationSettingsBuilderWrapped extends GenerationSettingsBuilderWrapped implements GenerationProperties.Mutable {
        public MutableGenerationSettingsBuilderWrapped(BiomeGenerationSettingsBuilder generation) {
            super(generation);
        }
        
        @Override
        public Mutable setSurfaceBuilder(ConfiguredSurfaceBuilder<?> builder) {
            generation.surfaceBuilder(builder);
            return this;
        }
        
        @Override
        public Mutable addFeature(GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature) {
            generation.addFeature(decoration, feature);
            return this;
        }
        
        @Override
        public Mutable addCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            generation.addCarver(carving, feature);
            return this;
        }
        
        @Override
        public Mutable addStructure(ConfiguredStructureFeature<?, ?> feature) {
            generation.addStructureStart(feature);
            return this;
        }
        
        @Override
        public Mutable removeFeature(GenerationStep.Decoration decoration, ConfiguredFeature<?, ?> feature) {
            generation.getFeatures(decoration).removeIf(supplier -> supplier.get() == feature);
            return this;
        }
        
        @Override
        public Mutable removeCarver(GenerationStep.Carving carving, ConfiguredWorldCarver<?> feature) {
            generation.getCarvers(carving).removeIf(supplier -> supplier.get() == feature);
            return this;
        }
        
        @Override
        public Mutable removeStructure(ConfiguredStructureFeature<?, ?> feature) {
            generation.getStructures().removeIf(supplier -> supplier.get() == feature);
            return this;
        }
    }
    
    private static class MutableSpawnSettingsBuilderWrapped extends SpawnSettingsBuilderWrapped implements SpawnProperties.Mutable {
        public MutableSpawnSettingsBuilderWrapped(MobSpawnInfoBuilder builder) {
            super(builder);
        }
        
        @Override
        public @NotNull Mutable setCreatureProbability(float probability) {
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
            builder.addMobCharge(entityType, cost.getCharge(), cost.getEnergyBudget());
            return this;
        }
        
        @Override
        public Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
            builder.addMobCharge(entityType, mass, gravityLimit);
            return this;
        }
        
        @Override
        public Mutable clearSpawnCost(EntityType<?> entityType) {
            getMobSpawnCosts().remove(entityType);
            return this;
        }
        
        @Override
        public @NotNull Mutable setPlayerSpawnFriendly(boolean friendly) {
            ((MobSpawnSettingsBuilderAccessor) builder).setPlayerCanSpawn(friendly);
            return this;
        }
    }
}
