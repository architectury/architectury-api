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

package dev.architectury.transfer.fluid.fabric;

import com.google.common.collect.Iterables;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferContext;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.fabric.TransferContextImpl;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class FluidTransferImpl {
    @Nullable
    public static TransferHandler<FluidStack> get(Level level, BlockPos pos, Direction direction) {
        return wrap(FluidStorage.SIDED.find(level, pos, direction));
    }
    
    @Nullable
    public static TransferHandler<FluidStack> get(Level level, BlockPos pos, @Nullable BlockEntity blockEntity, Direction direction) {
        if (blockEntity != null) {
            return wrap(FluidStorage.SIDED.find(level, pos, blockEntity.getBlockState(), blockEntity, direction));
        } else {
            return get(level, pos, direction);
        }
    }
    
    @Nullable
    public static TransferHandler<FluidStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof Storage) {
            return new FabricTransferHandler((Storage) object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    private static class FabricTransferHandler implements TransferHandler<FluidStack> {
        private final Storage<FluidVariant> storage;
        
        private FabricTransferHandler(Storage<FluidVariant> storage) {
            this.storage = storage;
        }
        
        @Override
        public Iterable<ResourceView<FluidStack>> getResources(TransferContext context) {
            return Iterables.transform(storage.iterable(((TransferContextImpl) context).transaction),
                    FabricResourceView::new);
        }
        
        @Override
        public long insert(FluidStack toInsert, TransferAction action, TransferContext context) {
            Transaction transaction = ((TransferContextImpl) context).transaction;
            long inserted;
            
            try (Transaction nested = Transaction.openNested(transaction)) {
                inserted = this.storage.insert(FluidStackHooksFabric.toFabric(toInsert), toInsert.getAmount(), nested);
                
                if (action == TransferAction.COMMIT) {
                    nested.commit();
                }
            }
            
            return inserted;
        }
        
        @Override
        public long extract(FluidStack toExtract, TransferAction action, TransferContext context) {
            Transaction transaction = ((TransferContextImpl) context).transaction;
            long extracted;
            
            try (Transaction nested = Transaction.openNested(transaction)) {
                extracted = this.storage.extract(FluidStackHooksFabric.toFabric(toExtract), toExtract.getAmount(), nested);
                
                if (action == TransferAction.COMMIT) {
                    nested.commit();
                }
            }
            
            return extracted;
        }
        
        private static class FabricResourceView implements ResourceView<FluidStack> {
            private final StorageView<FluidVariant> view;
            
            private FabricResourceView(StorageView<FluidVariant> view) {
                this.view = view;
            }
            
            @Override
            public FluidStack getResource() {
                return FluidStackHooksFabric.fromFabric(view.getResource(), view.getAmount());
            }
            
            @Override
            public long getCapacity() {
                return view.getCapacity();
            }
        }
    }
}
