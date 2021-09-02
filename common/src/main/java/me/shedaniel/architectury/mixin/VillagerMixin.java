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

import me.shedaniel.architectury.registry.trade.interal.TradeRegistryData;
import me.shedaniel.architectury.registry.trade.interal.VillagerTradeOfferContext;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Villager.class)
public abstract class VillagerMixin extends AbstractVillagerMixin {
    
    public VillagerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Shadow
    public abstract VillagerData getVillagerData();
    
    @Override
    public MerchantOffer architectury$handleOffer(MerchantOffer offer) {
        VillagerData vd = getVillagerData();
    
        VillagerTradeOfferContext context = new VillagerTradeOfferContext(vd, offer, this, random);
        
        boolean removeResult = TradeRegistryData.invokeVillagerOfferRemoving(context);
        if(removeResult) {
            return null;
        }
    
        TradeRegistryData.invokeVillagerOfferModify(context);
        return offer;
    }
    
    @Override
    @Nullable
    public Integer architectury$getMaxOfferOverride() {
        return TradeRegistryData.getVillagerMaxOffers(getVillagerData().getProfession(), getVillagerData().getLevel());
    }
}
