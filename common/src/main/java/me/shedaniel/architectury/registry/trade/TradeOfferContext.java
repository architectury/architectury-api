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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Random;

public class TradeOfferContext {
    private final MerchantOfferAccess offer;
    private final Entity entity;
    private final Random random;
    
    public TradeOfferContext(MerchantOffer offer, Entity entity, Random random) {
        this.offer = new MerchantOfferAccess(offer);
        this.entity = entity;
        this.random = random;
    }
    
    public MerchantOfferAccess getOffer() {
        return offer;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    public Random getRandom() {
        return random;
    }
}
