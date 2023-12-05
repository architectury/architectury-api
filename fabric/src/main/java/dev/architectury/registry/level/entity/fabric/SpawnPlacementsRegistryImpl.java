package dev.architectury.registry.level.entity.fabric;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public class SpawnPlacementsRegistryImpl {
    public static <T extends Mob> void register(Supplier<? extends EntityType<T>> entityType, SpawnPlacements.Type spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        SpawnPlacements.register(entityType.get(), spawnPlacement, heightmapType, spawnPredicate);
    }
}
