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

package dev.architectury.transfer.item.fabric;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.access.BlockTransferAccess;
import dev.architectury.transfer.fabric.FabricBlockTransferAccess;
import dev.architectury.transfer.fabric.FabricStorageTransferHandler;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

public class ItemTransferImpl {
    private static final Function<ItemStack, ItemVariant> TO_FABRIC = ItemVariant::of;
    private static final FabricStorageTransferHandler.FunctionWithAmount<ItemVariant, ItemStack> FROM_FABRIC = (variant, amount) -> variant.toStack((int) amount);
    private static final FabricStorageTransferHandler.FunctionWithAmount<ItemStack, ItemStack> COPY_WITH_AMOUNT = (stack, amount) -> ItemStackHooks.copyWithCount(stack, (int) amount);
    private static final Supplier<ItemStack> BLANK = () -> ItemStack.EMPTY;
    private static final Predicate<ItemStack> IS_EMPTY = ItemStack::isEmpty;
    private static final ToLongFunction<ItemStack> TO_AMOUNT = ItemStack::getCount;
    private static final FabricStorageTransferHandler.TypeAdapter<ItemVariant, ItemStack> TYPE_ADAPTER = new FabricStorageTransferHandler.TypeAdapter<>(TO_FABRIC, FROM_FABRIC, COPY_WITH_AMOUNT, BLANK, IS_EMPTY, TO_AMOUNT);
    
    @Nullable
    public static TransferHandler<ItemStack> wrap(@Nullable Object object) {
        if (object == null) return null;
        
        if (object instanceof Storage) {
            return new FabricStorageTransferHandler<>((Storage) object, null, TYPE_ADAPTER);
        } else if (object instanceof ContainerItemContext) {
            return new FabricContainerItemTransferHandler((ContainerItemContext) object, null);
        } else {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }
    
    public static BlockTransferAccess<TransferHandler<ItemStack>, Direction> instantiateBlockAccess() {
        return new FabricBlockTransferAccess<>(ItemStorage.SIDED, ItemTransferImpl::wrap);
    }
}
