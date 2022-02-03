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
import dev.architectury.transfer.access.BlockLookup;
import dev.architectury.transfer.energy.EnergyTransfer;
import dev.architectury.transfer.energy.EnergyTransferHandler;
import dev.architectury.transfer.forge.ForgeBlockLookupRegistration;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

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
    
    public static void init() {
        EnergyTransfer.BLOCK.addQueryHandler(instantiateBlockLookup());
        EnergyTransfer.BLOCK.addRegistrationHandler(ForgeBlockLookupRegistration.create(CapabilityEnergy.ENERGY,
                (level, pos, state, blockEntity) -> (direction, handler) -> new ArchEnergyStorage(handler)));
    }
    
    public static BlockLookup<SingleTransferHandler<Long>, Direction> instantiateBlockLookup() {
        return new BlockLookup<SingleTransferHandler<Long>, Direction>() {
            @Override
            @Nullable
            public SingleTransferHandler<Long> get(Level level, BlockPos pos, Direction direction) {
                return get(level, pos, level.getBlockState(pos), null, direction);
            }
            
            @Override
            @Nullable
            public SingleTransferHandler<Long> get(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction direction) {
                Block block = state.getBlock();
                IEnergyStorage handler = null;
                if (state.hasBlockEntity()) {
                    if (blockEntity == null) {
                        blockEntity = level.getBlockEntity(pos);
                    }
                    if (blockEntity != null) {
                        handler = blockEntity.getCapability(CapabilityEnergy.ENERGY, direction).resolve().orElse(null);
                    }
                }
                return wrap(handler);
            }
        };
    }
    
    private static class ForgeTransferHandler implements EnergyTransferHandler {
        private IEnergyStorage storage;
        
        private ForgeTransferHandler(IEnergyStorage storage) {
            this.storage = storage;
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
            return (int) handler.insert((long) toInsert, simulate ? TransferAction.SIMULATE : TransferAction.ACT);
        }
        
        @Override
        public int extractEnergy(int toExtract, boolean simulate) {
            return handler.extract((long) toExtract, simulate ? TransferAction.SIMULATE : TransferAction.ACT).intValue();
        }
        
        @Override
        public int getEnergyStored() {
            return (int) handler.getAmount();
        }
        
        @Override
        public int getMaxEnergyStored() {
            return (int) handler.getCapacity();
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
