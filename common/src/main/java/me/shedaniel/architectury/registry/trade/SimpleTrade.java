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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * This class is the easiest implementation of a trade object.
 * All trades added by vanilla do have custom classes like {@link VillagerTrades.EmeraldForItems}, but they aren't accessible.
 * <p>
 * Instead of widening the access of those classes or recreating them, this class was added to serve a basic trading implementation.
 * To register a trade, just call
 * {@link TradeRegistry#registerVillagerTrade(net.minecraft.world.entity.npc.VillagerProfession, int, VillagerTrades.ItemListing...)}
 * or
 * {@link TradeRegistry#registerTradeForWanderingTrader(boolean, VillagerTrades.ItemListing...)}.
 */
public class SimpleTrade implements VillagerTrades.ItemListing {
    private final ItemStack primaryPrice;
    private final ItemStack secondaryPrice;
    private final ItemStack sale;
    private final int maxTrades;
    private final int experiencePoints;
    private final float priceMultiplier;
    
    /**
     * Constructor for creating the trade.
     * You can take a look at all the values the vanilla game uses right here {@link VillagerTrades#TRADES}.
     *
     * @param primaryPrice     The first price a player has to pay to get the 'sale' stack.
     * @param secondaryPrice   An optional, secondary price to pay as well as the primary one. If not needed just use {@link ItemStack#EMPTY}.
     * @param sale             The ItemStack which a player can purchase in exchange for the two prices.
     * @param maxTrades        The amount of trades one villager or wanderer can do. When the amount is surpassed, the trade can't be purchased anymore.
     * @param experiencePoints How much experience points does the player get, when trading. Vanilla uses between 2 and 30 for this.
     * @param priceMultiplier  How much should the price rise, after the trade is used. It is added to the stack size of the primary price. Vanilla uses between 0.05 and 0.2.
     */
    public SimpleTrade(ItemStack primaryPrice, ItemStack secondaryPrice, ItemStack sale, int maxTrades, int experiencePoints, float priceMultiplier) {
        this.primaryPrice = primaryPrice;
        this.secondaryPrice = secondaryPrice;
        this.sale = sale;
        this.maxTrades = maxTrades;
        this.experiencePoints = experiencePoints;
        this.priceMultiplier = priceMultiplier;
    }
    
    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, Random random) {
        return new MerchantOffer(this.primaryPrice, this.secondaryPrice, this.sale, this.maxTrades, this.experiencePoints, this.priceMultiplier);
    }
}
