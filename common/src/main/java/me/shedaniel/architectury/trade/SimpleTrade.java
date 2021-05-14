package me.shedaniel.architectury.trade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class SimpleTrade implements VillagerTrades.ItemListing {
    
    private final ItemStack primaryPrice;
    private final ItemStack secondaryPrice;
    private final ItemStack sale;
    private final int maxTrades;
    private final int experiencePoints;
    private final float priceMultiplier;
    
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
