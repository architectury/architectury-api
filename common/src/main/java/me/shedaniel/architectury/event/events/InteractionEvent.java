/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface InteractionEvent {
    Event<LeftClickBlock> LEFT_CLICK_BLOCK = EventFactory.createInteractionResult(LeftClickBlock.class);
    Event<RightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createInteractionResult(RightClickBlock.class);
    Event<RightClickItem> RIGHT_CLICK_ITEM = EventFactory.createInteractionResultHolder(RightClickItem.class);
    Event<ClientLeftClickAir> CLIENT_LEFT_CLICK_AIR = EventFactory.createLoop(ClientLeftClickAir.class);
    Event<ClientRightClickAir> CLIENT_RIGHT_CLICK_AIR = EventFactory.createLoop(ClientRightClickAir.class);
    Event<InteractEntity> INTERACT_ENTITY = EventFactory.createInteractionResult(InteractEntity.class);
    
    interface RightClickBlock {
        InteractionResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface LeftClickBlock {
        InteractionResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface RightClickItem {
        InteractionResultHolder<ItemStack> click(Player player, InteractionHand hand);
    }
    
    interface ClientRightClickAir {
        void click(Player player, InteractionHand hand);
    }
    
    interface ClientLeftClickAir {
        void click(Player player, InteractionHand hand);
    }
    
    interface InteractEntity {
        InteractionResult interact(Player player, Entity entity, InteractionHand hand);
    }
    
    interface BlockBreak {
        InteractionResult breakBlock(Player player, BlockPos pos, BlockState state);
    }
}
