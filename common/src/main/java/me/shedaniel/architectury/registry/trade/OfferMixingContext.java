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
        this.maxOffers = Math.max(maxOffers, itemListings.length);
        this.itemListings = itemListings;
        this.random = random;
        
        List<Integer> shuffled = createShuffledIndexList();
        this.iterator = shuffled.iterator();
    }
    
    public void checkAndHandleMaxOfferReached() {
        currentIndex++;
        if (currentIndex >= maxOffers) {
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
    
}
