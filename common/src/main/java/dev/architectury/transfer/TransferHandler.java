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

import org.jetbrains.annotations.ApiStatus;

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
public interface TransferHandler<T> {
    /**
     * Returns an empty transfer handler, which does nothing.
     *
     * @param <T> the type of resource
     * @return an empty transfer handler
     */
    static <T> TransferHandler<T> empty() {
        return (TransferHandler<T>) EmptyTransferHandler.INSTANCE;
    }
    
    /**
     * Returns the iterable of immutable resources that are currently in the handler.
     *
     * @param context the context of the transfer
     * @return the iterable of resources that are currently in the handler
     */
    Iterable<ResourceView<T>> getResources(TransferContext context);
    
    /**
     * Inserts the given resource into the handler, returning the amount that was inserted.
     *
     * @param toInsert the resource to insert
     * @param action   whether to simulate or actually insert the resource
     * @param context  the context of the transfer
     * @return the amount that was inserted
     */
    long insert(T toInsert, TransferAction action, TransferContext context);
    
    /**
     * Extracts the given resource from the handler, returning the amount that was extracted.
     *
     * @param toExtract the resource to extract
     * @param action    whether to simulate or actually extract the resource
     * @param context   the context of the transfer
     * @return the amount that was extracted
     */
    long extract(T toExtract, TransferAction action, TransferContext context);
}
