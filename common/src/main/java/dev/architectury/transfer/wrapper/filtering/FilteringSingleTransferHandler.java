/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
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

package dev.architectury.transfer.wrapper.filtering;

import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.wrapper.forwarding.ForwardingSingleTransferHandler;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;

import java.util.function.Predicate;

public interface FilteringSingleTransferHandler<T> extends FilteringTransferHandler<T>, ForwardingSingleTransferHandler<T> {
    static <T> FilteringSingleTransferHandler<T> of(SingleTransferHandler<T> delegate, Predicate<T> canInsert, Predicate<T> canExtract) {
        return new FilteringSingleTransferHandler<T>() {
            @Override
            public SingleTransferHandler<T> forwardingTo() {
                return delegate;
            }
            
            @Override
            public boolean canInsert(T toInsert) {
                return canInsert.test(toInsert);
            }
            
            @Override
            public boolean canExtract(T toExtract) {
                return canExtract.test(toExtract);
            }
        };
    }
    
    @Override
    default T extract(Predicate<T> toExtract, long maxAmount, TransferAction action) {
        return FilteringTransferHandler.super.extract(toExtract, maxAmount, action);
    }
}
