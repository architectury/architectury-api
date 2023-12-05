package dev.architectury.registry.level.entity.forge;

import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpawnPlacementRegistryImpl {
    private static final List<EntityType> entityTypes = new ArrayList<>();
    private static final List<SpawnPlacements.Type> spawnPlacements = new ArrayList<>();
    private static final List<Heightmap.Types> heightmapTypes = new ArrayList<>();
    private static final List<SpawnPlacements.SpawnPredicate> spawnPredicates = new ArrayList<>();
    
    public static <T extends Mob>  void register(Supplier<? extends EntityType<T>> entityType, SpawnPlacements.Type spawnPlacement, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> spawnPredicate) {
        entityTypes.add(entityType.get());
        spawnPlacements.add(spawnPlacement);
        heightmapTypes.add(heightmapType);
        spawnPredicates.add(spawnPredicate);
    }
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus ->{
            bus.register(SpawnPlacementRegistryImpl.class);
        });
    }
    
    @SubscribeEvent
    public static void registerEntityAttribute(SpawnPlacementRegisterEvent event){
        for (int i = 0; i < entityTypes.size(); i++) {
            event.register(
                    entityTypes.get(i),
                    spawnPlacements.get(i),
                    heightmapTypes.get(i),
                    spawnPredicates.get(i),
                    SpawnPlacementRegisterEvent.Operation.OR
            );
        }
    }
}
