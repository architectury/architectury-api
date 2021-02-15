package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import me.shedaniel.architectury.utils.IntValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface BlockEvent {
    
    // Block interaction events
    /**
     * Called when a player breaks a block.
     */
    Event<Break> BREAK = EventFactory.createInteractionResult();
    /**
     * Called when a block is placed in the world by an entity.
     */
    Event<Place> PLACE = EventFactory.createInteractionResult();
    
    /**
     * Called when a falling block (sand, anvil, etc.) is about to land.
     * Use fallState#getBlock to get the type of block for more granular control.
     */
    Event<FallingLand> FALLING_LAND = EventFactory.createLoop();
    
    interface Break {
        InteractionResult breakBlock(Level world, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp);
    }
    
    interface Place {
        InteractionResult placeBlock(Level world, BlockPos pos, BlockState state, @Nullable Entity placer);
    }
    
    interface FallingLand {
        void onLand(Level level, BlockPos pos, BlockState fallState, BlockState landOn, FallingBlockEntity entity);
    }
}
