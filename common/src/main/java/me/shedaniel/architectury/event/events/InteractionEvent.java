/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
