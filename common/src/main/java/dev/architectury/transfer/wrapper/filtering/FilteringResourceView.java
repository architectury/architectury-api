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

import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.view.ExtractableView;
import dev.architectury.transfer.wrapper.forwarding.ForwardingResourceView;

import java.util.function.Predicate;

public interface FilteringResourceView<T> extends ForwardingResourceView<T>, ExtractableView<T> {
    static <T> FilteringResourceView<T> of(ResourceView<T> delegate, Predicate<T> canExtract) {
        return new FilteringResourceView<T>() {
            @Override
            public ResourceView<T> forwardingTo() {
                return delegate;
            }
            
            @Override
            public boolean canExtract(T toExtract) {
                return canExtract.test(toExtract);
            }
            
            @Override
            public void close() {
                delegate.close();
            }
        };
    }
    
    @Override
    boolean canExtract(T toExtract);
    
    @Override
    default T extract(T toExtract, TransferAction action) {
        if (canExtract(toExtract)) {
            return ForwardingResourceView.super.extract(toExtract, action);
        } else {
            return blank();
        }
    }
}
