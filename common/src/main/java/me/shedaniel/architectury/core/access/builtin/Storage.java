package me.shedaniel.architectury.core.access.builtin;

import me.shedaniel.architectury.core.access.builtin.util.CombinedStorage;
import me.shedaniel.architectury.core.access.builtin.util.EmptyStorage;

import java.util.Collection;

public interface Storage<T> {
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
    
    long insert(T type, long amount, Transaction transaction);
}
