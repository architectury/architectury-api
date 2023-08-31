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
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
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
     * @see PlayerAdvancement#award(ServerPlayer, AdvancementHolder)
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
    Event<PickupItemPredicate> PICKUP_ITEM_PRE = EventFactory.createEventResult();
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
    Event<DropItem> DROP_ITEM = EventFactory.createEventResult();
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
     * @see AttackEntity#attack(Player, Level, Entity, InteractionHand, EntityHitResult)
     */
    Event<AttackEntity> ATTACK_ENTITY = EventFactory.createEventResult();
    
    interface PlayerJoin {
        /**
         * Invoked after a player joined a server level.
         * Equivalent to Forge's {@code PlayerLoggedInEvent} event.
         *
         * @param player The joined player.
         */
        void join(ServerPlayer player);
    }
    
    interface PlayerQuit {
        /**
         * Invoked after a player logged out of a server level.
         * Equivalent to Forge's {@code PlayerLoggedOutEvent} event.
         *
         * @param player The now logged out player.
         */
        void quit(ServerPlayer player);
    }
    
    interface PlayerRespawn {
        /**
         * Invoked when a player is respawned (e.g. changing dimension).
         * Equivalent to Forge's {@code PlayerRespawnEvent} event.
         * To manipulate the player use {@link PlayerClone#clone(ServerPlayer, ServerPlayer, boolean)}.
         *
         * @param newPlayer    The respawned player.
         * @param conqueredEnd Whether the player has conquered the end. This is true when the player joined the end and now is leaving it. {@link ServerPlayer#wonGame}
         */
        void respawn(ServerPlayer newPlayer, boolean conqueredEnd);
    }
    
    interface PlayerClone {
        /**
         * Invoked when a player respawns.
         * This can be used to manipulate the new player.
         * Equivalent to Forge's {@code PlayerEvent.Clone} event.
         *
         * @param oldPlayer The old player.
         * @param newPlayer The new player.
         * @param wonGame   This is true when the player joined the end and now is leaving it. {@link ServerPlayer#wonGame}
         */
        void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame);
    }
    
    interface PlayerAdvancement {
        /**
         * Invoked when a player gets an advancement.
         * Equivalent to Forge's {@code AdvancementEvent} event.
         *
         * @param player      The player who got the advancement.
         * @param advancement The advancement the player got.
         */
        void award(ServerPlayer player, AdvancementHolder advancement);
    }
    
    interface CraftItem {
        /**
         * Invoked when a player crafts an item.
         * Equivalent to Forge's {@code ItemCraftedEvent} event.
         * This only applies for the vanilla crafting table (or any crafting table using the {@link net.minecraft.world.inventory.ResultSlot}) and
         * the player inventory crafting grid.
         * This is invoked when the player takes something out of the result slot.
         *
         * @param player      The player.
         * @param constructed The ItemStack that has been crafted.
         * @param inventory   The inventory of the constructor.
         */
        void craft(Player player, ItemStack constructed, Container inventory);
    }
    
    interface SmeltItem {
        /**
         * Invoked when a player smelts an item.
         * Equivalent to Forge's {@code ItemSmeltedEvent} event.
         * This is invoked when the player takes the stack out of the output slot.
         *
         * @param player  The player.
         * @param smelted The smelt result.
         */
        void smelt(Player player, ItemStack smelted);
    }
    
    interface PickupItemPredicate {
        /**
         * Invoked when a player tries to pickup an {@link ItemEntity}.
         * Equivalent to Forge's {@code EntityItemPickupEvent} event.
         *
         * @param player The player picking up.
         * @param entity The {@link ItemEntity} that the player tries to pick up.
         * @param stack  The content of the {@link ItemEntity}.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the pickup may be cancelled by the result.
         */
        EventResult canPickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface PickupItem {
        /**
         * Invoked when a player has picked up an {@link ItemEntity}.
         * Equivalent to Forge's {@code ItemPickupEvent} event.
         *
         * @param player The player.
         * @param entity The {@link ItemEntity} that the player picked up.
         * @param stack  The content of the {@link ItemEntity}.
         */
        void pickup(Player player, ItemEntity entity, ItemStack stack);
    }
    
    interface ChangeDimension {
        /**
         * Invoked when a player changes their dimension.
         * Equivalent to Forge's {@code PlayerChangedDimensionEvent} event.
         *
         * @param player   The teleporting player.
         * @param oldLevel The level the player comes from.
         * @param newLevel The level the player teleports into.
         */
        void change(ServerPlayer player, ResourceKey<Level> oldLevel, ResourceKey<Level> newLevel);
    }
    
    interface DropItem {
        /**
         * Invoked when a player drops an item.
         * Equivalent to Forge's {@code ItemTossEvent} event.
         *
         * @param player The player dropping something.
         * @param entity The entity that has spawned when the player dropped a ItemStack.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the drop may be cancelled by the result.
         */
        EventResult drop(Player player, ItemEntity entity);
    }
    
    interface OpenMenu {
        /**
         * Invoked when a player opens a menu.
         * Equivalent to Forge's {@code PlayerContainerEvent.Open} event.
         *
         * @param player The player opening the menu.
         * @param menu   The menu that is opened.
         */
        void open(Player player, AbstractContainerMenu menu);
    }
    
    interface CloseMenu {
        /**
         * Invoked when a player closes a menu.
         * Equivalent to Forge's {@code PlayerContainerEvent.Close} event.
         *
         * @param player The player closing the menu.
         * @param menu   The menu that is closed.
         */
        void close(Player player, AbstractContainerMenu menu);
    }
    
    interface FillBucket {
        /**
         * Invoked when a player attempts to fill a bucket using right-click.
         * You can return a non-PASS interaction result to cancel further processing by other mods.
         *
         * @param player The player filling the bucket.
         * @param level  The level the player is in.
         * @param stack  The bucket stack.
         * @param target The target which the player has aimed at.
         * @return A {@link CompoundEventResult} determining the outcome of the event.
         */
        CompoundEventResult<ItemStack> fill(Player player, Level level, ItemStack stack, @Nullable HitResult target);
    }
    
    interface AttackEntity {
        /**
         * Invoked when a player is about to attack an entity using left-click.
         * Equivalent to Forge's {@code AttackEntityEvent} and Fabric API's {@code AttackEntityCallback} events.
         *
         * @param player The player attacking the entity.
         * @param level  The level the player is in.
         * @param target The entity about to be attacked.
         * @param hand   The hand the player is using.
         * @param result The entity hit result.
         * @return An {@link EventResult} determining the outcome of the event,
         * the attack may be cancelled by the result.
         */
        EventResult attack(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result);
    }
}
