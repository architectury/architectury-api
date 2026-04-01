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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public class TradeRegistryImpl {
    public static void registerVillagerTrade0(ResourceKey<VillagerProfession> profession, int level, TradeRegistry.ItemListing... trades) {
        throw new UnsupportedOperationException("Villager trade registration is not implemented for Fabric on Minecraft 26.1 yet.");
    }
    
    public static void registerTradeForWanderingTrader(TradeRegistry.WandererTradeType type, TradeRegistry.ItemListing... trades) {
        throw new UnsupportedOperationException("Wandering trader registration is not implemented for Fabric on Minecraft 26.1 yet.");
    }
}
