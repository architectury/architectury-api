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

package dev.architectury.registry.level.entity.trade.fabric;

import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Collections;

public class TradeRegistryImpl {
    public static void registerVillagerTrade0(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerVillagerOffers(profession, level, allTradesList -> Collections.addAll(allTradesList, trades));
    }
    
    public static void registerTradeForWanderingTrader(TradeRegistry.WandererTradeType type, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerWanderingTraderOffers(builder -> {
            builder.addOffersToPool(switch (type) {
                case BUYING_TRADES -> TradeOfferHelper.WanderingTraderOffersBuilder.BUY_ITEMS_POOL;
                case GENERIC_TRADES -> TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL;
                case RARE_TRADES -> TradeOfferHelper.WanderingTraderOffersBuilder.SELL_SPECIAL_ITEMS_POOL;
            }, trades);
        });
    }
}
