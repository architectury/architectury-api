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

import com.google.common.base.MoreObjects;
import me.shedaniel.architectury.registry.trade.TradeRegistry;
import me.shedaniel.architectury.registry.trade.impl.OfferMixingContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Iterator;
import java.util.Set;

/**
 * {@link AbstractVillager#addOffersFromItemListings(MerchantOffers, VillagerTrades.ItemListing[], int)} creates
 * a {@link Set} with x random integer from {@link VillagerTrades.ItemListing} array indexes to iterate through.
 * <p>
 * If we use {@link TradeRegistry} to remove one offer from a villager
 * we will end up with just x-1 offers but we still want to have x offers (as long there are enough) for a villager if
 * there are still {@link VillagerTrades.ItemListing} left.
 * <p>
 * To solve this we override the iterator with our own iterator which iterate through all indexes.
 * As soon {@link OfferMixingContext#maxOffers} offers are created we skip the remaining elements in the iterator {@link OfferMixingContext#skipIteratorIfMaxOffersReached()}.
 */
@Mixin(AbstractVillager.class)
public abstract class AbstractVillagerMixin extends Entity {
    public AbstractVillagerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Unique
    private final ThreadLocal<OfferMixingContext> offerContext = new ThreadLocal<>();
    
    
    @Redirect(
            method = "addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;")
    )
    public Iterator<Integer> overrideIterator(Set<Integer> set, MerchantOffers offers, VillagerTrades.ItemListing[] itemListings, int maxOffers) {
        OfferMixingContext context = new OfferMixingContext(MoreObjects.firstNonNull(architectury$getMaxOfferOverride(), maxOffers), itemListings, random);
        offerContext.set(context);
        return context.getIterator();
    }
    
    @ModifyVariable(
            method = "addOffersFromItemListings(Lnet/minecraft/world/item/trading/MerchantOffers;[Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;I)V",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    public MerchantOffer handleOffer(MerchantOffer offer) {
        OfferMixingContext context = offerContext.get();
        
        if (offer == null || context.getMaxOffers() == 0) {
            context.skipIteratorIfMaxOffersReached();
            return null;
        }
        
        MerchantOffer handledOffer = architectury$handleOffer(offer);
        if (handledOffer != null) {
            context.skipIteratorIfMaxOffersReached();
        }
        
        return handledOffer;
    }
    
    public MerchantOffer architectury$handleOffer(MerchantOffer offer) {
        return offer;
    }
    
    @Nullable
    public Integer architectury$getMaxOfferOverride() {
        return null;
    }
}
