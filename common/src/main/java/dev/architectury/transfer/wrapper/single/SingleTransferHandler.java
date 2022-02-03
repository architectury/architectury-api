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
    
    default long getAmount() {
        return getAmount(getResource());
    }
}