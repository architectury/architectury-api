package me.shedaniel.architectury.registry.trade;

import net.minecraft.world.entity.npc.VillagerTrades;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class OfferMixingContext {
    private int currentIndex;
    private final int maxOffers;
    private final VisibleIterator<Integer> iterator;
    private final VillagerTrades.ItemListing[] itemListings;
    private final Random random;
    
    public OfferMixingContext(int maxOffers, VillagerTrades.ItemListing[] itemListings, Random random) {
        this.currentIndex = 0;
        this.maxOffers = Math.max(maxOffers, itemListings.length);
        this.itemListings = itemListings;
        this.random = random;
        
        List<Integer> shuffled = createShuffledIndexList();
        this.iterator = new VisibleIterator<>(shuffled.iterator());
    }
    
    public void skipIteratorIfMaxOffersReached() {
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
    
    public VillagerTrades.ItemListing getCurrentItemListing() {
        return itemListings[iterator.getCurrentElement()];
    }
    
    public static class VisibleIterator<E> implements Iterator<E>{
    
        private final Iterator<E> intern;
        private E currentElement;
        
        public VisibleIterator(Iterator<E> intern) {
            this.intern = intern;
        }
    
        @Override
        public boolean hasNext() {
            return intern.hasNext();
        }
    
        @Override
        public E next() {
            E e = intern.next();
            currentElement = e;
            return e;
        }
    
        @Override
        public void remove() {
            intern.remove();
        }
    
        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            intern.forEachRemaining(action);
        }
        
        public E getCurrentElement() {
            return currentElement;
        }
    }
}
