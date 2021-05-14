package me.shedaniel.architectury.registry.fabric;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

import java.util.Collections;

public class TradeRegistryImpl {
    
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if(level < 1 || level > 5){
            throw new IllegalArgumentException("Villager Trade level has to be between 1 and 5!");
        }    
        TradeOfferHelper.registerVillagerOffers(profession, level, allTradesList -> Collections.addAll(allTradesList, trades));
    }

    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        TradeOfferHelper.registerWanderingTraderOffers(rare ? 2 : 1, allTradesList -> Collections.addAll(allTradesList, trades));
    }
}
