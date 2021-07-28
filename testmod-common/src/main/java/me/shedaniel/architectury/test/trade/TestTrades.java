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

package me.shedaniel.architectury.test.trade;

import me.shedaniel.architectury.registry.trade.SimpleTrade;
import me.shedaniel.architectury.registry.trade.TradeRegistry;
import me.shedaniel.architectury.registry.trade.VillagerTradeOfferContext;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TestTrades {
    public static void init() {
        for (VillagerProfession villagerProfession : Registry.VILLAGER_PROFESSION) {
            TradeRegistry.registerVillagerTrade(villagerProfession, 1, TestTrades.createTrades());
        }
        TradeRegistry.registerTradeForWanderingTrader(false, TestTrades.createTrades());
        
        TradeRegistry.registerVillagerOfferModify(farmerSwitchBreadResultToGoldenApple);
        TradeRegistry.registerVillagerOfferModify(farmerCarrotsNeedSticksToo);
        TradeRegistry.registerVillagerOfferModify(farmerCarrotWithStickIncreasePriceMultiplier);
        TradeRegistry.registerVillagerOfferModify(butcherWantsManyEmeralds);
        TradeRegistry.registerVillagerOfferModify(butcherGivesMoreEmeraldForChicken);
        
        TradeRegistry.registerVillagerOfferRemoving(removeCarrotTrade);
        TradeRegistry.registerVillagerOfferRemoving(removeFarmersLevelTwoTrades);
        
        TradeRegistry.overrideVillagerMaxOffers(VillagerProfession.FISHERMAN, 1, 100);
        TradeRegistry.overrideVillagerMaxOffers(VillagerProfession.BUTCHER, 2, 100);
        
        TradeRegistry.overrideVillagerMaxOffers(VillagerProfession.SHEPHERD, 1, 10); // easier to level up
        TradeRegistry.overrideVillagerMaxOffers(VillagerProfession.SHEPHERD, 2, 0);
        
        TradeRegistry.overrideWanderingTraderMaxOffers(10); // will end up having 11 because of the rare item
    }
    
    private static VillagerTrades.ItemListing[] createTrades() {
        SimpleTrade trade = new SimpleTrade(Items.APPLE.getDefaultInstance(), ItemStack.EMPTY, Items.ACACIA_BOAT.getDefaultInstance(), 1, 0, 1.0F);
        return new VillagerTrades.ItemListing[]{trade};
    }
    
    public static Consumer<VillagerTradeOfferContext> farmerSwitchBreadResultToGoldenApple = ctx -> {
        if (ctx.getProfession() == VillagerProfession.FARMER && ctx.getOffer().getResult().getItem() == Items.BREAD) {
            ctx.getOffer().setResult(new ItemStack(Items.GOLDEN_APPLE));
            ctx.getOffer().setXp(10000); // should fill the XP bar on top of the trade gui to the moon
            ctx.getOffer().setMaxUses(1);
        }
    };
    
    public static Consumer<VillagerTradeOfferContext> farmerCarrotsNeedSticksToo = ctx -> {
        if (ctx.getProfession() == VillagerProfession.FARMER && ctx.getOffer().getPrimary().getItem() == Items.CARROT) {
            ctx.getOffer().setSecondary(new ItemStack(Items.STICK, 32)); // will switch the empty itemstack to 3 sticks
        }
    };
    
    public static Consumer<VillagerTradeOfferContext> farmerCarrotWithStickIncreasePriceMultiplier = ctx -> {
        if (ctx.getProfession() == VillagerProfession.FARMER
                && ctx.getOffer().getPrimary().getItem() == Items.CARROT
                && ctx.getOffer().getSecondary().getItem() == Items.STICK) {
            ctx.getOffer().setPriceMultiplier(5f);
        }
    };
    
    public static Consumer<VillagerTradeOfferContext> butcherWantsManyEmeralds = ctx -> {
        if (ctx.getProfession() == VillagerProfession.BUTCHER && ctx.getOffer().getPrimary().getItem() == Items.EMERALD) {
            ctx.getOffer().getPrimary().setCount(42);
        }
    };
    
    public static Consumer<VillagerTradeOfferContext> butcherGivesMoreEmeraldForChicken = ctx -> {
        if (ctx.getProfession() == VillagerProfession.BUTCHER && ctx.getOffer().getPrimary().getItem() == Items.CHICKEN) {
            ctx.getOffer().getResult().setCount(64);
        }
    };
    
    public static Predicate<VillagerTradeOfferContext> removeCarrotTrade = ctx -> ctx.getProfession() == VillagerProfession.FARMER && ctx.getOffer().getPrimary().getItem() == Items.POTATO;
    
    public static Predicate<VillagerTradeOfferContext> removeFarmersLevelTwoTrades = ctx -> ctx.getProfession() == VillagerProfession.FARMER && ctx.getLevel() == 2;
    
    
}
