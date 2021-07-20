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
    
    public ItemStack getSecondary() {
        return offer.getCostB();
    }
    
    public ItemStack getResult() {
        return offer.getResult();
    }
    
    public int getMaxUses() {
        return offer.getMaxUses();
    }
    
    public float getPriceMultiplier() {
        return offer.getPriceMultiplier();
    }
    
    public int getXp() {
        return offer.getXp();
    }
}
