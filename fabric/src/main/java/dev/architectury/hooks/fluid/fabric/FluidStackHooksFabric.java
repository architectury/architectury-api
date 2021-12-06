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

package dev.architectury.hooks.fluid.fabric;

import dev.architectury.fluid.FluidStack;
import dev.architectury.fluid.fabric.FluidStackImpl;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;

public final class FluidStackHooksFabric {
    private FluidStackHooksFabric() {
    }
    
    public static FluidStack fromFabric(StorageView<FluidVariant> storageView) {
        return fromFabric(storageView.getResource(), storageView.getAmount());
    }
    
    public static FluidStack fromFabric(FluidVariant variant, long amount) {
        return FluidStackImpl.fromValue.apply(new FluidStackImpl.Pair(variant, amount));
    }
    
    public static FluidVariant toFabric(FluidStack stack) {
        return ((FluidStackImpl.Pair) FluidStackImpl.toValue.apply(stack)).variant;
    }
}
