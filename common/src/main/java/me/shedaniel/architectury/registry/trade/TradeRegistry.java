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

package me.shedaniel.architectury.registry.trade;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.shedaniel.architectury.registry.trade.impl.TradeRegistryData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TradeRegistry {
    private TradeRegistry() {
    }
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) for a villager by its profession and level.
     * When the mod loader is Forge, the {@code VillagerTradesEvent} event is used.
     *
     * @param profession The Profession the villager needs to have this trade.
     * @param level      The level the villager needs. Vanilla range is 1 to 5, however mods may extend that upper limit further.
     * @param trades     The trades to add to this profession at the specified level.
     */
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if (level < 1) {
            throw new IllegalArgumentException("Villager Trade level has to be at least 1!");
        }
        registerVillagerTrade0(profession, level, trades);
    }
    
    @ExpectPlatform
    private static void registerVillagerTrade0(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    /**
     * Override the max possible offers a villager can have by its profession and level.
     *
     * @param profession The Profession of the villager.
     * @param level      The level of the villager. Vanilla range is 1 to 5, however mods may extend that upper limit further.
     * @param maxOffers  Max possible offers a villager can have.
     */
    public static void setVillagerMaxOffers(VillagerProfession profession, int level, int maxOffers) {
        if (level < 1) {
            throw new IllegalArgumentException("Villager Trade level has to be at least 1!");
        }
        
        if (maxOffers < 0) {
            throw new IllegalArgumentException("Villager's max offers has to be at least 0!");
        }
        
        Map<Integer, Integer> map = TradeRegistryData.VILLAGER_MAX_OFFER_OVERRIDES.computeIfAbsent(profession, k -> new HashMap<>());
        map.put(level, maxOffers);
    }
    
    
    /**
     * Register a callback which provide {@link VillagerTradeOfferContext} to modify the given offer from a villager.
     * The callback gets called when {@link net.minecraft.world.entity.npc.Villager} generates their offer list.
     *
     * @param callback The callback to handle modification for the given offer context.
     */
    public static void modifyVillagerOffers(Consumer<VillagerTradeOfferContext> callback) {
        Objects.requireNonNull(callback);
        TradeRegistryData.VILLAGER_MODIFY_HANDLERS.add(callback);
    }
    
    /**
     * Register a filter which provide {@link VillagerTradeOfferContext} to test the given offer from a villager.
     * The filter gets called when {@link net.minecraft.world.entity.npc.Villager} generates their offer list.
     *
     * @param filter The filter to test if an offer should be removed. Returning true means the offer will be removed.
     */
    public static void removeVillagerOffers(Predicate<VillagerTradeOfferContext> filter) {
        Objects.requireNonNull(filter);
        TradeRegistryData.VILLAGER_REMOVE_HANDLERS.add(filter);
    }
    
    /**
     * Register a callback which provide {@link WanderingTraderOfferContext} to modify the given offer from the wandering trader.
     * The callback gets called when {@link net.minecraft.world.entity.npc.WanderingTrader} generates their offer list.
     *
     * @param callback The callback to handle modification for the given offer context.
     */
    public static void modifyWanderingTraderOffers(Consumer<WanderingTraderOfferContext> callback) {
        Objects.requireNonNull(callback);
        TradeRegistryData.WANDERING_TRADER_MODIFY_HANDLERS.add(callback);
    }
    
    /**
     * Register a filter which provide {@link WanderingTraderOfferContext} to test the given offer from the wandering trader.
     * The filter gets called when {@link net.minecraft.world.entity.npc.WanderingTrader} generates their offer list.
     *
     * @param filter The filter to test if an offer should be removed. Returning true means the offer will be removed.
     */
    public static void removeWanderingTraderOffers(Predicate<WanderingTraderOfferContext> filter) {
        Objects.requireNonNull(filter);
        TradeRegistryData.WANDERING_TRADER_REMOVE_HANDLERS.add(filter);
    }
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) to a wandering trader by its rarity.
     * When the mod loader is Forge, the {@code WandererTradesEvent} event is used.
     *
     * @param rare   Whether this trade is "rare". Rare trades have a five times lower chance of being used.
     * @param trades The trades to add to the wandering trader.
     */
    @ExpectPlatform
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    /**
     * Override the max possible offers the wandering trader can have. This does not affect the rare trade.
     *
     * @param maxOffers Max possible offers a villager can have.
     */
    public static void setWanderingTraderMaxOffers(int maxOffers) {
        if (maxOffers < 0) {
            throw new IllegalArgumentException("Wandering trader's max offers has to be at least 0!");
        }
        
        TradeRegistryData.wanderingTraderMaxOfferOverride = maxOffers;
    }
}
