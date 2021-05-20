/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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
import me.shedaniel.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface InteractionEvent {
    /**
     * @see LeftClickBlock#click(Player, InteractionHand, BlockPos, Direction)
     */
    Event<LeftClickBlock> LEFT_CLICK_BLOCK = EventFactory.createInteractionResult();
    /**
     * @see RightClickBlock#click(Player, InteractionHand, BlockPos, Direction)
     */
    Event<RightClickBlock> RIGHT_CLICK_BLOCK = EventFactory.createInteractionResult();
    /**
     * @see RightClickItem#click(Player, InteractionHand)
     */
    Event<RightClickItem> RIGHT_CLICK_ITEM = EventFactory.createInteractionResultHolder();
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
    Event<InteractEntity> INTERACT_ENTITY = EventFactory.createInteractionResult();
    /**
     * @see FarmlandTrample#trample(Level, BlockPos, BlockState, float, Entity)
     */
    Event<FarmlandTrample> FARMLAND_TRAMPLE = EventFactory.createEventResult();
    
    interface RightClickBlock {
        /**
         * Invoked whenever a player right clicks a block.
         * Equal to the Forge {@code PlayerInteractEvent.RightClickBlock} event and Fabric's {@code UseBlockCallback}.
         * 
         * @param player The player right clicking the block.
         * @param hand The hand that is used.
         * @param pos The position of the block in the level. Use {@link Player#getCommandSenderWorld()} to get the level.
         * @param face The face of the block clicked.
         * @return The event is canceled if anything else than {@link InteractionResult#PASS} is returned.
         */
        InteractionResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface LeftClickBlock {
        /**
         * Invoked whenever a player left clicks a block.
         * Equal to the Forge {@code PlayerInteractEvent.LeftClickBlock} event and Fabric's {@code AttackBlockCallback}.
         *
         * @param player The player left clicking the block.
         * @param hand The hand that is used.
         * @param pos The position of the block in the level. Use {@link Player#getCommandSenderWorld()} to get the level.
         * @param face The face of the block clicked.
         * @return The event is canceled if anything else than {@link InteractionResult#PASS} is returned.
         */
        InteractionResult click(Player player, InteractionHand hand, BlockPos pos, Direction face);
    }
    
    interface RightClickItem {
        /**
         * Invoked whenever a player uses an item on a block.
         * Equal to the Forge {@code PlayerInteractEvent.RightClickItem} event and Fabric's {@code UseItemCallback}.
         *
         * @param player The player right clicking the block.
         * @param hand The hand that is used. You can get the {@link ItemStack} with {@link Player#getItemInHand(InteractionHand)}
         * @return Whenever the return is not {@link InteractionResult#PASS}, the result value is used and the event is canceled.
         */
        InteractionResultHolder<ItemStack> click(Player player, InteractionHand hand);
    }
    
    interface ClientRightClickAir {
        /**
         * Called when a player is right clicking the air.
         * This is only fired on the client.
         * Equal to the forge {@code PlayerInteractEvent.RightClickEmpty} event.
         * 
         * @param player The player. Always {@link net.minecraft.client.player.LocalPlayer}
         * @param hand The hand used.
         */
        void click(Player player, InteractionHand hand);
    }
    
    interface ClientLeftClickAir {
        /**
         * Called when a player is left clicking the air.
         * This is only fired on the client.
         * Equal to the forge {@code PlayerInteractEvent.LeftClickEmpty} event.
         *
         * @param player The player. Always {@link net.minecraft.client.player.LocalPlayer}
         * @param hand The hand used.
         */
        void click(Player player, InteractionHand hand);
    }
    
    interface InteractEntity {
        /**
         * Called when a player is right clicking on an entity.
         * Equal to the forge {@code PlayerInteractEvent.EntityInteract} event.
         * 
         * @param player The player clicking the entity.
         * @param entity Then entity the player clicks.
         * @param hand The used hand.
         * @return If the return value is not {@link InteractionResult#PASS}, event is cancelled and the used result is passed as return value.
         */
        InteractionResult interact(Player player, Entity entity, InteractionHand hand);
    }
    
    /**
     * @deprecated use {@link BlockEvent#BREAK}
     */
    @Deprecated
    interface BlockBreak {
        InteractionResult breakBlock(Player player, BlockPos pos, BlockState state);
    }
    
    interface FarmlandTrample {
        /**
         * Called when a entity is trampling farmland.
         * Equal to the forge {@code BlockEvent.FarmlandTrampleEvent} event.
         * 
         * @param world The level where the block and the player are located in.
         * @param pos The position of the block.
         * @param state The state of the block.
         * @param distance The distance of the player to the block.
         * @param entity The entity trampling.
         * @return If any value is returned, the trampling is canceled.
         */
        EventResult trample(Level world, BlockPos pos, BlockState state, float distance, Entity entity);
    }
}
