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

package me.shedaniel.architectury.registry.trade.fabric;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Collections;

public class TradeRegistryImpl {
    private static void registerVillagerTrade0(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerVillagerOffers(profession, level, allTradesList -> Collections.addAll(allTradesList, trades));
    }
    
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerWanderingTraderOffers(rare ? 2 : 1, allTradesList -> Collections.addAll(allTradesList, trades));
    }
}
