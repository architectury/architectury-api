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

import dev.architectury.transfer.item.ItemTransferHandler;
import dev.architectury.transfer.wrapper.combined.CombinedSingleTransferHandler;
import dev.architectury.transfer.wrapper.single.BaseSingleTransferHandler;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.AbstractList;
import java.util.List;

public class ContainerTransferHandler<C extends Container> implements CombinedItemTransferHandler, CombinedSingleTransferHandler<ItemStack> {
    public final C container;
    private List<SingleTransferHandler<ItemStack>> handlers = null;
    
    public ContainerTransferHandler(C container) {
        this.container = container;
    }
    
    protected List<SingleTransferHandler<ItemStack>> createHandlers() {
        return new Handlers();
    }
    
    @Override
    public List<SingleTransferHandler<ItemStack>> getParts() {
        if (handlers == null) {
            handlers = createHandlers();
        }
        
        return handlers;
    }
    
    protected SingleTransferHandler<ItemStack> asTransfer(int index) {
        return new SlotTransferHandler(container, index);
    }
    
    protected class Handlers extends AbstractList<SingleTransferHandler<ItemStack>> {
        @Override
        public SingleTransferHandler<ItemStack> get(int index) {
            if (index < 0 || index >= size()) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + size());
            }
            return asTransfer(index);
        }
        
        @Override
        public int size() {
            return container.getContainerSize();
        }
    }
    
    protected static class SlotTransferHandler implements BaseSingleTransferHandler<ItemStack>, ItemTransferHandler {
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
        
        @Override
        public void close() {
        }
    }
}
