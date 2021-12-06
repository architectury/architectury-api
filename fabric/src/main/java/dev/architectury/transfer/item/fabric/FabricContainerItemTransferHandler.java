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

package dev.architectury.transfer.item.fabric;

import com.google.common.collect.Iterables;
import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FabricContainerItemTransferHandler implements TransferHandler<ItemStack> {
    private final ContainerItemContext context;
    @Nullable
    private final Transaction transaction;
    
    public FabricContainerItemTransferHandler(ContainerItemContext context, @Nullable Transaction transaction) {
        this.context = context;
        this.transaction = transaction;
    }
    
    public ContainerItemContext getContext() {
        return context;
    }
    
    @Override
    public Stream<ResourceView<ItemStack>> getContents() {
        return Stream.concat(Stream.of(context.getMainSlot()), context.getAdditionalSlots().stream())
                .map(FabricResourceView::new);
    }
    
    @Override
    public int getContentsSize() {
        return 1 + context.getAdditionalSlots().size();
    }
    
    @Override
    public ResourceView<ItemStack> getContent(int index) {
        if (index == 0) return new FabricResourceView(context.getMainSlot());
        return new FabricResourceView(context.getAdditionalSlots().get(index - 1));
    }
    
    @Override
    public long insert(ItemStack toInsert, TransferAction action) {
        long inserted;
        
        try (Transaction nested = Transaction.openNested(this.transaction)) {
            inserted = this.context.insert(ItemVariant.of(toInsert), toInsert.getCount(), nested);
            
            if (action == TransferAction.ACT) {
                nested.commit();
            }
        }
        
        return inserted;
    }
    
    @Override
    public ItemStack extract(ItemStack toExtract, TransferAction action) {
        if (toExtract.isEmpty()) return blank();
        long extracted;
        
        try (Transaction nested = Transaction.openNested(this.transaction)) {
            extracted = this.context.extract(ItemVariant.of(toExtract), toExtract.getCount(), nested);
            
            if (action == TransferAction.ACT) {
                nested.commit();
            }
        }
        
        return ItemStackHooks.copyWithCount(toExtract, (int) extracted);
    }
    
    @Override
    public ItemStack extract(Predicate<ItemStack> toExtract, long maxAmount, TransferAction action) {
        try (Transaction nested = Transaction.openNested(this.transaction)) {
            for (StorageView<ItemVariant> view : Iterables.concat(Collections.singletonList(context.getMainSlot()), context.getAdditionalSlots())) {
                if (toExtract.test(view.getResource().toStack((int) view.getAmount()))) {
                    long extracted = view.extract(view.getResource(), maxAmount, nested);
                    
                    if (action == TransferAction.ACT) {
                        nested.commit();
                    }
                    
                    return view.getResource().toStack((int) extracted);
                }
            }
        }
        
        return blank();
    }
    
    @Override
    public ItemStack blank() {
        return ItemStack.EMPTY;
    }
    
    @Override
    public Object saveState() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void loadState(Object state) {
        throw new UnsupportedOperationException();
    }
    
    private class FabricResourceView implements ResourceView<ItemStack> {
        private final SingleSlotStorage<ItemVariant> storage;
        
        private FabricResourceView(SingleSlotStorage<ItemVariant> storage) {
            this.storage = storage;
        }
        
        @Override
        public ItemStack getResource() {
            return storage.getResource().toStack((int) storage.getAmount());
        }
        
        @Override
        public long getCapacity() {
            return storage.getCapacity();
        }
        
        @Override
        public ItemStack copyWithAmount(ItemStack resource, long amount) {
            return ItemStackHooks.copyWithCount(resource, (int) amount);
        }
        
        @Override
        public ItemStack extract(ItemStack toExtract, TransferAction action) {
            if (toExtract.isEmpty()) return blank();
            long extracted;
            
            try (Transaction nested = Transaction.openNested(FabricContainerItemTransferHandler.this.transaction)) {
                extracted = this.storage.extract(ItemVariant.of(toExtract), toExtract.getCount(), nested);
                
                if (action == TransferAction.ACT) {
                    nested.commit();
                }
            }
            
            return copyWithAmount(toExtract, extracted);
        }
        
        @Override
        public ItemStack blank() {
            return ItemStack.EMPTY;
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
}
