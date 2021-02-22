/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
