package me.shedaniel.architectury.core.access.specific;

import me.shedaniel.architectury.core.access.specific.filter.BlockAccessPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockAccess<T> {
    default T getByBlock(Level level, BlockPos pos) {
        return getByBlock(level, pos, level.getBlockState(pos));
    }
    
    default T getByBlock(Level level, BlockPos pos, BlockState state) {
        return getByBlock(level, level.getChunkAt(pos), pos, state, level.getBlockEntity(pos), null);
    }
    
    default T getByBlock(Level level, LevelChunk chunk, BlockPos pos) {
        return getByBlock(level, chunk, pos, level.getBlockState(pos), null);
    }
    
    default T getByBlock(Level level, BlockPos pos, @Nullable Direction direction) {
        return getByBlock(level, level.getChunkAt(pos), pos, direction);
    }
    
    default T getByBlock(Level level, LevelChunk chunk, BlockPos pos, @Nullable Direction direction) {
        return getByBlock(level, chunk, pos, level.getBlockState(pos), direction);
    }
    
    default T getByBlock(Level level, BlockPos pos, BlockState state, @Nullable Direction direction) {
        return getByBlock(level, level.getChunkAt(pos), pos, state, direction);
    }
    
    default T getByBlock(Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable Direction direction) {
        return getByBlock(level, chunk, pos, state, level.getBlockEntity(pos), direction);
    }
    
    T getByBlock(Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
    
    default BlockAccess<T> filterBlock(BlockAccessPredicate predicate) {
        return (level, chunk, pos, state, entity, direction) -> {
            if (predicate.test(level, chunk, pos, state, entity, direction)) {
                return this.getByBlock(level, chunk, pos, state, entity, direction);
            }
            return null;
        };
    }
}
