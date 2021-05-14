package me.shedaniel.architectury.registry.trade.fabric;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Collections;

public class TradeRegistryImpl {
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerVillagerOffers(profession, level, allTradesList -> Collections.addAll(allTradesList, trades));
    }
    
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerWanderingTraderOffers(rare ? 2 : 1, allTradesList -> Collections.addAll(allTradesList, trades));
    }
}
