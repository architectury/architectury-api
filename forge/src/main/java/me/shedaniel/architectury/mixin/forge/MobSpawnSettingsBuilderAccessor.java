package me.shedaniel.architectury.mixin.forge;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(MobSpawnSettings.Builder.class)
public interface MobSpawnSettingsBuilderAccessor {
    @Accessor
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();
    
    @Accessor
    boolean isPlayerCanSpawn();
    
    @Accessor
    void setPlayerCanSpawn(boolean canSpawn);
    
    @Accessor
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();
}
