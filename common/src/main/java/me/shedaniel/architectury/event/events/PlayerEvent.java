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

import me.shedaniel.architectury.event.CompoundEventResult;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import me.shedaniel.architectury.utils.IntValue;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public interface PlayerEvent {
    Event<PlayerJoin> PLAYER_JOIN = EventFactory.createLoop();
    Event<PlayerQuit> PLAYER_QUIT = EventFactory.createLoop();
    Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop();
    Event<PlayerAdvancement> PLAYER_ADVANCEMENT = EventFactory.createLoop();
    Event<PlayerClone> PLAYER_CLONE = EventFactory.createLoop();
    Event<CraftItem> CRAFT_ITEM = EventFactory.createLoop();
    Event<SmeltItem> SMELT_ITEM = EventFactory.createLoop();
    Event<PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createInteractionResult();
    Event<PickupItem> PICKUP_ITEM_POST = EventFactory.createLoop();
    Event<ChangeDimension> CHANGE_DIMENSION = EventFactory.createLoop();
    Event<DropItem> DROP_ITEM = EventFactory.createLoop();
    Event<OpenMenu> OPEN_MENU = EventFactory.createLoop();
    Event<CloseMenu> CLOSE_MENU = EventFactory.createLoop();
    /**
     * Invoked when a player attempts to fill a bucket using right-click.
     * You can return a non-PASS interaction result to cancel further processing by other mods.
     * <p>
     * On Forge, FAIL cancels the event, and SUCCESS sets the event as handled.
     * On Fabric, any non-PASS result is returned directly and immediately.
     */
    Event<FillBucket> FILL_BUCKET = EventFactory.createCompoundEventResult();
    
    /**
     * @deprecated use {@link BlockEvent#BREAK}
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0")
    Event<BreakBlock> BREAK_BLOCK = EventFactory.createInteractionResult();
    
    interface PlayerJoin {
        /**
         * Invoked after a player joined a server level.
         * This is fired on the server side only.
         * Equal to the forge {@code PlayerLoggedInEvent} event.
         *
         * @param player The joined player.
         */
        void join(ServerPlayer player);
    }
    
    interface PlayerQuit {
        /**
         * Invoked after a player logged out of a server level.
         * This event is fired server side only.
         * Equal to the forge {@code PlayerLoggedOutEvent} event.
         *
         * @param player The now logged out player.
         */
        void quit(ServerPlayer player);
    }
    
    interface PlayerRespawn {
        /**
         * Invoked when a player is respawned (e.g. changing dimension).
         * This event is fired server side only.
         * Equal to the forge {@code PlayerRespawnEvent} event.
         * To manipulate the player use {@link PlayerClone#clone(ServerPlayer, ServerPlayer, boolean)}.
         *
         * @param newPlayer The respawned player.
         * @param conqueredEnd Has the player conquered the end. This is true when the player joined the end and now is leaving it. {@link ServerPlayer#wonGame}
         */
        void respawn(ServerPlayer newPlayer, boolean conqueredEnd);
    }
    
    interface PlayerClone {
        /**
         * Invoked when a player respawns.
         * This can be used to manipulate the new player.
         * This event is fired server side only.
         * Equal to the forge {@code PlayerEvent.Clone} event.
         *
         * @param oldPlayer The old player.
         * @param newPlayer The new player.
         * @param wonGame This is true when the player joined the end and now is leaving it. {@link ServerPlayer#wonGame}
         */
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame);
    }
    
    interface PlayerAdvancement {
        void award(ServerPlayer player, Advancement advancement);
    }
    
    interface CraftItem {
        void craft(Player player, ItemStack constructed, Container inventory);
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
    
    interface ChangeDimension {
        void change(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel);
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
    
    interface FillBucket {
        CompoundEventResult<ItemStack> fill(Player player, Level level, ItemStack stack, @Nullable HitResult target);
    }
}
