package me.shedaniel.architectury.hooks.biome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public interface SpawnProperties {
    float getCreatureProbability();
    
    @NotNull
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();
    
    @NotNull
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();
    
    boolean isPlayerSpawnFriendly();
    
    interface Mutable extends SpawnProperties {
        @NotNull
        Mutable setCreatureProbability(float probability);
        
        Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data);
        
        boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate);
        
        Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost);
        
        Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit);
        
        Mutable clearSpawnCost(EntityType<?> entityType);
        
        @NotNull
        Mutable setPlayerSpawnFriendly(boolean friendly);
    }
}
