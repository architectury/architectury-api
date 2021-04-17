package me.shedaniel.architectury.core.access.specific;

import net.minecraft.world.level.Level;

import java.util.function.Predicate;

@FunctionalInterface
public interface LevelAccess<T> {
    T getByLevel(Level level);
    
    default LevelAccess<T> filterLevel(Predicate<Level> predicate) {
        return level -> {
            if (predicate.test(level)) {
                return this.getByLevel(level);
            }
            return null;
        };
    }
}
