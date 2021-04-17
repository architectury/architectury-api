package me.shedaniel.architectury.impl.fluid;

import me.shedaniel.architectury.core.access.builtin.Transaction;
import me.shedaniel.architectury.core.access.builtin.TransactionState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class TransactionImpl implements Transaction {
    private final List<BiConsumer<Transaction, TransactionState>> listeners = new ArrayList<>();
    private boolean opened = true;
    
    @Override
    public void abort() {
        if (opened) {
            opened = false;
            close(TransactionState.COMMITTED);
        }
    }
    
    @Override
    public void commit() {
        if (opened) {
            opened = false;
            close(TransactionState.COMMITTED);
        }
    }
    
    private void close(TransactionState state) {
        for (BiConsumer<Transaction, TransactionState> listener : listeners) {
            listener.accept(this, state);
        }
    }
    
    @Override
    public void onClose(BiConsumer<Transaction, TransactionState> consumer) {
        if (!opened) {
            listeners.add(consumer);
        } else {
            throw new IllegalStateException("The transaction has already been closed!");
        }
    }
}
