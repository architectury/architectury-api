package me.shedaniel.architectury.core.access.builtin;

import me.shedaniel.architectury.core.access.builtin.util.CombinedStorage;
import me.shedaniel.architectury.core.access.builtin.util.EmptyStorage;

import java.util.Collection;

public interface Storage<T> {
    /**
     * Returns a combined {@link Storage} from a collection of storages.
     *
     * @param storages the collection of storages, storages will be evaluated by its order.
     * @param <T>      the type of storage
     * @return the combined storage
     */
    static <T> Storage<T> combine(Collection<Storage<T>> storages) {
        if (storages.size() == 0) return EmptyStorage.empty();
        if (storages.size() == 1) return storages.iterator().next();
        return new CombinedStorage<>(storages);
    }
    
    /**
     * Extracts {@link T} from the storage,
     *
     * @param type        the type to extract
     * @param maxAmount   the maximum amount to extract
     * @param transaction the transaction of this extraction
     * @return the amount extracted
     */
    long extract(T type, long maxAmount, Transaction transaction);
    
    /**
     * Inserts {@link T} into the storage,
     *
     * @param type        the type to insert
     * @param amount      the amount to insert
     * @param transaction the transaction of this insertion
     * @return the amount inserted
     */
    long insert(T type, long amount, Transaction transaction);
}
