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

package dev.architectury.transfer.item.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.access.BlockTransferAccess;
import dev.architectury.transfer.forge.ForgeBlockTransferAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    
    public static BlockTransferAccess<TransferHandler<ItemStack>, Direction> instantiateBlockAccess() {
        return new ForgeBlockTransferAccess<TransferHandler<ItemStack>, IItemHandler>() {
            @Override
            @Nullable
            public TransferHandler<ItemStack> get(Level level, BlockPos pos, Direction direction) {
                return get(level, pos, level.getBlockState(pos), null, direction);
            }
            
            @Override
            @Nullable
            public TransferHandler<ItemStack> get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                Block block = state.getBlock();
                IItemHandler handler = null;
                if (state.hasBlockEntity()) {
                    if (blockEntity == null) {
                        blockEntity = level.getBlockEntity(pos);
                    }
                    if (blockEntity != null) {
                        handler = blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction).resolve().orElse(null);
                    }
                }
                return wrap(handler);
            }
            
            @Override
            public Capability<IItemHandler> getCapability() {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
            }
            
            @Override
            public IItemHandler from(TransferHandler<ItemStack> handler) {
                return new ArchItemHandler(handler);
            }
        };
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
            return handler.getContent(index).getResource();
        }
        
        @Override
        public int getSlotLimit(int index) {
            return (int) handler.getContent(index).getCapacity();
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
            ItemStack content = handler.getContent(index).getResource();
            return content.getItem() == stack.getItem() && Objects.equals(content.getTag(), stack.getTag());
        }
    }
    
    private static class ForgeTransferHandler implements TransferHandler<ItemStack> {
        private IItemHandler handler;
        
        public ForgeTransferHandler(IItemHandler handler) {
            this.handler = handler;
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
            int toInsertCount = toInsert.getCount();
            ItemStack left = ItemHandlerHelper.insertItemStacked(handler, toInsert, action == TransferAction.SIMULATE);
            return toInsertCount - left.getCount();
        }
        
        @Override
        public ItemStack extract(ItemStack toExtract, TransferAction action) {
            return null;
        }
        
        @Override
        public ItemStack extract(Predicate<ItemStack> toExtract, long maxAmount, TransferAction action) {
            return null;
        }
        
        @Override
        public ItemStack blank() {
            return ItemStack.EMPTY;
        }
        
        private class ForgeResourceView implements ResourceView<ItemStack> {
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
        }
    }
}
