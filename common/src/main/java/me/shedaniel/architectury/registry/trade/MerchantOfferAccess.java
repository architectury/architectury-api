package me.shedaniel.architectury.registry.trade;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

public class MerchantOfferAccess {
    private final MerchantOffer offer;
    
    public MerchantOfferAccess(MerchantOffer offer) {
        this.offer = offer;
    }
    
    public ItemStack getPrimary() {
        return offer.getBaseCostA();
    }
    
    public void setPrimary(ItemStack itemStack) {
        offer.baseCostA = itemStack.copy();
    }
    
    public ItemStack getSecondary() {
        return offer.getCostB();
    }
    
    public void setSecondary(ItemStack itemStack) {
        offer.costB = itemStack.copy();
    }
    
    public ItemStack getResult() {
        return offer.getResult();
    }
    
    public void setResult(ItemStack itemStack) {
        offer.result = itemStack.copy();
    }
    
    public int getMaxUses() {
        return offer.getMaxUses();
    }
    
    public void setMaxUses(int maxUses) {
        offer.maxUses = maxUses;
    }
    
    public float getPriceMultiplier() {
        return offer.getPriceMultiplier();
    }
    
    public void setPriceMultiplier(float priceMultiplier) {
        offer.priceMultiplier = priceMultiplier;
    }
    
    public int getXp() {
        return offer.getXp();
    }
    
    public void setXp(int xp) {
        offer.xp = xp;
    }
}
