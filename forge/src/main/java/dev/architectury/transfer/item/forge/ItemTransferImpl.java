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

package dev.architectury.transfer.item.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.item.ItemTransferHandler;
import dev.architectury.transfer.item.ItemTransferView;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static dev.architectury.utils.Amount.toInt;

public class ItemTransferImpl {
    @Nullable
    public static TransferHandler<ItemStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof IItemHandler) {
            return new ForgeTransferHandler((IItemHandler) object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    @Nullable
    public static Object unwrap(@Nullable TransferHandler<ItemStack> handler) {
        if (handler == null) return null;
        
        if (handler instanceof ForgeTransferHandler) {
            return ((ForgeTransferHandler) handler).getHandler();
        } else {
            return new ArchItemHandler(handler);
        }
    }
    
    public static Object platformBlockLookup() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }
    
    public static class ArchItemHandler implements IItemHandler {
        private static final Predicate<FluidStack> TRUE = stack -> true;
        private TransferHandler<ItemStack> handler;
        
        public ArchItemHandler(TransferHandler<ItemStack> handler) {
            this.handler = handler;
        }
        
        @Override
        public int getSlots() {
            return handler.getContentsSize();
        }
        
        @NotNull
        @Override
        public ItemStack getStackInSlot(int index) {
            try (var resource = handler.getContent(index)) {
                return resource.getResource();
            }
        }
        
        @Override
        public int getSlotLimit(int index) {
            try (var resource = handler.getContent(index)) {
                return toInt(resource.getCapacity());
            }
        }
        
        @NotNull
        @Override
        public ItemStack insertItem(int index, @NotNull ItemStack arg, boolean simulate) {
            return null;
        }
        
        @NotNull
        @Override
        public ItemStack extractItem(int index, int maxAmount, boolean simulate) {
            return null;
        }
        
        @Override
        public boolean isItemValid(int index, @NotNull ItemStack stack) {
            ItemStack content;
            
            try (var resource = handler.getContent(index)) {
                content = resource.getResource();
            }
            return content.getItem() == stack.getItem() && Objects.equals(content.getTag(), stack.getTag());
        }
    }
    
    private static class ForgeTransferHandler implements ItemTransferHandler {
        private IItemHandler handler;
        
        public ForgeTransferHandler(IItemHandler handler) {
            this.handler = handler;
        }
        
        public IItemHandler getHandler() {
            return handler;
        }
        
        @Override
        public Stream<ResourceView<ItemStack>> getContents() {
            return IntStream.range(0, handler.getSlots()).mapToObj(ForgeResourceView::new);
        }
        
        @Override
        public int getContentsSize() {
            return handler.getSlots();
        }
        
        @Override
        public ResourceView<ItemStack> getContent(int index) {
            return new ForgeResourceView(index);
        }
        
        @Override
        public long insert(ItemStack toInsert, TransferAction action) {
            if (toInsert.isEmpty()) return 0;
            int toInsertCount = toInsert.getCount();
            ItemStack remaining = ItemHandlerHelper.insertItemStacked(handler, toInsert, action == TransferAction.SIMULATE);
            return toInsertCount - remaining.getCount();
        }
        
        @Override
        public ItemStack extract(ItemStack toExtract, TransferAction action) {
            int toExtractAmount = toExtract.getCount();
            if (toExtractAmount == 0) return ItemStack.EMPTY;
            int extractedAmount = 0;
            
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slot = handler.getStackInSlot(i);
                
                if (ItemHandlerHelper.canItemStacksStack(toExtract, slot)) {
                    ItemStack extracted = handler.extractItem(i, toExtractAmount - extractedAmount, action == TransferAction.SIMULATE);
                    extractedAmount += extracted.getCount();
                    if (extractedAmount >= toExtractAmount) {
                        break;
                    }
                }
            }
            
            return copyWithAmount(toExtract, extractedAmount);
        }
        
        @Override
        public ItemStack extract(Predicate<ItemStack> toExtract, long maxAmount, TransferAction action) {
            ItemStack type = null;
            int extractedAmount = 0;
            
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slot = handler.getStackInSlot(i);
                if (slot.isEmpty()) continue;
                
                if (type == null ? toExtract.test(slot) : ItemHandlerHelper.canItemStacksStack(type, slot)) {
                    ItemStack extracted = handler.extractItem(i, toInt(maxAmount - extractedAmount), action == TransferAction.SIMULATE);
                    if (type == null && !extracted.isEmpty()) {
                        type = extracted;
                    }
                    extractedAmount += extracted.getCount();
                    if (extractedAmount >= maxAmount) {
                        break;
                    }
                }
            }
            
            return type == null ? blank() : copyWithAmount(type, extractedAmount);
        }
        
        @Override
        public Object saveState() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void loadState(Object state) {
            throw new UnsupportedOperationException();
        }
        
        private class ForgeResourceView implements ResourceView<ItemStack>, ItemTransferView {
            int index;
            
            public ForgeResourceView(int index) {
                this.index = index;
            }
            
            @Override
            public ItemStack getResource() {
                return handler.getStackInSlot(index);
            }
            
            @Override
            public long getCapacity() {
                return handler.getSlotLimit(index);
            }
            
            @Override
            public long insert(ItemStack toInsert, TransferAction action) {
                if (toInsert.isEmpty()) return 0;
                
                int toInsertCount = toInsert.getCount();
                ItemStack remaining = handler.insertItem(index, toInsert, action == TransferAction.SIMULATE);
                return toInsertCount - remaining.getCount();
            }
            
            @Override
            public ItemStack extract(ItemStack toExtract, TransferAction action) {
                if (toExtract.isEmpty()) return blank();
                return handler.extractItem(index, toExtract.getCount(), action == TransferAction.SIMULATE);
            }
            
            @Override
            public Object saveState() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void loadState(Object state) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public void close() {
            }
        }
    }
}
