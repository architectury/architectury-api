package me.shedaniel.architectury.core.access.specific;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.Predicate;

@FunctionalInterface
public interface ChunkAccess<T> {
    default T getByChunk(Level level, BlockPos pos) {
        return getByChunk(level.getChunkAt(pos));
    }
    
    default T getByChunk(Level level, int chunkX, int chunkY) {
        return getByChunk(level.getChunk(chunkX, chunkY));
    }
    
    T getByChunk(LevelChunk chunk);
    
    default ChunkAccess<T> filterChunk(Predicate<LevelChunk> predicate) {
        return chunk -> {
            if (predicate.test(chunk)) {
                return this.getByChunk(chunk);
            }
            return null;
        };
    }
}
