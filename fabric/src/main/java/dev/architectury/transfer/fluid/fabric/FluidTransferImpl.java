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

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.fabric.BlockApiLookupWrapper;
import dev.architectury.transfer.fabric.FabricBlockLookupRegistration;
import dev.architectury.transfer.fabric.FabricStorageTransferHandler;
import dev.architectury.transfer.fabric.TransferHandlerStorage;
import dev.architectury.transfer.fluid.FluidTransfer;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

public class FluidTransferImpl {
    private static final Function<FluidStack, FluidVariant> TO_FABRIC = FluidStackHooksFabric::toFabric;
    private static final FabricStorageTransferHandler.FunctionWithAmount<FluidVariant, FluidStack> FROM_FABRIC = FluidStackHooksFabric::fromFabric;
    private static final FabricStorageTransferHandler.FunctionWithAmount<FluidStack, FluidStack> COPY_WITH_AMOUNT = FluidStack::copyWithAmount;
    private static final Supplier<FluidStack> BLANK = FluidStack::empty;
    private static final Predicate<FluidStack> IS_EMPTY = FluidStack::isEmpty;
    private static final ToLongFunction<FluidStack> TO_AMOUNT = FluidStack::getAmount;
    private static final FabricStorageTransferHandler.TypeAdapter<FluidVariant, FluidStack> TYPE_ADAPTER = new FabricStorageTransferHandler.TypeAdapter<>(TO_FABRIC, FROM_FABRIC, COPY_WITH_AMOUNT, BLANK, IS_EMPTY, TO_AMOUNT);
    
    @Nullable
    public static TransferHandler<FluidStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof Storage) {
            return new FabricStorageTransferHandler<>((Storage) object, null, TYPE_ADAPTER);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    @Nullable
    public static Storage<FluidVariant> unwrap(@Nullable TransferHandler<FluidStack> handler) {
        if (handler == null) return null;
        
        if (handler instanceof FabricStorageTransferHandler) {
            return ((FabricStorageTransferHandler) handler).getStorage();
        } else {
            return new TransferHandlerStorage<>(handler, TYPE_ADAPTER);
        }
    }
    
    public static void init() {
        FluidTransfer.BLOCK.addQueryHandler(new BlockApiLookupWrapper<>(FluidStorage.SIDED, FluidTransferImpl::wrap));
        FluidTransfer.BLOCK.addRegistrationHandler(FabricBlockLookupRegistration.create(FluidStorage.SIDED, FluidTransferImpl::unwrap));
//        FluidTransfer.ITEM.addQueryHandler((stack, context) -> {
//            return wrap(FluidStorage.ITEM.find(stack, fromTransfer(stack, context)));
//        });
    }
    
    public static ContainerItemContext fromTransfer(ItemStack stack, TransferHandler<ItemStack> transferHandler) {
        SingleSlotStorage<ItemVariant> mainSlot = new SingleVariantStorage<ItemVariant>() {
            @Override
            protected ItemVariant getBlankVariant() {
                return ItemVariant.blank();
            }
            
            @Override
            protected long getCapacity(ItemVariant variant) {
                // TODO Revisit this
                return variant.getItem().getMaxStackSize();
            }
        };
        return new ContainerItemContext() {
            @Override
            public SingleSlotStorage<ItemVariant> getMainSlot() {
                return mainSlot;
            }
            
            @Override
            public List<SingleSlotStorage<ItemVariant>> getAdditionalSlots() {
                return null;
            }
            
            @Override
            public long insertOverflow(ItemVariant itemVariant, long maxAmount, TransactionContext transactionContext) {
                return 0;
            }
        };
    }
}
