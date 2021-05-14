package me.shedaniel.architectury.registry.fabric;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class TradeRegistryImpl {
    
    public static void register(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if(level < 1 || level > 5){
            throw new RuntimeException("Villager Trade level has to be between 1 and 5!");
        }
        if (VillagerTrades.TRADES.containsKey(profession)) {
            VillagerTrades.ItemListing[] originalTrades = VillagerTrades.TRADES.get(profession).get(level);
            VillagerTrades.ItemListing[] combinedTrades = ArrayUtils.addAll(originalTrades, trades);
            VillagerTrades.TRADES.get(profession).put(level, combinedTrades);
        }
    }

    public static void registerTradeForWanderer(boolean rare, VillagerTrades.ItemListing... trades) {
        int level = rare ? 2 : 1;
        if (VillagerTrades.WANDERING_TRADER_TRADES.containsKey(level)) {
            VillagerTrades.ItemListing[] originalTrades = VillagerTrades.WANDERING_TRADER_TRADES.get(level);
            VillagerTrades.ItemListing[] combinedTrades = ArrayUtils.addAll(originalTrades, trades);
            VillagerTrades.WANDERING_TRADER_TRADES.put(level, combinedTrades);
        } else {
            VillagerTrades.WANDERING_TRADER_TRADES.put(level, trades);
        }
    }
}
