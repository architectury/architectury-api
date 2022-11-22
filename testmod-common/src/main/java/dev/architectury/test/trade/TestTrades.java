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

package dev.architectury.test.trade;

import dev.architectury.registry.level.entity.trade.SimpleTrade;
import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestTrades {
    public static void init() {
        for (var villagerProfession : BuiltInRegistries.VILLAGER_PROFESSION) {
            TradeRegistry.registerVillagerTrade(villagerProfession, 1, TestTrades.createTrades());
        }
        TradeRegistry.registerTradeForWanderingTrader(false, TestTrades.createTrades());
    }
    
    private static VillagerTrades.ItemListing[] createTrades() {
        var trade = new SimpleTrade(Items.APPLE.getDefaultInstance(), ItemStack.EMPTY, Items.ACACIA_BOAT.getDefaultInstance(), 1, 0, 1.0F);
        return new VillagerTrades.ItemListing[]{trade};
    }
}
