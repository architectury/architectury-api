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

package dev.architectury.transfer;

import com.google.common.base.Predicates;
import dev.architectury.transfer.wrapper.filtering.FilteringTransferHandler;
import dev.architectury.transfer.wrapper.filtering.FilteringTransferView;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Predicate;

public interface TransferView<T> {
    /**
     * Inserts the given resource into the handler, returning the amount that was inserted.
     *
     * @param toInsert the resource to insert
     * @param action   whether to simulate or actually insert the resource
     * @return the amount that was inserted
     */
    long insert(T toInsert, TransferAction action);
    
    /**
     * Extracts the given resource from the handler, returning the stack that was extracted.
     *
     * @param toExtract the resource to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    T extract(T toExtract, TransferAction action);
    
    /**
     * Extracts the given resource from the handler, returning the stack that was extracted.
     *
     * @param toExtract the predicates to use to filter the resources to extract
     * @param maxAmount the maximum amount of resources to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    T extract(Predicate<T> toExtract, long maxAmount, TransferAction action);
    
    /**
     * Extracts the any resource from the handler, returning the stack that was extracted.
     *
     * @param maxAmount the maximum amount of resources to extract
     * @param action    whether to simulate or actually extract the resource
     * @return the stack that was extracted
     */
    default T extract(long maxAmount, TransferAction action) {
        return extract(Predicates.alwaysTrue(), maxAmount, action);
    }
    
    
    /**
     * Returns a blank resource.
     *
     * @return a blank resource
     */
    T blank();
    
    /**
     * Returns a copy of a resource with the given amount.
     *
     * @param resource the resource to copy
     * @param amount   the amount to copy
     * @return the copy
     */
    T copyWithAmount(T resource, long amount);
    
    /**
     * Returns the saved state of the handler, this method must not be called by the implementation.
     * This method is used to provide support for transactions, which is only used if the handler is
     * registered with the lookup.
     *
     * @return the saved state of the handler
     * @throws UnsupportedOperationException if the handler is provided by the platform
     */
    @ApiStatus.OverrideOnly
    Object saveState();
    
    /**
     * Loads the saved state of the handler, this method must not be called by the implementation.
     * This method is used to provide support for transactions, which is only used if the handler is
     * registered with the lookup.
     *
     * @param state the saved state of the handler
     * @throws UnsupportedOperationException if the handler is provided by the platform
     */
    @ApiStatus.OverrideOnly
    void loadState(Object state);
    
    default TransferView<T> unmodifiable() {
        return filter(Predicates.alwaysFalse());
    }
    
    default TransferView<T> onlyInsert() {
        return filter(Predicates.alwaysTrue(), Predicates.alwaysFalse());
    }
    
    default TransferView<T> onlyExtract() {
        return filter(Predicates.alwaysFalse(), Predicates.alwaysTrue());
    }
    
    default TransferView<T> filter(Predicate<T> filter) {
        return filter(filter, filter);
    }
    
    default TransferView<T> filter(Predicate<T> insert, Predicate<T> extract) {
        return FilteringTransferView.of(this, insert, extract);
    }
}
