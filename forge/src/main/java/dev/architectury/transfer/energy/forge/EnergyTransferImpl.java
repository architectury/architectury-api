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

package dev.architectury.transfer.energy.forge;

import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.energy.EnergyTransferHandler;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import static dev.architectury.utils.Amount.toInt;

public class EnergyTransferImpl {
    @Nullable
    public static SingleTransferHandler<Long> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof IEnergyStorage) {
            return new ForgeTransferHandler((IEnergyStorage) object);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    @Nullable
    private static Object unwrap(@Nullable SingleTransferHandler<Long> handler) {
        if (handler == null) return null;
        
        if (handler instanceof ForgeTransferHandler) {
            return ((ForgeTransferHandler) handler).getStorage();
        } else {
            return new ArchEnergyStorage(handler);
        }
    }
    
    public static Object platformBlockLookup() {
        return CapabilityEnergy.ENERGY;
    }
    
    private static class ForgeTransferHandler implements EnergyTransferHandler {
        private IEnergyStorage storage;
        
        private ForgeTransferHandler(IEnergyStorage storage) {
            this.storage = storage;
        }
        
        public IEnergyStorage getStorage() {
            return storage;
        }
        
        @Override
        public Long getResource() {
            return (long) storage.getEnergyStored();
        }
        
        @Override
        public long getCapacity() {
            return storage.getMaxEnergyStored();
        }
        
        @Override
        public long insert(Long toInsert, TransferAction action) {
            return storage.extractEnergy(toInsert.intValue(), action == TransferAction.SIMULATE);
        }
        
        @Override
        public Long extract(Long toExtract, TransferAction action) {
            return (long) storage.extractEnergy(toExtract.intValue(), action == TransferAction.SIMULATE);
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
    
    public static class ArchEnergyStorage implements IEnergyStorage {
        private SingleTransferHandler<Long> handler;
        
        public ArchEnergyStorage(SingleTransferHandler<Long> handler) {
            this.handler = handler;
        }
        
        @Override
        public int receiveEnergy(int toInsert, boolean simulate) {
            return toInt(handler.insert((long) toInsert, simulate ? TransferAction.SIMULATE : TransferAction.ACT));
        }
        
        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            return toInt(handler.extract(toExtract, simulate ? TransferAction.SIMULATE : TransferAction.ACT).intValue());
        }
        
        @Override
        public int getEnergyStored() {
            return toInt(handler.getAmount());
        }
        
        @Override
        public int getMaxEnergyStored() {
            return toInt(handler.getCapacity());
        }
        
        @Override
        public boolean canExtract() {
            return true;
        }
        
        @Override
        public boolean canReceive() {
            return true;
        }
    }
}
