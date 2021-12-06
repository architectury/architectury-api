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

package dev.architectury.transfer;

import dev.architectury.fluid.FluidStack;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.stream.Stream;

/**
 * A handler for transferring resources.
 * This is wrapped around apis given by the platform,
 * <p>
 * <b>DO NOT</b> extend this interface, binary compatibility is not guaranteed
 * with future versions if you do.
 *
 * @param <T> the type of resource
 */
@ApiStatus.NonExtendable
public interface TransferHandler<T> extends TransferView<T> {
    /**
     * Returns an empty item transfer handler, which does nothing.
     *
     * @return an empty item transfer handler
     */
    static TransferHandler<ItemStack> emptyItem() {
        return EmptyTransferHandler.ITEM;
    }
    
    /**
     * Returns an empty fluid transfer handler, which does nothing.
     *
     * @return an empty fluid transfer handler
     */
    static TransferHandler<FluidStack> emptyFluid() {
        return EmptyTransferHandler.FLUID;
    }
    
    /**
     * Returns the iterable of immutable resources that are currently in the handler.<br>
     * <b>Please properly close this stream.</b> Failure to do so will result in a potential
     * crash in conflicting transactions.
     *
     * @return the iterable of resources that are currently in the handler
     */
    Stream<ResourceView<T>> getContents();
    
    /**
     * Returns the size of the handler.
     * This may be extremely expensive to compute, avoid if you can.
     *
     * @return the size of the handler
     */
    @Deprecated
    int getContentsSize();
    
    /**
     * Returns the resource in a particular index.
     * This may be extremely expensive to compute, avoid if you can.
     *
     * @param index the index of the resource
     * @return the resource in the given index
     */
    @Deprecated
    ResourceView<T> getContent(int index);
    
    /**
     * Inserts the given resource into the handler, returning the amount that was inserted.
     *
     * @param toInsert the resource to insert
     * @param action   whether to simulate or actually insert the resource
     * @return the amount that was inserted
     */
    long insert(T toInsert, TransferAction action);
}
