/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

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
