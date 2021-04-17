package me.shedaniel.architectury.core.access;

import me.shedaniel.architectury.core.access.specific.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

@FunctionalInterface
public interface Access<T> {
    /**
     * Returns {@link T} by providers.
     *
     * @return the single provider compiled from providers.
     */
    T get();
    
    default Access<T> filter(BooleanSupplier predicate) {
        return () -> {
            if (predicate.getAsBoolean()) {
                return this.get();
            }
            return null;
        };
    }
}
