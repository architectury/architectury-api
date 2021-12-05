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
import dev.architectury.transfer.TransferContext;
import dev.architectury.transfer.TransferHandler;
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
import net.minecraftforge.fluids.capability.wrappers.BucketPickupHandlerWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class FluidTransferImpl {
    @Nullable
    public static TransferHandler<FluidStack> get(Level level, BlockPos pos, Direction direction) {
        return get(level, pos, null, direction);
    }
    
    @Nullable
    public static TransferHandler<FluidStack> get(Level level, BlockPos pos, @Nullable BlockEntity blockEntity, Direction direction) {
        BlockState state = level.getBlockState(pos);
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
    
    @Nullable
    public static TransferHandler<FluidStack> get(ItemStack stack) {
        return wrap(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve().orElse(null));
    }
    
    @Nullable
    public static TransferHandler<FluidStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof IFluidHandler) {
            return new ForgeTransferHandler((IFluidHandler) object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    private static class ForgeTransferHandler implements TransferHandler<FluidStack> {
        private IFluidHandler handler;
        
        private ForgeTransferHandler(IFluidHandler handler) {
            this.handler = handler;
        }
        
        @Override
        public Iterable<ResourceView<FluidStack>> getResources(TransferContext context) {
            return Itr::new;
        }
        
        @Override
        public long insert(FluidStack toInsert, TransferAction action, TransferContext context) {
            return handler.fill(FluidStackHooksForge.toForge(toInsert), action == TransferAction.SIMULATE ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
        }
        
        @Override
        public long extract(FluidStack toExtract, TransferAction action, TransferContext context) {
            return handler.drain(FluidStackHooksForge.toForge(toExtract), action == TransferAction.SIMULATE ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE).getAmount();
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
        }
    }
}
