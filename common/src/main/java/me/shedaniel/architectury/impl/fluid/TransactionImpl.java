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
