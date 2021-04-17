package me.shedaniel.architectury.core.access.combined;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.*;
import me.shedaniel.architectury.impl.CombinedAccessImpl;

import java.util.Collection;
import java.util.function.Function;

public interface CombinedAccess<T> extends ProvidedAccess<T>, BlockAccess<T>, LevelAccess<T>, ChunkAccess<T>,
        ItemAccess<T>, EntityAccess<T> {
    static <T> CombinedAccess<T> create(Function<Collection<T>, T> compiler) {
        return new CombinedAccessImpl<>(compiler);
    }
    
    AccessPoint<T, ?> general();
    
    BlockAccessPoint<T, ?> block();
    
    ChunkAccessPoint<T, ?> chunk();
    
    LevelAccessPoint<T, ?> level();
    
    EntityAccessPoint<T, ?> entity();
    
    ItemAccessPoint<T, ?> item();
}
