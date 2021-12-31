/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
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

package dev.architectury.event.events.common;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface InteractionEvent {
    /**
     * @see LeftClickBlock#click(Player, InteractionHand, BlockPos, Direction)
     */
    Event<LeftClickBlock> LEFT_CLICK_BLOCK = EventFactory.createEventResult();
    /**
     * @see RightClickBlock#click(Player, InteractionHand, BlockPos, Direction)
     */
    Event<RightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createEventResult();
    /**
     * @see RightClickItem#click(Player, InteractionHand)
     */
    Event<RightClickItem> RIGHT_CLICK_ITEM = EventFactory.createCompoundEventResult();
    /**
     * @see ClientLeftClickAir#click(Player, InteractionHand)
     */
    Event<ClientLeftClickAir> CLIENT_LEFT_CLICK_AIR = EventFactory.createLoop();
    /**
     * @see ClientRightClickAir#click(Player, InteractionHand)
     */
    Event<ClientRightClickAir> CLIENT_RIGHT_CLICK_AIR = EventFactory.createLoop();
    /**
     * @see InteractEntity#interact(Player, Entity, InteractionHand)
     */
    Event<InteractEntity> INTERACT_ENTITY = EventFactory.createEventResult();
    /**
     * @see FarmlandTrample#trample(Level, BlockPos, BlockState, float, Entity)
     */
    Event<FarmlandTrample> FARMLAND_TRAMPLE = EventFactory.createEventResult();
    
    interface RightClickBlock {
        /**
         * Invoked whenever a player right clicks a block.
         * Equivalent to Forge's {@code PlayerInteractEvent.RightClickBlock} event and Fabric's {@code UseBlockCallback}.
         *
         * @param player The player right clicking the block.
         * @param hand   The hand that is used.
         * @param pos    The position of the block in the level.
         * @param face   The face of the block clicked.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        EventResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface LeftClickBlock {
        /**
         * Invoked whenever a player left clicks a block.
         * Equivalent to Forge's {@code PlayerInteractEvent.LeftClickBlock} event and Fabric's {@code AttackBlockCallback}.
         *
         * @param player The player left clicking the block.
         * @param hand   The hand that is used.
         * @param pos    The position of the block in the level. Use {@link Player#getCommandSenderWorld()} to get the level.
         * @param face   The face of the block clicked.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        EventResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface RightClickItem {
        /**
         * Invoked whenever a player uses an item on a block.
         * Equivalent to Forge's {@code PlayerInteractEvent.RightClickItem} event and Fabric's {@code UseItemCallback}.
         *
         * @param player The player right clicking the block.
         * @param hand   The hand that is used.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        CompoundEventResult<ItemStack> click(Player player, InteractionHand hand);
    }
    
    interface ClientRightClickAir {
        /**
         * Invoked whenever a player right clicks the air.
         * This only occurs on the client.
         * Equivalent to Forge's {@code PlayerInteractEvent.RightClickEmpty} event.
         *
         * @param player The player. Always {@link net.minecraft.client.player.LocalPlayer}
         * @param hand   The hand used.
         */
        void click(Player player, InteractionHand hand);
    }
    
    interface ClientLeftClickAir {
        /**
         * Invoked whenever a player left clicks the air.
         * This only occurs on the client.
         * Equivalent to Forge's {@code PlayerInteractEvent.LeftClickEmpty} event.
         *
         * @param player The player. Always {@link net.minecraft.client.player.LocalPlayer}
         * @param hand   The hand used.
         */
        void click(Player player, InteractionHand hand);
    }
    
    interface InteractEntity {
        /**
         * Invoked whenever a player right clicks an entity.
         * Equivalent to Forge's {@code PlayerInteractEvent.EntityInteract} event.
         *
         * @param player The player clicking the entity.
         * @param entity Then entity the player clicks.
         * @param hand   The used hand.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        EventResult interact(Player player, Entity entity, InteractionHand hand);
    }
    
    interface FarmlandTrample {
        /**
         * Invoked when an entity attempts to trample farmland.
         * Equivalent to Forge's {@code BlockEvent.FarmlandTrampleEvent} event.
         *
         * @param world    The level where the block and the player are located in.
         * @param pos      The position of the block.
         * @param state    The state of the block.
         * @param distance The distance of the player to the block.
         * @param entity   The entity trampling.
         * @return A {@link EventResult} determining the outcome of the event,
         * the action may be cancelled by the result.
         */
        EventResult trample(Level world, BlockPos pos, BlockState state, float distance, Entity entity);
    }
}
