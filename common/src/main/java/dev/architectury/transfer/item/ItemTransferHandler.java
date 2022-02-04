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

package dev.architectury.transfer.item;

import dev.architectury.hooks.item.ItemStackHooks;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.view.VariantView;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * This is a convenience class that implements methods for {@link ItemStack}s.
 */
public interface ItemTransferHandler extends ItemTransferView, TransferHandler<ItemStack>, VariantView<ItemStack> {
    @Override
    default long getAmount(ItemStack resource) {
        return resource.getCount();
    }
    
    @Override
    @Nullable
    default Long getCapacityNullable(ItemStack resource) {
        return (long) resource.getMaxStackSize();
    }
    
    @Override
    default boolean isSameVariant(ItemStack first, ItemStack second) {
        return ItemStackHooks.isStackable(first, second);
    }
}
