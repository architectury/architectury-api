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

package dev.architectury.transfer.energy.fabric;

import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.energy.EnergyTransfer;
import dev.architectury.transfer.energy.EnergyTransferHandler;
import dev.architectury.transfer.fabric.BlockApiLookupWrapper;
import dev.architectury.transfer.fabric.FabricBlockLookupRegistration;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;

public class EnergyTransferImpl {
    @Nullable
    public static SingleTransferHandler<Long> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof EnergyStorage) {
            return new FabricTransferHandler((EnergyStorage) object, null);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    @Nullable
    public static EnergyStorage unwrap(@Nullable SingleTransferHandler<Long> handler) {
        if (handler == null) return null;
        
        if (handler instanceof FabricTransferHandler) {
            return ((FabricTransferHandler) handler).getStorage();
        } else {
            return new EnergyTransferHandlerStorage(handler);
        }
    }
    
    public static void init() {
        EnergyTransfer.BLOCK.addQueryHandler(new BlockApiLookupWrapper<>(EnergyStorage.SIDED, EnergyTransferImpl::wrap));
        EnergyTransfer.BLOCK.addRegistrationHandler(FabricBlockLookupRegistration.create(EnergyStorage.SIDED, EnergyTransferImpl::unwrap));
    }
    
    private static class FabricTransferHandler implements EnergyTransferHandler {
        private final EnergyStorage storage;
        @Nullable
        private final Transaction transaction;
        
        public FabricTransferHandler(EnergyStorage storage, @Nullable Transaction transaction) {
            this.storage = storage;
            this.transaction = transaction;
        }
        
        public EnergyStorage getStorage() {
            return storage;
        }
        
        @Override
        public Long getResource() {
            return storage.getAmount();
        }
        
        @Override
        public long getCapacity() {
            return storage.getCapacity();
        }
        
        @Override
        public long insert(Long toInsert, TransferAction action) {
            long inserted;
            
            try (Transaction nested = Transaction.openNested(this.transaction)) {
                inserted = this.storage.insert(toInsert, nested);
                
                if (action == TransferAction.ACT) {
                    nested.commit();
                }
            }
            
            return inserted;
        }
        
        @Override
        public Long extract(Long toExtract, TransferAction action) {
            if (toExtract <= 0) return blank();
            long extracted;
            
            try (Transaction nested = Transaction.openNested(this.transaction)) {
                extracted = this.storage.extract(toExtract, nested);
                
                if (action == TransferAction.ACT) {
                    nested.commit();
                }
            }
            
            return extracted;
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
    
    private static class EnergyTransferHandlerStorage extends SnapshotParticipant<Object> implements EnergyStorage {
        private final SingleTransferHandler<Long> handler;
        
        public EnergyTransferHandlerStorage(@Nullable SingleTransferHandler<Long> handler) {
            this.handler = handler;
        }
        
        @Override
        public long insert(long maxAmount, TransactionContext transaction) {
            updateSnapshots(transaction);
            return this.handler.insert(maxAmount, TransferAction.ACT);
        }
        
        @Override
        public long extract(long maxAmount, TransactionContext transaction) {
            updateSnapshots(transaction);
            return this.handler.extract(maxAmount, TransferAction.ACT);
        }
        
        @Override
        public long getAmount() {
            return handler.getAmount();
        }
        
        @Override
        public long getCapacity() {
            return handler.getCapacity();
        }
        
        @Override
        protected Object createSnapshot() {
            return this.handler.saveState();
        }
        
        @Override
        protected void readSnapshot(Object snapshot) {
            this.handler.loadState(snapshot);
        }
    }
}
