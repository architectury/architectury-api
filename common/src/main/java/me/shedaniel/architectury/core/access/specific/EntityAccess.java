package me.shedaniel.architectury.core.access.specific;

import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

@FunctionalInterface
public interface EntityAccess<T> {
    T getByEntity(Entity entity);
    
    default EntityAccess<T> filterEntity(Predicate<Entity> predicate) {
        return entity -> {
            if (predicate.test(entity)) {
                return this.getByEntity(entity);
            }
            return null;
        };
    }
}
