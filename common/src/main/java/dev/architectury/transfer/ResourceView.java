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

import java.util.function.Predicate;

/**
 * Represents an <b>immutable</b> view of a resource.
 *
 * @param <T> the type of resource
 */
public interface ResourceView<T> extends TransferView<T> {
    /**
     * Returns the resource that this view represents.
     * The returned resource is <b>immutable</b>.
     *
     * @return the resource
     */
    T getResource();
    
    /**
     * Returns the capacity of this view.
     *
     * @return the capacity
     */
    long getCapacity();
    
    /**
     * Returns a copy of a resource with the given amount.
     *
     * @param resource the resource to copy
     * @param amount   the amount to copy
     * @return the copy
     */
    T copyWithAmount(T resource, long amount);
    
    @Override
    default T extract(Predicate<T> toExtract, long maxAmount, TransferAction action) {
        if (toExtract.test(getResource())) {
            return extract(copyWithAmount(getResource(), maxAmount), action);
        }
        
        return blank();
    }
}
