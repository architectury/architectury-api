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

package dev.architectury.transfer.fluid;

import dev.architectury.fluid.FluidStack;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.transfer.TransferHandler;
import dev.architectury.transfer.access.BlockTransferAccess;
import dev.architectury.transfer.access.ItemTransferAccess;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class FluidTransfer {
    private FluidTransfer() {
    }
    
    public static final BlockTransferAccess<TransferHandler<FluidStack>, Direction> BLOCK = instantiateBlockAccess();
    public static final ItemTransferAccess<TransferHandler<FluidStack>, TransferHandler<ItemStack>> ITEM = instantiateItemAccess();
    
    @ExpectPlatform
    private static BlockTransferAccess<TransferHandler<FluidStack>, Direction> instantiateBlockAccess() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static ItemTransferAccess<TransferHandler<FluidStack>, TransferHandler<ItemStack>> instantiateItemAccess() {
        throw new AssertionError();
    }
    
    /**
     * Wraps a platform-specific fluid transfer handler into the architectury transfer handler.
     * This accepts {@code IFluidHandler} on Forge.
     * This accepts {@code Storage<FluidVariant>} on Fabric.
     *
     * @param object the handler to wrap
     * @return the wrapped handler, or {@code null} if {@code object} is null
     * @throws IllegalArgumentException if {@code object} is not a supported handler
     */
    @ExpectPlatform
    @Nullable
    public static TransferHandler<FluidStack> wrap(@Nullable Object object) {
        throw new AssertionError();
    }
}
