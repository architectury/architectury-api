package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeRegistry {
    
    private TradeRegistry(){}
    
    @ExpectPlatform
    public static void register(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades){
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static void registerTradeForWanderer(boolean rare, VillagerTrades.ItemListing... trades){
        throw new AssertionError();
    }
    
}
