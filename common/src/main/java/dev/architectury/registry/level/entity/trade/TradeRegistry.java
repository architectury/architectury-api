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

package dev.architectury.registry.level.entity.trade;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;

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
    public static void registerVillagerTrade(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... trades) {
        if (level < 1) {
            throw new IllegalArgumentException("Villager Trade level has to be at least 1!");
        }
        registerVillagerTrade0(profession, level, trades);
    }
    
    @ExpectPlatform
    private static void registerVillagerTrade0(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) to a wandering trader by its rarity.
     * When the mod loader is Forge, the {@code WandererTradesEvent} event is used.
     *
     * @param type   The type of trade to add to the wandering trader.
     * @param trades The trades to add to the wandering trader.
     */
    @ExpectPlatform
    public static void registerTradeForWanderingTrader(WandererTradeType type, VillagerTrades.ItemListing... trades) {
        throw new AssertionError();
    }
    
    public enum WandererTradeType {
        BUYING_TRADES,
        GENERIC_TRADES,
        RARE_TRADES,
    }
    
}
