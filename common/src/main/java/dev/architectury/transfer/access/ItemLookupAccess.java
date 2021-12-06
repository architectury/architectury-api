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

package dev.architectury.transfer.access;

import dev.architectury.impl.transfer.access.ItemLookupAccessImpl;
import dev.architectury.transfer.ApiLookupAccess;

public interface ItemLookupAccess<T, Context> extends ApiLookupAccess<T, ItemLookup<T, Context>, ItemLookupRegistration<T, Context>>, ItemLookup<T, Context>, ItemLookupRegistration<T, Context> {
    static <T, Context> ItemLookupAccess<T, Context> create() {
        return new ItemLookupAccessImpl<>();
    }
}
