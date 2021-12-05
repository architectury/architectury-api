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

package dev.architectury.transfer.fabric;

import dev.architectury.transfer.TransferContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class TransferContextImpl implements TransferContext {
    @ApiStatus.Internal
    public final Transaction transaction;
    
    private TransferContextImpl(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public static TransferContext create() {
        return new TransferContextImpl(Transaction.openOuter());
    }
    
    public static TransferContext create(@Nullable Object transaction) {
        if (transaction != null && !(transaction instanceof Transaction)) {
            throw new IllegalArgumentException("transaction must be a Transaction");
        }
        
        return new TransferContextImpl(transaction == null ? Transaction.openOuter() : (Transaction) transaction);
    }
    
    @Override
    public int nestingDepth() {
        return transaction.nestingDepth();
    }
    
    @Override
    public void close() throws Exception {
        this.transaction.close();
    }
}
