package dev.architectury.registry.level.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.function.Supplier;

public final class SpawnPlacementsRegistry {
    
    /**
     * Registers default attributes to entities.
     *
     * @param entityType      the type of entity
     * @param spawnPlacement  the type of spawn placement
     * @param heightmapType   the type of heightmap
     * @param spawnPredicate  the spawn predicate
     * @see net.minecraft.world.entity.SpawnPlacements
     */
    @ExpectPlatform
    public static <T extends Mob> void register(Supplier<? extends EntityType<T>> entityType, SpawnPlacements.Type spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        throw new AssertionError();
    }
}
