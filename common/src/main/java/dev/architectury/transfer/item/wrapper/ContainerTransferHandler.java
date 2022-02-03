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

package dev.architectury.transfer.item.wrapper;

import dev.architectury.transfer.TransferHandler;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;

public class ContainerTransferHandler implements CombinedItemTransferHandler {
    protected final Container container;
    private Iterable<TransferHandler<ItemStack>> handlers = null;
    
    public ContainerTransferHandler(Container container) {
        this.container = container;
    }
    
    protected Iterable<TransferHandler<ItemStack>> createHandlers() {
        return new Handlers();
    }
    
    @Override
    public Iterable<TransferHandler<ItemStack>> getHandlers() {
        if (handlers == null) {
            handlers = createHandlers();
        }
        
        return handlers;
    }
    
    protected class Handlers extends AbstractList<TransferHandler<ItemStack>> {
        @Override
        public TransferHandler<ItemStack> get(int index) {
            if (index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + size());
            }
            return asTransfer(index);
        }
        
        protected TransferHandler<ItemStack> asTransfer(int index) {
            return new SlotTransferHandler(container, index);
        }
        
        @Override
        public int size() {
            return container.getContainerSize();
        }
    }
    
    protected static class SlotTransferHandler implements SingleItemTransferHandler {
        protected final Container container;
        protected final int index;
        
        public SlotTransferHandler(Container container, int index) {
            this.container = container;
            this.index = index;
        }
        
        @Override
        public void setResource(ItemStack resource) {
            container.setItem(index, resource);
        }
        
        @Override
        public ItemStack getResource() {
            return container.getItem(index);
        }
        
        @Override
        public long getCapacity() {
            return Math.min(container.getMaxStackSize(), getResource().getMaxStackSize());
        }
    }
}
