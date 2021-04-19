package me.shedaniel.architectury.core.access.combined;

import me.shedaniel.architectury.core.access.specific.*;
import me.shedaniel.architectury.impl.access.CombinedAccessImpl;

public interface CombinedAccess<T> extends ProvidedAccess<T>, BlockAccess<T>, LevelAccess<T>, ChunkAccess<T>,
        ItemAccess<T>, EntityAccess<T> {
    static <T> CombinedAccess<T> create() {
        return new CombinedAccessImpl<>();
    }
    
    ProvidedAccessPoint<T, ?> general();
    
    BlockAccessPoint<T, ?> block();
    
    ChunkAccessPoint<T, ?> chunk();
    
    LevelAccessPoint<T, ?> level();
    
    EntityAccessPoint<T, ?> entity();
    
    ItemAccessPoint<T, ?> item();
}
