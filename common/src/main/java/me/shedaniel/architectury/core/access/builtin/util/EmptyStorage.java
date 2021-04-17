package me.shedaniel.architectury.core.access.builtin.util;

import me.shedaniel.architectury.core.access.builtin.Storage;
import me.shedaniel.architectury.core.access.builtin.Transaction;

public final class EmptyStorage implements Storage<Object> {
    private static final Storage<Object> EMPTY = new EmptyStorage();
    
    public static <T> Storage<T> empty() {
        return (Storage<T>) EMPTY;
    }
    
    @Override
    public long extract(Object type, long maxAmount, Transaction transaction) {
        return 0;
    }
    
    @Override
    public long insert(Object type, long amount, Transaction transaction) {
        return 0;
    }
}
