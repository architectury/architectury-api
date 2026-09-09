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
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events related to loot tables and loot generation.
 */
public interface LootEvent {
    /**
     * An event to modify loot tables when they are loaded.
     * This can be used to add new drops via new loot pools to existing loot tables
     * without replacing the entire table.
     *
     * <h2>Built-in loot tables</h2>
     * <p>{@linkplain ModifyLootTable The event interface} includes a {@code builtin} parameter.
     * If it's {@code true}, the loot table is built-in to vanilla or a mod.
     * Otherwise, it's from a user data pack. The parameter can be used to only modify built-in loot tables
     * and let user-provided loot tables act as full "overwrites".
     *
     * <p>On NeoForge, {@code builtin} is always {@code true}: the event runs for every loot table,
     * but {@code LootTableLoadEvent} does not expose where the table came from, so data pack tables
     * cannot be told apart from built-in ones. Only Fabric reports the real value.
     *
     * <h2>Example: adding diamonds as a drop for dirt</h2>
     * <pre>{@code
     * LootEvent.MODIFY_LOOT_TABLE.register((registries, key, context, builtin) -> {
     *     // Check that the loot table is dirt and built-in
     *     if (builtin && Blocks.DIRT.getLootTable().equals(Optional.ofNullable(key))) {
     *         // Create a loot pool with a single item entry of Items.DIAMOND
     *         LootPool.Builder pool = LootPool.lootPool().add(LootItem.lootTableItem(Items.DIAMOND));
     *         context.addPool(pool);
     *     }
     * });
     * }</pre>
     *
     * @see ModifyLootTable#modifyLootTable(HolderLookup.Provider, ResourceKey, LootTableModificationContext, boolean)
     */
    Event<ModifyLootTable> MODIFY_LOOT_TABLE = EventFactory.createLoop();

    /**
     * An event to replace loot tables outright as they are loaded.
     *
     * <p>Unlike {@link #MODIFY_LOOT_TABLE}, which appends pools to the existing table, this event
     * swaps the whole table for a different one. Interrupt the result with the replacement table to
     * take effect; the first listener to interrupt wins and later listeners still see the original.
     *
     * <p>Equivalent to NeoForge's {@code LootTableLoadEvent#setTable} and
     * Fabric's {@code LootTableEvents#REPLACE}.
     *
     * @see ReplaceLootTable#replaceLootTable(HolderLookup.Provider, ResourceKey, LootTable)
     */
    Event<ReplaceLootTable> REPLACE_LOOT_TABLE = EventFactory.createCompoundEventResult();

    @FunctionalInterface
    interface ReplaceLootTable {
        /**
         * Replaces a loot table.
         *
         * @param registries the registries provider
         * @param key        the loot table key
         * @param original   the loot table that would otherwise be used
         * @return a {@link CompoundEventResult} carrying the replacement table,
         * or {@link CompoundEventResult#pass()} to leave the table alone
         */
        CompoundEventResult<LootTable> replaceLootTable(HolderLookup.Provider registries, ResourceKey<LootTable> key, LootTable original);
    }
    
    @FunctionalInterface
    interface ModifyLootTable {
        /**
         * Modifies a loot table.
         *
         * @param key     the loot table key
         * @param context the context used to modify the loot table
         * @param builtin if {@code true}, the loot table is built-in;
         *                if {@code false}, it is from a user data pack
         * @deprecated Use {@link #modifyLootTable(HolderLookup.Provider, ResourceKey, LootTableModificationContext, boolean)} instead.
         */
        @Deprecated(forRemoval = true)
        void modifyLootTable(ResourceKey<LootTable> key, LootTableModificationContext context, boolean builtin);

        /**
         * Modifies a loot table.
         *
         * @param registries the registries provider
         * @param key        the loot table key
         * @param context    the context used to modify the loot table
         * @param builtin    if {@code true}, the loot table is built-in;
         *                   if {@code false}, it is from a user data pack
         */
        default void modifyLootTable(HolderLookup.Provider registries, ResourceKey<LootTable> key, LootTableModificationContext context, boolean builtin) {
            modifyLootTable(key, context, builtin);
        }
    }
    
    /**
     * A platform-specific bridge for modifying a specific loot table.
     */
    @ApiStatus.NonExtendable
    interface LootTableModificationContext {
        /**
         * Adds a pool to the loot table.
         *
         * @param pool the pool to add
         */
        void addPool(LootPool.Builder pool);
    }
}
