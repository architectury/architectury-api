package me.shedaniel.architectury.impl;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.LevelAccessPoint;
import me.shedaniel.architectury.core.access.specific.LevelAccess;
import net.minecraft.world.level.Level;

public class LevelAccessPointImpl<T, SELF extends LevelAccessPointImpl<T, SELF>> implements LevelAccessPoint<T, SELF> {
    private final AccessPoint<LevelAccess<T>, ?> parent;
    
    public LevelAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> level -> {
            return processIterable(ts, level);
        });
    }
    
    private T processIterable(Iterable<LevelAccess<T>> iterable, Level level) {
        for (LevelAccess<T> accessor : iterable) {
            T t = accessor.getByLevel(level);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<LevelAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByLevel(Level level) {
        return get().getByLevel(level);
    }
}
