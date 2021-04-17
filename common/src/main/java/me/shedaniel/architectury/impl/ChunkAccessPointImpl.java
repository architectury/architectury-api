package me.shedaniel.architectury.impl;

import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.ChunkAccessPoint;
import me.shedaniel.architectury.core.access.specific.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkAccessPointImpl<T, SELF extends ChunkAccessPointImpl<T, SELF>> implements ChunkAccessPoint<T, SELF> {
    private final AccessPoint<ChunkAccess<T>, ?> parent;
    
    public ChunkAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> chunk -> {
            return processIterable(ts, chunk);
        });
    }
    
    private T processIterable(Iterable<ChunkAccess<T>> iterable, LevelChunk chunk) {
        for (ChunkAccess<T> accessor : iterable) {
            T t = accessor.getByChunk(chunk);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<ChunkAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByChunk(LevelChunk chunk) {
        return get().getByChunk(chunk);
    }
}
