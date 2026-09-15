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
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;

/**
 * Events related to loot tables and loot generation.
 *
 * <h2>The {@code registries} parameter</h2>
 * <p>Both events hand listeners a {@link HolderGetter.Provider}, which resolves holders and tags
 * ({@code lookupOrThrow}, {@code get(ResourceKey)}, {@code get(TagKey)}) but cannot enumerate registries or
 * build a {@link net.minecraft.resources.RegistryOps}.
 *
 * <p>This was widened from {@link net.minecraft.core.HolderLookup.Provider} in 26.3. The narrower type is the
 * honest one rather than a convenience, because the loaders genuinely differ here:
 * <ul>
 * <li>Fabric's {@code LootTableEvents} passes a real {@link net.minecraft.core.HolderLookup.Provider}, so
 *     {@code instanceof} succeeds there and the extra methods are reachable.</li>
 * <li>NeoForge's loot loading passes {@code ops.lookupProvider::lookup} -- a method reference, so the object
 *     implements {@link HolderGetter.Provider} and nothing else. {@code instanceof HolderLookup.Provider} is
 *     <em>always</em> false there, and a cast always throws.</li>
 * </ul>
 *
 * <p>So do not write a listener that depends on recovering the wider type: it would work on Fabric and fail on
 * every NeoForge install. If you need a {@link net.minecraft.resources.RegistryOps} to deserialize loot
 * content, build it outside these events from a provider you own.
 */
public interface LootEvent {
    /**
     * An event to modify loot tables when they are loaded.
     * This can be used to add new drops via new loot pools to existing loot tables
     * without replacing the entire table.
     *
     * <h4>Built-in loot tables</h4>
     * <p>{@linkplain ModifyLootTable The event interface} includes a {@code builtin} parameter.
     * If it's {@code true}, the loot table is built-in to vanilla or a mod.
     * Otherwise, it's from a user data pack. The parameter can be used to only modify built-in loot tables
     * and let user-provided loot tables act as full "overwrites".
     *
     * <p>On NeoForge, {@code builtin} is always {@code true}: the event runs for every loot table,
     * but {@code LootTableLoadEvent} does not expose where the table came from, so data pack tables
     * cannot be told apart from built-in ones. Only Fabric reports the real value.
     *
     * <h4>Example: adding diamonds as a drop for dirt</h4>
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
     * @see ModifyLootTable#modifyLootTable(HolderGetter.Provider, ResourceKey, LootTableModificationContext, boolean)
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
     * @see ReplaceLootTable#replaceLootTable(HolderGetter.Provider, ResourceKey, LootTable)
     */
    Event<ReplaceLootTable> REPLACE_LOOT_TABLE = EventFactory.createCompoundEventResult();

    @FunctionalInterface
    interface ReplaceLootTable {
        /**
         * Replaces a loot table.
         *
         * @param registries the registries provider, for resolving the holders and tags a replacement table
         *                   refers to
         * @param key        the loot table key
         * @param original   the loot table that would otherwise be used
         * @return a {@link CompoundEventResult} carrying the replacement table,
         * or {@link CompoundEventResult#pass()} to leave the table alone
         */
        CompoundEventResult<LootTable> replaceLootTable(HolderGetter.Provider registries, ResourceKey<LootTable> key, LootTable original);
    }

    @FunctionalInterface
    interface ModifyLootTable {
        /**
         * Modifies a loot table.
         *
         * @param registries the registries provider, see
         *                   {@link ReplaceLootTable#replaceLootTable(HolderGetter.Provider, ResourceKey, LootTable)}
         * @param key        the loot table key
         * @param context    the context used to modify the loot table
         * @param builtin    if {@code true}, the loot table is built-in;
         *                   if {@code false}, it is from a user data pack
         */
        void modifyLootTable(HolderGetter.Provider registries, ResourceKey<LootTable> key, LootTableModificationContext context, boolean builtin);
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
