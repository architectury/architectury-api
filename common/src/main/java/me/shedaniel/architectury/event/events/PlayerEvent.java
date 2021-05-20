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
    /**
     * @see PlayerJoin#join(ServerPlayer)
     */
    Event<PlayerJoin> PLAYER_JOIN = EventFactory.createLoop();
    /**
     * @see PlayerQuit#quit(ServerPlayer)
     */
    Event<PlayerQuit> PLAYER_QUIT = EventFactory.createLoop();
    /**
     * @see PlayerRespawn#respawn(ServerPlayer, boolean)
     */
    Event<PlayerRespawn> PLAYER_RESPAWN = EventFactory.createLoop();
    /**
     * @see PlayerAdvancement#award(ServerPlayer, Advancement)
     */
    Event<PlayerAdvancement> PLAYER_ADVANCEMENT = EventFactory.createLoop();
    /**
     * @see PlayerClone#clone(ServerPlayer, ServerPlayer, boolean)
     */
    Event<PlayerClone> PLAYER_CLONE = EventFactory.createLoop();
    /**
     * @see CraftItem#craft(Player, ItemStack, Container)
     */
    Event<CraftItem> CRAFT_ITEM = EventFactory.createLoop();
    /**
     * @see SmeltItem#smelt(Player, ItemStack)
     */
    Event<SmeltItem> SMELT_ITEM = EventFactory.createLoop();
    /**
     * @see PickupItemPredicate#canPickup(Player, ItemEntity, ItemStack)
     */
    Event<PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createInteractionResult();
    /**
     * @see PickupItem#pickup(Player, ItemEntity, ItemStack)
     */
    Event<PickupItem> PICKUP_ITEM_POST = EventFactory.createLoop();
    /**
     * @see ChangeDimension#change(ServerPlayer, ResourceKey, ResourceKey)
     */
    Event<ChangeDimension> CHANGE_DIMENSION = EventFactory.createLoop();
    /**
     * @see DropItem#drop(Player, ItemEntity)
     */
    Event<DropItem> DROP_ITEM = EventFactory.createLoop();
    /**
     * @see OpenMenu#open(Player, AbstractContainerMenu)
     */
    Event<OpenMenu> OPEN_MENU = EventFactory.createLoop();
    /**
     * @see CloseMenu#close(Player, AbstractContainerMenu)
     */
    Event<CloseMenu> CLOSE_MENU = EventFactory.createLoop();
    /**
     * @see FillBucket#fill(Player, Level, ItemStack, HitResult)
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
        /**
         * Invoked when a player gets an advancement.
         * This event is fired server side only.
         * Equal to the forge {@code AdvancementEvent} event.
         *
         * @param player The player who got the advancement.
         * @param advancement THe advancement the player got.
         */
        void award(ServerPlayer player, Advancement advancement);
    }
    
    interface CraftItem {
        /**
         * Invoked when a player crafts an item.
         * Equal to the forge {@code ItemCraftedEvent} event.
         * This only applies for the vanilla crafting table (or any crafting table using the {@link net.minecraft.world.inventory.ResultSlot}) and
         * the player inventory crafting grid.
         * This is fired when the player takes something out of the result slot.
         *
         * @param player The player.
         * @param constructed The ItemStack that has been crafted.
         * @param inventory The inventory of the constructor.
         */
        void craft(Player player, ItemStack constructed, Container inventory);
    }
    
    interface SmeltItem {
        /**
         * Invoked when a player smelts an item.
         * Equal to the forge {@code ItemSmeltedEvent} event.
         * This is fired when the player takes the stack out of the output slot.
         *
         * @param player The player.
         * @param smelted The smelt result.
         */
        void smelt(Player player, ItemStack smelted);
    }
    
    interface PickupItemPredicate {
        /**
         * Invoked when a player tries to pickup a item entity.
         * Equal to the forge {@code EntityItemPickupEvent} event.
         *
         * @param player The player picking up.
         * @param entity The {@link ItemEntity} that the player tries to pick up.
         * @param stack The content of the {@link ItemEntity}.
         * @return Forge ignores the return value. On Fabric a {@link InteractionResult#FAIL} results in the event getting canceled and the item not being picked up.
         */
        InteractionResult canPickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface PickupItem {
        /**
         * Invoked when a player has picked up an {@link ItemEntity}.
         * Equal to the forge {@code ItemPickupEvent} event.
         *
         * @param player The player.
         * @param entity The {@link ItemEntity} that the player picked up.
         * @param stack The content of the {@link ItemEntity}.
         */
        void pickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface ChangeDimension {
        /**
         * Invoked when a player changes their dimension.
         * Equal to the forge {@code PlayerChangedDimensionEvent} event.
         *
         * @param player The teleporting player.
         * @param oldLevel The level the player comes from.
         * @param newLevel The level the player teleports into.
         */
        void change(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel);
    }
    
    interface DropItem {
        /**
         * Invoked when a player drops an item.
         * Equal to the forge {@code ItemTossEvent} event.
         *
         * @param player The player dropping something.
         * @param entity The entity that has spawned when the player dropped a ItemStack.
         * @return Forge ignores the return value. On Fabric a {@link InteractionResult#FAIL} results in the event getting canceled and the item not being dropped.
         */
        InteractionResult drop(Player player, ItemEntity entity);
    }
    
    interface BreakBlock {
        /**
         * Called by and equal to {@link BlockEvent#BREAK}.
         */
        InteractionResult breakBlock(Level world, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp);
    }
    
    interface OpenMenu {
        /**
         * Invoked when a player opens a menu.
         * Equal to the forge {@code PlayerContainerEvent.Open} event.
         *
         * If you want to prevent a certain menu to be opened, you have to catch the {@link me.shedaniel.architectury.event.events.GuiEvent#SET_SCREEN} event and open your own.
         *
         * @param player The player opening the menu.
         * @param menu The menu that is opened.
         */
        void open(Player player, AbstractContainerMenu menu);
    }
    
    interface CloseMenu {
        /**
         * Invoked when a player closes a menu.
         * Equal to the forge {@code PlayerContainerEvent.Close} event.
         **
         * @param player The player closing the menu.
         * @param menu The menu that is closed.
         */
        void close(Player player, AbstractContainerMenu menu);
    }
    
    interface FillBucket {
        /**
         * Invoked when a player attempts to fill a bucket using right-click.
         * You can return a non-PASS interaction result to cancel further processing by other mods.
         *
         * @param player The player filling the bucket.
         * @param level The level the player is in.
         * @param stack The bucket stack.
         * @param target The target which the player has aimed at.
         * @return On Forge, FAIL cancels the event, and SUCCESS sets the event as handled. On Fabric, any non-PASS result is returned directly and immediately.
         */
        CompoundEventResult<ItemStack> fill(Player player, Level level, ItemStack stack, @Nullable HitResult target);
    }
}
