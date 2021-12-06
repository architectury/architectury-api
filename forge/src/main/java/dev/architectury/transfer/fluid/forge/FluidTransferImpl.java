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

package dev.architectury.transfer.fluid.forge;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.forge.FluidStackHooksForge;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.access.BlockLookup;
import dev.architectury.transfer.access.ItemLookup;
import dev.architectury.transfer.fluid.FluidTransfer;
import dev.architectury.transfer.forge.ForgeBlockLookupRegistration;
import dev.architectury.transfer.forge.ForgeItemLookupRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.BucketPickupHandlerWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidTransferImpl {
    @Nullable
    public static TransferHandler<FluidStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof IFluidHandler) {
            return new ForgeTransferHandler((IFluidHandler) object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    public static void init() {
        FluidTransfer.BLOCK.addQueryHandler(instantiateBlockLookup());
        FluidTransfer.BLOCK.addRegistrationHandler(ForgeBlockLookupRegistration.create(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
                (level, pos, state, blockEntity) -> (direction, handler) -> new ArchFluidHandler(handler)));
//        FluidTransfer.ITEM.addQueryHandler(instantiateItemLookup());
//        FluidTransfer.ITEM.addRegistrationHandler(ForgeItemLookupRegistration.create(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
//                stack -> (direction, handler) -> new ArchFluidHandlerItem(handler, stack)));
    }
    
    public static BlockLookup<TransferHandler<FluidStack>, Direction> instantiateBlockLookup() {
        return new BlockLookup<TransferHandler<FluidStack>, Direction>() {
            @Override
            @Nullable
            public TransferHandler<FluidStack> get(Level level, BlockPos pos, Direction direction) {
                return get(level, pos, level.getBlockState(pos), null, direction);
            }
            
            @Override
            @Nullable
            public TransferHandler<FluidStack> get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                Block block = state.getBlock();
                IFluidHandler handler = null;
                if (block instanceof IFluidBlock) {
                    handler = new FluidBlockWrapper((IFluidBlock) block, level, pos);
                } else if (block instanceof BucketPickup) {
                    handler = new BucketPickupHandlerWrapper((BucketPickup) block, level, pos);
                } else if (state.hasBlockEntity()) {
                    if (blockEntity == null) {
                        blockEntity = level.getBlockEntity(pos);
                    }
                    if (blockEntity != null) {
                        handler = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).resolve().orElse(null);
                    }
                }
                return wrap(handler);
            }
        };
    }
    
    public static ItemLookup<TransferHandler<FluidStack>, TransferHandler<ItemStack>> instantiateItemLookup() {
        return new ItemLookup<TransferHandler<FluidStack>, TransferHandler<ItemStack>>() {
            @Override
            @Nullable
            public TransferHandler<FluidStack> get(ItemStack stack, TransferHandler<ItemStack> context) {
                return wrap(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve().orElse(null));
            }
        };
    }
    
    public static class ArchFluidHandler implements IFluidHandler {
        private static final Predicate<FluidStack> TRUE = stack -> true;
        private TransferHandler<FluidStack> handler;
        
        public ArchFluidHandler(TransferHandler<FluidStack> handler) {
            this.handler = handler;
        }
        
        @Override
        public int getTanks() {
            return handler.getContentsSize();
        }
        
        @NotNull
        @Override
        public net.minecraftforge.fluids.FluidStack getFluidInTank(int index) {
            return FluidStackHooksForge.toForge(handler.getContent(index).getResource());
        }
        
        @Override
        public int getTankCapacity(int index) {
            return (int) handler.getContent(index).getCapacity();
        }
        
        @Override
        public boolean isFluidValid(int index, @NotNull net.minecraftforge.fluids.FluidStack stack) {
            FluidStack content = handler.getContent(index).getResource();
            return content.getFluid() == stack.getFluid() && Objects.equals(content.getTag(), stack.getTag());
        }
        
        @Override
        public int fill(net.minecraftforge.fluids.FluidStack stack, FluidAction action) {
            return (int) handler.insert(FluidStackHooksForge.fromForge(stack), getFluidAction(action));
        }
        
        @NotNull
        @Override
        public net.minecraftforge.fluids.FluidStack drain(net.minecraftforge.fluids.FluidStack stack, FluidAction action) {
            return FluidStackHooksForge.toForge(handler.extract(FluidStackHooksForge.fromForge(stack), getFluidAction(action)));
        }
        
        @NotNull
        @Override
        public net.minecraftforge.fluids.FluidStack drain(int maxAmount, FluidAction action) {
            return FluidStackHooksForge.toForge(handler.extract(TRUE, maxAmount, getFluidAction(action)));
        }
    }
    
    public static class ArchFluidHandlerItem extends ArchFluidHandler implements IFluidHandlerItem {
        private final ItemStack stack;
        
        public ArchFluidHandlerItem(TransferHandler<FluidStack> handler, ItemStack stack) {
            super(handler);
            this.stack = stack;
        }
        
        @NotNull
        @Override
        public ItemStack getContainer() {
            return stack;
        }
    }
    
    private static class ForgeTransferHandler implements TransferHandler<FluidStack> {
        private IFluidHandler handler;
        
        private ForgeTransferHandler(IFluidHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public Stream<ResourceView<FluidStack>> getContents() {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Itr(), 0), false);
        }
        
        @Override
        public int getContentsSize() {
            return handler.getTanks();
        }
        
        @Override
        public ResourceView<FluidStack> getContent(int index) {
            return new ForgeResourceView(index);
        }
        
        @Override
        public long insert(FluidStack toInsert, TransferAction action) {
            return handler.fill(FluidStackHooksForge.toForge(toInsert), getFluidAction(action));
        }
        
        @Override
        public FluidStack extract(FluidStack toExtract, TransferAction action) {
            return FluidStackHooksForge.fromForge(handler.drain(FluidStackHooksForge.toForge(toExtract), getFluidAction(action)));
        }
        
        @Override
        public FluidStack extract(Predicate<FluidStack> toExtract, long maxAmount, TransferAction action) {
            for (int i = 0; i < handler.getTanks(); i++) {
                net.minecraftforge.fluids.FluidStack forgeStack = handler.getFluidInTank(i);
                FluidStack stack = FluidStackHooksForge.fromForge(forgeStack);
                if (toExtract.test(stack)) {
                    net.minecraftforge.fluids.FluidStack copy = forgeStack.copy();
                    copy.setAmount((int) maxAmount);
                    net.minecraftforge.fluids.FluidStack extracted = handler.drain(copy, getFluidAction(action));
                    stack.setAmount(extracted.getAmount());
                    return stack;
                }
            }
            
            return blank();
        }
        
        @Override
        public FluidStack blank() {
            return FluidStack.empty();
        }
        
        @Override
        public Object saveState() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void loadState(Object state) {
            throw new UnsupportedOperationException();
        }
        
        private class Itr implements Iterator<ResourceView<FluidStack>> {
            int cursor;
            
            Itr() {
            }
            
            @Override
            public boolean hasNext() {
                return cursor != handler.getTanks();
            }
            
            @Override
            public ResourceView<FluidStack> next() {
                int i = cursor;
                if (i >= handler.getTanks())
                    throw new NoSuchElementException();
                cursor = i + 1;
                return new ForgeResourceView(i);
            }
            
            @Override
            public void forEachRemaining(Consumer<? super ResourceView<FluidStack>> action) {
                Objects.requireNonNull(action);
                final int size = handler.getTanks();
                int i = cursor;
                if (i < size) {
                    for (; i < size; i++) {
                        action.accept(new ForgeResourceView(i));
                    }
                    cursor = i;
                }
            }
        }
        
        private class ForgeResourceView implements ResourceView<FluidStack> {
            int index;
            
            public ForgeResourceView(int index) {
                this.index = index;
            }
            
            @Override
            public FluidStack getResource() {
                return FluidStackHooksForge.fromForge(handler.getFluidInTank(index));
            }
            
            @Override
            public long getCapacity() {
                return handler.getTankCapacity(index);
            }
            
            @Override
            public FluidStack extract(FluidStack toExtract, TransferAction action) {
                // TODO: implement
                return null;
            }
            
            @Override
            public FluidStack blank() {
                return FluidStack.empty();
            }
            
            @Override
            public FluidStack copyWithAmount(FluidStack resource, long amount) {
                return resource.copyWithAmount(amount);
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
    
    private static IFluidHandler.FluidAction getFluidAction(TransferAction action) {
        return action == TransferAction.SIMULATE ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
    }
    
    private static TransferAction getFluidAction(IFluidHandler.FluidAction action) {
        return action == IFluidHandler.FluidAction.SIMULATE ? TransferAction.SIMULATE : TransferAction.ACT;
    }
}
