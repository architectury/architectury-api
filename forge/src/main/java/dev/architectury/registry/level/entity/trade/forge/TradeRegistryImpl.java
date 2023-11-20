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

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;

import java.util.*;

public class TradeRegistryImpl {
    private static final Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> TRADES_TO_ADD = new HashMap<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_GENERIC = new ArrayList<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_RARE = new ArrayList<>();
    
    static {
        MinecraftForge.EVENT_BUS.addListener(TradeRegistryImpl::onTradeRegistering);
        MinecraftForge.EVENT_BUS.addListener(TradeRegistryImpl::onWanderingTradeRegistering);
    }
    
    public static void registerVillagerTrade0(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> tradesForProfession = TRADES_TO_ADD.computeIfAbsent(profession, $ -> new Int2ObjectOpenHashMap<>());
        List<VillagerTrades.ItemListing> tradesForLevel = tradesForProfession.computeIfAbsent(level, $ -> new ArrayList<>());
        Collections.addAll(tradesForLevel, trades);
    }
    
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        if (rare) {
            Collections.addAll(WANDERER_TRADER_TRADES_RARE, trades);
        } else {
            Collections.addAll(WANDERER_TRADER_TRADES_GENERIC, trades);
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
        if (!WANDERER_TRADER_TRADES_GENERIC.isEmpty()) {
            event.getGenericTrades().addAll(WANDERER_TRADER_TRADES_GENERIC);
        }
        if (!WANDERER_TRADER_TRADES_RARE.isEmpty()) {
            event.getRareTrades().addAll(WANDERER_TRADER_TRADES_RARE);
        }
    }
}
