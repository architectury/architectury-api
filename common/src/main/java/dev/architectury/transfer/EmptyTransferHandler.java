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

package dev.architectury.transfer;

import dev.architectury.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

class EmptyTransferHandler<T> implements TransferHandler<T> {
    static final TransferHandler<ItemStack> ITEM = new EmptyTransferHandler<>(() -> ItemStack.EMPTY);
    static final TransferHandler<FluidStack> FLUID = new EmptyTransferHandler<>(FluidStack::empty);
    private final Supplier<T> blank;
    
    protected EmptyTransferHandler(Supplier<T> blank) {
        this.blank = blank;
    }
    
    @Override
    public Stream<ResourceView<T>> getContents() {
        return Stream.empty();
    }
    
    @Override
    public int getContentsSize() {
        return 0;
    }
    
    @Override
    public ResourceView<T> getContent(int index) {
        throw new IndexOutOfBoundsException(index);
    }
    
    @Override
    public long insert(T toInsert, TransferAction action) {
        return 0;
    }
    
    @Override
    public T extract(T toExtract, TransferAction action) {
        return blank();
    }
    
    @Override
    public T extract(Predicate<T> toExtract, long maxAmount, TransferAction action) {
        return blank();
    }
    
    @Override
    public T blank() {
        return blank.get();
    }
    
    @Override
    public Object saveState() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void loadState(Object state) {
        throw new UnsupportedOperationException();
    }
}
