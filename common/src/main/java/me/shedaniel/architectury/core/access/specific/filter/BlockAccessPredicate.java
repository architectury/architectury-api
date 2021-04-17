package me.shedaniel.architectury.core.access.specific.filter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface BlockAccessPredicate {
    static BlockAccessPredicate alwaysTrue() {
        return (level, chunk, pos, state, entity, direction) -> true;
    }
    
    static BlockAccessPredicate alwaysFalse() {
        return (level, chunk, pos, state, entity, direction) -> false;
    }
    
    boolean test(Level level, LevelChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction);
    
    default BlockAccessPredicate and(BlockAccessPredicate other) {
        return (level, chunk, pos, state, entity, direction) -> {
            return test(level, chunk, pos, state, entity, direction) &&
                   other.test(level, chunk, pos, state, entity, direction);
        };
    }
    
    default BlockAccessPredicate or(BlockAccessPredicate other) {
        return (level, chunk, pos, state, entity, direction) -> {
            return test(level, chunk, pos, state, entity, direction) ||
                   other.test(level, chunk, pos, state, entity, direction);
        };
    }
}
