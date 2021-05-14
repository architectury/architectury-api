package me.shedaniel.architectury.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * This class is the easiest implementation of a trade object.
 * All trades added by vanilla do have custom classes like {@link VillagerTrades.EmeraldForItems}, but they aren't accessible.
 * 
 * Instead of widening the access of those classes or recreating them, this class was added to serve a basic trading implementation.
 * To register a trade, just call
 * {@link me.shedaniel.architectury.registry.TradeRegistry#registerVillagerTrade(net.minecraft.world.entity.npc.VillagerProfession, int, VillagerTrades.ItemListing...)}
 * or
 * {@link me.shedaniel.architectury.registry.TradeRegistry#registerTradeForWanderer(boolean, VillagerTrades.ItemListing...)}.
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
     * 
     * @param primaryPrice The first price a player has to pay to get the 'sale' stack.
     * @param secondaryPrice A optional, secondary price to pay as well as the primary one. If not needed just use {@link ItemStack#EMPTY}.
     * @param sale The ItemStack which a player can purchase in exchange for the two prices.
     * @param maxTrades The amount of trades one villager or wanderer can do. When the amount is surpassed, the trade can't be purchased anymore.
     * @param experiencePoints How much experience points does the player get, when trading. Vanilla uses between 2 and 30 for this.
     * @param priceMultiplier How much should the price rise, after the trade is used. It is added to the stack size of the primary price. Vanilla uses between 0.05 and 0.2.
     *                        
     * You can take a look at all the values the vanilla game uses right here {@link VillagerTrades#TRADES}.
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
