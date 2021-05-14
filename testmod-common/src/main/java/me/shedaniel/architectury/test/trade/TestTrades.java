package me.shedaniel.architectury.test.trade;

import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.TradeRegistry;
import me.shedaniel.architectury.trade.SimpleTrade;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestTrades {
    
    public static void init(){
        for (VillagerProfession villagerProfession : Registry.VILLAGER_PROFESSION) {
            TradeRegistry.register(villagerProfession, 1, TestTrades.createTrades());
        }
    }
    
    private static VillagerTrades.ItemListing[] createTrades(){
        SimpleTrade trade = new SimpleTrade(Items.APPLE.getDefaultInstance(), ItemStack.EMPTY, Items.ACACIA_BOAT.getDefaultInstance(), 1, 0, 1.0F);
        return new VillagerTrades.ItemListing[]{trade};
    }
    
}
