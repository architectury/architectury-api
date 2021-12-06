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

package dev.architectury.transfer.fabric;

import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.TransferHandler;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.Iterator;
import java.util.stream.Stream;

public class TransferHandlerStorage<F, S> extends SnapshotParticipant<Object> implements Storage<F> {
    private final TransferHandler<S> handler;
    private final FabricStorageTransferHandler.TypeAdapter<F, S> typeAdapter;
    
    public TransferHandlerStorage(TransferHandler<S> handler, FabricStorageTransferHandler.TypeAdapter<F, S> typeAdapter) {
        this.handler = handler;
        this.typeAdapter = typeAdapter;
    }
    
    @Override
    public Iterator<StorageView<F>> iterator(TransactionContext transaction) {
        Stream<StorageView<F>> stream = this.handler.getContents()
                .map(view -> new FabricStorageView<F, S>(view, typeAdapter));
        transaction.addCloseCallback((t, result) -> {
            stream.close();
        });
        return stream.iterator();
    }
    
    @Override
    public long insert(F resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        return this.handler.insert(typeAdapter.fromFabric.apply(resource, maxAmount), TransferAction.ACT);
    }
    
    @Override
    public long extract(F resource, long maxAmount, TransactionContext transaction) {
        updateSnapshots(transaction);
        S extracted = this.handler.extract(typeAdapter.fromFabric.apply(resource, maxAmount), TransferAction.ACT);
        return typeAdapter.toAmount.applyAsLong(extracted);
    }
    
    @Override
    protected Object createSnapshot() {
        return this.handler.saveState();
    }
    
    @Override
    protected void readSnapshot(Object snapshot) {
        this.handler.loadState(snapshot);
    }
    
    private static class FabricStorageView<F, S> extends SnapshotParticipant<Object> implements StorageView<F> {
        private final ResourceView<S> storage;
        private final FabricStorageTransferHandler.TypeAdapter<F, S> typeAdapter;
        
        private FabricStorageView(ResourceView<S> view, FabricStorageTransferHandler.TypeAdapter<F, S> typeAdapter) {
            this.storage = view;
            this.typeAdapter = typeAdapter;
        }
        
        @Override
        public long extract(F resource, long maxAmount, TransactionContext transaction) {
            updateSnapshots(transaction);
            S extracted = this.storage.extract(typeAdapter.fromFabric.apply(resource, maxAmount), TransferAction.ACT);
            return typeAdapter.toAmount.applyAsLong(extracted);
        }
        
        @Override
        public boolean isResourceBlank() {
            return getAmount() <= 0;
        }
        
        @Override
        public F getResource() {
            return typeAdapter.toFabric.apply(storage.getResource());
        }
        
        @Override
        public long getAmount() {
            return typeAdapter.toAmount.applyAsLong(storage.getResource());
        }
        
        @Override
        public long getCapacity() {
            return storage.getCapacity();
        }
        
        @Override
        protected Object createSnapshot() {
            return storage.saveState();
        }
        
        @Override
        protected void readSnapshot(Object snapshot) {
            storage.loadState(snapshot);
        }
    }
}
