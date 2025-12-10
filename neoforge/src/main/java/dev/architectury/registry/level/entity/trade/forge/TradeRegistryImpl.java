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

package dev.architectury.registry.level.entity.trade.forge;

import dev.architectury.registry.level.entity.trade.TradeRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.*;

public class TradeRegistryImpl {
    private static final Map<ResourceKey<VillagerProfession>, Int2ObjectMap<List<VillagerTrades.ItemListing>>> TRADES_TO_ADD = new HashMap<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_BUYING = new ArrayList<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_GENERIC = new ArrayList<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_RARE = new ArrayList<>();
    
    static {
        NeoForge.EVENT_BUS.addListener(TradeRegistryImpl::onTradeRegistering);
        NeoForge.EVENT_BUS.addListener(TradeRegistryImpl::onWanderingTradeRegistering);
    }
    
    public static void registerVillagerTrade0(ResourceKey<VillagerProfession> profession, int level, VillagerTrades.ItemListing... trades) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> tradesForProfession = TRADES_TO_ADD.computeIfAbsent(profession, $ -> new Int2ObjectOpenHashMap<>());
        List<VillagerTrades.ItemListing> tradesForLevel = tradesForProfession.computeIfAbsent(level, $ -> new ArrayList<>());
        Collections.addAll(tradesForLevel, trades);
    }
    
    public static void registerTradeForWanderingTrader(TradeRegistry.WandererTradeType type, VillagerTrades.ItemListing... trades) {
        if (type == TradeRegistry.WandererTradeType.RARE_TRADES) {
            Collections.addAll(WANDERER_TRADER_TRADES_RARE, trades);
        } else if (type == TradeRegistry.WandererTradeType.GENERIC_TRADES) {
            Collections.addAll(WANDERER_TRADER_TRADES_GENERIC, trades);
        } else {
            Collections.addAll(WANDERER_TRADER_TRADES_BUYING, trades);
        }
    }
    
    public static void onTradeRegistering(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = TRADES_TO_ADD.get(event.getType());
        
        if (trades != null) {
            for (Int2ObjectMap.Entry<List<VillagerTrades.ItemListing>> entry : trades.int2ObjectEntrySet()) {
                event.getTrades().computeIfAbsent(entry.getIntKey(), $ -> NonNullList.create()).addAll(entry.getValue());
            }
        }
    }
    
    public static void onWanderingTradeRegistering(WandererTradesEvent event) {
        if (!WANDERER_TRADER_TRADES_BUYING.isEmpty()) {
            event.getBuyingTrades().addAll(WANDERER_TRADER_TRADES_BUYING);
        }
        if (!WANDERER_TRADER_TRADES_GENERIC.isEmpty()) {
            event.getGenericTrades().addAll(WANDERER_TRADER_TRADES_GENERIC);
        }
        if (!WANDERER_TRADER_TRADES_RARE.isEmpty()) {
            event.getRareTrades().addAll(WANDERER_TRADER_TRADES_RARE);
        }
    }
}
