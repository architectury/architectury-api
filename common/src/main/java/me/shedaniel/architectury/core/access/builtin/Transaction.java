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
