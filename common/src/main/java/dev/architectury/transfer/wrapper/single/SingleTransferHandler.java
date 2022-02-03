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

package dev.architectury.transfer.wrapper.single;

import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.view.ModifiableView;
import dev.architectury.transfer.view.VariantView;

import java.util.stream.Stream;

public interface SingleTransferHandler<T> extends TransferHandler<T>, ResourceView<T>, ModifiableView<T>, VariantView<T> {
    @Override
    default Stream<ResourceView<T>> getContents() {
        return Stream.of(this);
    }
    
    @Override
    default int getContentsSize() {
        return 1;
    }
    
    @Override
    default ResourceView<T> getContent(int index) {
        if (index != 0) throw new IndexOutOfBoundsException("Index must be 0, got " + index);
        return this;
    }
    
    @Override
    default Object saveState() {
        return copy(getResource());
    }
    
    @Override
    default void loadState(Object state) {
        setResource((T) state);
    }
    
    void setResource(T resource);
    
    T copy(T resource);
    
    long getCapacity(T resource);
    
    @Override
    default long insert(T toInsert, TransferAction action) {
        T resource = getResource();
        long currentAmount = getAmount(resource);
        boolean isEmpty = currentAmount <= 0;
        if ((isEmpty || isSameVariant(resource, toInsert)) && canInsert(toInsert)) {
            long slotSpace = isEmpty ? getCapacity(toInsert) : getCapacity() - currentAmount;
            long inserted = Math.min(slotSpace, getAmount(toInsert));
            
            if (inserted > 0) {
                if (isEmpty) {
                    setResource(copyWithAmount(toInsert, inserted));
                } else {
                    setResource(copyWithAmount(resource, currentAmount + inserted));
                }
            }
            
            return inserted;
        }
        
        return 0;
    }
    
    @Override
    default T extract(T toExtract, TransferAction action) {
        T resource = getResource();
        if (!isSameVariant(resource, toExtract)) return blank();
        long extracted = Math.min(getAmount(toExtract), getAmount(resource));
        if (extracted > 0) {
            setResource(copyWithAmount(resource, getAmount(resource) - extracted));
            
            return copyWithAmount(toExtract, extracted);
        }
        
        return blank();
    }
}
