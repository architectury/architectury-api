package me.shedaniel.architectury.registry.forge;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import me.shedaniel.architectury.forge.ArchitecturyForge;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

@Mod.EventBusSubscriber(modid = ArchitecturyForge.MOD_ID)
public class TradeRegistryImpl {
    
    private static final Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> TRADES_TO_ADD = new HashMap<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_GENERIC = new ArrayList<>();
    private static final List<VillagerTrades.ItemListing> WANDERER_TRADER_TRADES_RARE = new ArrayList<>();
    
    public static void registerVillagerTrade(VillagerProfession profession, int level, VillagerTrades.ItemListing... trades) {
        if(level < 1 || level > 5){
            throw new RuntimeException("Villager Trade level has to be between 1 and 5!");
        }
        Int2ObjectMap<List<VillagerTrades.ItemListing>> tradesForProfession = TRADES_TO_ADD.getOrDefault(profession, new Int2ObjectOpenHashMap<>());
        List<VillagerTrades.ItemListing> tradesForLevel = tradesForProfession.get(level);
        if(tradesForLevel != null){
            Collections.addAll(tradesForLevel, trades);
        } else {
            tradesForProfession.put(level, Arrays.asList(trades));
        }
        TRADES_TO_ADD.put(profession, tradesForProfession);
    }
    
    public static void registerTradeForWanderingTrader(boolean rare, VillagerTrades.ItemListing... trades) {
        if(rare){
            Collections.addAll(WANDERER_TRADER_TRADES_RARE, trades);
        } else {
            Collections.addAll(WANDERER_TRADER_TRADES_GENERIC, trades);
        }
    }
    
    @SubscribeEvent
    public static void onTradeRegistering(VillagerTradesEvent event){
        if(TRADES_TO_ADD.containsKey(event.getType())){
            TRADES_TO_ADD.get(event.getType()).forEach((level, trade) -> {
                event.getTrades().get(level).addAll(trade);
            });
        }
    }

    @SubscribeEvent
    public static void onWanderingTradeRegistering(WandererTradesEvent event){
        if(!WANDERER_TRADER_TRADES_GENERIC.isEmpty()){
            event.getGenericTrades().addAll(WANDERER_TRADER_TRADES_GENERIC);
        }
        if(!WANDERER_TRADER_TRADES_RARE.isEmpty()){
            event.getRareTrades().addAll(WANDERER_TRADER_TRADES_RARE);
        }
    }
}
