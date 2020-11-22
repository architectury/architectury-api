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
import me.shedaniel.architectury.utils.IntValue;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface PlayerEvent {
    Event<PlayerJoin> PLAYER_JOIN = EventFactory.createLoop(PlayerJoin.class);
    Event<PlayerQuit> PLAYER_QUIT = EventFactory.createLoop(PlayerQuit.class);
    Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop(PlayerRespawn.class);
    Event<PlayerAdvancement> PLAYER_ADVANCEMENT = EventFactory.createLoop(PlayerAdvancement.class);
    Event<PlayerClone> PLAYER_CLONE = EventFactory.createLoop(PlayerClone.class);
    Event<CraftItem> CRAFT_ITEM = EventFactory.createLoop(CraftItem.class);
    Event<SmeltItem> SMELT_ITEM = EventFactory.createLoop(SmeltItem.class);
    Event<PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createInteractionResult(PickupItemPredicate.class);
    Event<PickupItem> PICKUP_ITEM_POST = EventFactory.createLoop(PickupItem.class);
    Event<DropItem> DROP_ITEM = EventFactory.createLoop(DropItem.class);
    Event<OpenMenu> OPEN_MENU = EventFactory.createLoop(OpenMenu.class);
    Event<CloseMenu> CLOSE_MENU = EventFactory.createLoop(CloseMenu.class);
    Event<BreakBlock> BREAK_BLOCK = EventFactory.createInteractionResult(BreakBlock.class);
    
    interface PlayerJoin {
        void join(ServerPlayer player);
    }
    
    interface PlayerQuit {
        void quit(ServerPlayer player);
    }
    
    interface PlayerRespawn {
        void respawn(ServerPlayer newPlayer, boolean conqueredEnd);
    }
    
    interface PlayerClone {
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame);
    }
    
    interface PlayerAdvancement {
        void award(ServerPlayer player, Advancement advancement);
    }
    
    interface CraftItem {
        void craft(Player player, ItemStack smelted, Container inventory);
    }
    
    interface SmeltItem {
        void smelt(Player player, ItemStack smelted);
    }
    
    interface PickupItemPredicate {
        InteractionResult canPickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface PickupItem {
        void pickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface DropItem {
        InteractionResult drop(Player player, ItemEntity entity);
    }
    
    interface BreakBlock {
        InteractionResult breakBlock(Level world, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp);
    }
    
    interface OpenMenu {
        void open(Player player, AbstractContainerMenu menu);
    }
    
    interface CloseMenu {
        void close(Player player, AbstractContainerMenu menu);
    }
}
