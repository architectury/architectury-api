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

package dev.architectury.transfer.item.util;

import dev.architectury.transfer.ResourceView;
import dev.architectury.transfer.TransferAction;
import dev.architectury.transfer.view.VariantView;
import dev.architectury.transfer.wrapper.single.BaseSingleTransferHandler;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

import static dev.architectury.utils.Amount.toInt;

/**
 * A convenience wrapper for {@link Slot}.<br>
 * Implementations of this class may want to override {@link #set(ItemStack)},
 * if the resource is not an instance of {@link BaseSingleTransferHandler}.
 */
public class TransferHandlerSlot extends Slot {
    protected final Supplier<ResourceView<ItemStack>> viewSupplier;
    
    public TransferHandlerSlot(Supplier<ResourceView<ItemStack>> viewSupplier, int x, int y) {
        super(new SimpleContainer(0), 0, x, y);
        this.viewSupplier = viewSupplier;
    }
    
    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return super.mayPlace(itemStack);
    }
    
    @Override
    public ItemStack getItem() {
        try (var view = viewSupplier.get()) {
            return view.getResource();
        }
    }
    
    @Override
    public void set(ItemStack itemStack) {
        try (var view = viewSupplier.get()) {
            if (view instanceof BaseSingleTransferHandler) {
                ((BaseSingleTransferHandler<ItemStack>) view).setResource(itemStack);
            }
        }
        setChanged();
    }
    
    @Override
    public int getMaxStackSize() {
        try (var view = viewSupplier.get()) {
            return toInt(view.getCapacity());
        }
    }
    
    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        try (var view = viewSupplier.get()) {
            if (view instanceof VariantView) {
                Long capacity = ((VariantView<ItemStack>) view).getCapacityNullable(itemStack);
                return capacity == null ? super.getMaxStackSize(itemStack) : capacity.intValue();
            }
        }
        
        return super.getMaxStackSize(itemStack);
    }
    
    @Override
    public ItemStack remove(int amount) {
        try (var view = viewSupplier.get()) {
            return view.extract(view.copyWithAmount(view.getResource(), amount), TransferAction.ACT);
        }
    }
    
    @Override
    public boolean mayPickup(Player player) {
        try (var view = viewSupplier.get()) {
            return !view.extract(view.getResource(), TransferAction.ACT).isEmpty();
        }
    }
}
