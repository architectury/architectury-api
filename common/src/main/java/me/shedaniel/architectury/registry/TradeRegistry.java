package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.annotations.ExpectPlatform;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeRegistry {
    
    private TradeRegistry(){}
    
    /**
     * Register a trade ({@link VillagerTrades.ItemListing}) for a villager by its profession and level.
     *
     * @param profession The Profession the villager needs to have this trade.
     * @param level The level the villager needs. Range is 1 to 5 with 1 being the lowest level and 5 the highest.
     * @param trades The trades to add to this profession at the specified level.
     *               
     * When the mod loader is Forge, the {@code VillagerTradesEvent} event is used.
     */
    @ExpectPlatform
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades){
        throw new AssertionError();
    }
    
    /**
     * Register a Trade (ItemListing) to a wandering trader by its rarity.
     *
     * @param rare Is this a rare trade. Rare trades only have a five times lower chance of being used.
     * @param trades The trades to add to the wandering trader.
     *
     * When the Modloader is forge the {@code WandererTradesEvent} event is used.
     */
    @ExpectPlatform
    public static void registerTradeForWanderer(boolean rare, VillagerTrades.ItemListing... trades){
        throw new AssertionError();
    }
    
}
