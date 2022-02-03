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

package dev.architectury.transfer.wrapper;

import com.google.common.base.Predicates;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.view.ModifiableView;

import java.util.function.Predicate;

public interface FilteringTransferHandler<T> extends ForwardingTransferHandler<T>, ModifiableView<T> {
    static <T> FilteringTransferHandler<T> unmodifiable(TransferHandler<T> delegate) {
        return FilteringTransferHandler.of(delegate, Predicates.alwaysFalse(), Predicates.alwaysFalse());
    }
    
    static <T> FilteringTransferHandler<T> onlyInsert(TransferHandler<T> delegate) {
        return FilteringTransferHandler.of(delegate, Predicates.alwaysTrue(), Predicates.alwaysFalse());
    }
    
    static <T> FilteringTransferHandler<T> onlyExtract(TransferHandler<T> delegate) {
        return FilteringTransferHandler.of(delegate, Predicates.alwaysFalse(), Predicates.alwaysTrue());
    }
    
    static <T> FilteringTransferHandler<T> predicate(TransferHandler<T> delegate, Predicate<T> predicate) {
        return FilteringTransferHandler.of(delegate, predicate, predicate);
    }
    
    static <T> FilteringTransferHandler<T> of(TransferHandler<T> delegate, Predicate<T> canInsert, Predicate<T> canExtract) {
        return new FilteringTransferHandler<T>() {
            @Override
            public TransferHandler<T> forwardingTo() {
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
    default long insert(T toInsert, TransferAction action) {
        if (canInsert(toInsert)) {
            return ForwardingTransferHandler.super.insert(toInsert, action);
        } else {
            return 0;
        }
    }
    
    @Override
    default T extract(T toExtract, TransferAction action) {
        if (canExtract(toExtract)) {
            return ForwardingTransferHandler.super.extract(toExtract, action);
        } else {
            return blank();
        }
    }
    
    @Override
    default T extract(Predicate<T> toExtract, long maxAmount, TransferAction action) {
        return ForwardingTransferHandler.super.extract(toExtract.and(this::canExtract), maxAmount, action);
    }
    
    @Override
    default ResourceView<T> forwardResource(ResourceView<T> resource) {
        return FilteringResourceView.of(ForwardingTransferHandler.super.forwardResource(resource), this::canExtract);
    }
}
