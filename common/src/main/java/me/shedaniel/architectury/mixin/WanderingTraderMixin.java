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

package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.registry.trade.impl.TradeRegistryData;
import me.shedaniel.architectury.registry.trade.WanderingTraderOfferContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillagerMixin {
    public WanderingTraderMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Unique
    private final ThreadLocal<VillagerTrades.ItemListing> vanillaSelectedItemListing = new ThreadLocal<>();
    
    @ModifyVariable(
            method = "updateTrades()V",
            at = @At(value = "INVOKE_ASSIGN"),
            ordinal = 0
    )
    public VillagerTrades.ItemListing storeItemListing(VillagerTrades.ItemListing itemListing) {
        vanillaSelectedItemListing.set(itemListing);
        return itemListing;
    }
    
    @ModifyVariable(
            method = "updateTrades()V",
            at = @At(value = "INVOKE_ASSIGN"),
            ordinal = 0
    )
    public MerchantOffer handleSecondListingOffer(MerchantOffer offer) {
        if (offer == null) {
            return null;
        }
    
        return invokeWanderingTraderEvents(offer, true);
    }
    
    @Override
    public MerchantOffer architectury$handleOffer(MerchantOffer offer) {
        return invokeWanderingTraderEvents(offer, false);
    }
    
    @Nullable
    private MerchantOffer invokeWanderingTraderEvents(MerchantOffer offer, boolean rare) {
        WanderingTraderOfferContext context = new WanderingTraderOfferContext(offer, rare, this, random);
        boolean removeResult = TradeRegistryData.invokeWanderingTraderOfferRemoving(context);
        if (removeResult) {
            return null;
        }
    
        TradeRegistryData.invokeWanderingTraderOfferModify(context);
        return offer;
    }
    
    @Override
    @Nullable
    public Integer architectury$getMaxOfferOverride() {
        return TradeRegistryData.getWanderingTraderMaxOffers();
    }
}
