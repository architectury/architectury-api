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

import net.minecraft.world.entity.npc.VillagerTrades;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OfferMixingContext {
    private int currentIndex;
    private final int maxOffers;
    private final Iterator<Integer> iterator;
    private final VillagerTrades.ItemListing[] itemListings;
    private final Random random;
    
    public OfferMixingContext(int maxOffers, VillagerTrades.ItemListing[] itemListings, Random random) {
        this.currentIndex = 0;
        this.maxOffers = Math.min(maxOffers, itemListings.length);
        this.itemListings = itemListings;
        this.random = random;
        
        List<Integer> shuffled = createShuffledIndexList();
        this.iterator = shuffled.iterator();
    }
    
    public void skipIteratorIfMaxOffersReached() {
        currentIndex++;
        if (currentIndex >= getMaxOffers()) {
            skip();
        }
    }
    
    @NotNull
    public Iterator<Integer> getIterator() {
        return iterator;
    }
    
    private void skip() {
        iterator.forEachRemaining(($) -> { });
    }
    
    @NotNull
    private List<Integer> createShuffledIndexList() {
        List<Integer> shuffledListings = new ArrayList<>();
        for (int i = 0; i < itemListings.length; i++) {
            shuffledListings.add(i);
        }
        Collections.shuffle(shuffledListings, random);
        return shuffledListings;
    }
    
    public int getMaxOffers() {
        return maxOffers;
    }
}
