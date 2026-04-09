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
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeRegistryImpl {
    private static final Map<ResourceKey<VillagerProfession>, Int2ObjectMap<List<TradeRegistry.ItemListing>>> TRADES_TO_ADD = new HashMap<>();
    private static final EnumMap<TradeRegistry.WandererTradeType, List<TradeRegistry.ItemListing>> WANDERER_TRADES_TO_ADD = new EnumMap<>(TradeRegistry.WandererTradeType.class);

    public static void registerVillagerTrade0(ResourceKey<VillagerProfession> profession, int level, TradeRegistry.ItemListing... trades) {
        Int2ObjectMap<List<TradeRegistry.ItemListing>> tradesForProfession = TRADES_TO_ADD.computeIfAbsent(profession, $ -> new Int2ObjectOpenHashMap<>());
        List<TradeRegistry.ItemListing> tradesForLevel = tradesForProfession.computeIfAbsent(level, $ -> new ArrayList<>());
        Collections.addAll(tradesForLevel, trades);
    }

    public static void registerTradeForWanderingTrader(TradeRegistry.WandererTradeType type, TradeRegistry.ItemListing... trades) {
        List<TradeRegistry.ItemListing> tradesForType = WANDERER_TRADES_TO_ADD.computeIfAbsent(type, $ -> new ArrayList<>());
        Collections.addAll(tradesForType, trades);
    }

    public static void appendVillagerTrades(ServerLevel level, MerchantOffers offers, ResourceKey<VillagerProfession> profession, int villagerLevel, Entity entity, RandomSource random) {
        Int2ObjectMap<List<TradeRegistry.ItemListing>> tradesForProfession = TRADES_TO_ADD.get(profession);
        if (tradesForProfession == null) {
            return;
        }

        appendTrades(level, offers, tradesForProfession.get(villagerLevel), entity, random);
    }

    public static void appendWanderingTraderTrades(ServerLevel level, MerchantOffers offers, Entity entity, RandomSource random) {
        for (TradeRegistry.WandererTradeType type : TradeRegistry.WandererTradeType.values()) {
            appendTrades(level, offers, WANDERER_TRADES_TO_ADD.get(type), entity, random);
        }
    }

    private static void appendTrades(ServerLevel level, MerchantOffers offers, List<TradeRegistry.ItemListing> trades, Entity entity, RandomSource random) {
        if (trades == null) {
            return;
        }

        for (TradeRegistry.ItemListing trade : trades) {
            MerchantOffer offer = trade.getOffer(level, entity, random);
            if (offer != null) {
                offers.add(offer);
            }
        }
    }
}
