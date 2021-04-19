package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.DelegateAccessPoint;
import me.shedaniel.architectury.impl.access.ChunkAccessPointImpl;

public interface ChunkAccessPoint<T, SELF extends ChunkAccessPoint<T, SELF>> extends DelegateAccessPoint<ChunkAccess<T>, SELF>, ChunkAccess<T> {
    static <T> ChunkAccessPoint<T, ?> create() {
        return new ChunkAccessPointImpl<>();
    }
}
