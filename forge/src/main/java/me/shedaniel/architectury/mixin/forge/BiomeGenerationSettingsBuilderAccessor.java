package me.shedaniel.architectury.mixin.forge;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.Builder.class)
public interface BiomeGenerationSettingsBuilderAccessor {
    @Accessor
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();
}
