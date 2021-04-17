package me.shedaniel.architectury.core.access.builtin;

import org.jetbrains.annotations.ApiStatus;

import java.io.Closeable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ApiStatus.NonExtendable
public interface Transaction extends Closeable {
    /**
     * Closes and aborts the transaction.
     */
    @Override
    default void close() {
        abort();
    }
    
    void abort();
    
    /**
     * Commits the transaction.
     */
    void commit();
    
    /**
     * Adds a consumer invoked on close.
     *
     * @param consumer the consumer to add to the transaction
     */
    void onClose(BiConsumer<Transaction, TransactionState> consumer);
    
    default void onAbort(Consumer<Transaction> consumer) {
        onClose((transaction, state) -> {
            if (state == TransactionState.ABORTED) {
                consumer.accept(transaction);
            }
        });
    }
    
    default void onCommit(Consumer<Transaction> consumer) {
        onClose((transaction, state) -> {
            if (state == TransactionState.COMMITTED) {
                consumer.accept(transaction);
            }
        });
    }
}
