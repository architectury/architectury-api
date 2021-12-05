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

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import org.jetbrains.annotations.Nullable;

/**
 * Transfer context is used to defer the state of the transaction.
 * <p>
 * On Fabric, each thread can only have one transaction at a time,
 * you can create a new context with {@link #create()}, this will instantiate
 * a Transaction on Fabric.
 * <p>
 * If you wish to create a context with a transaction, you can use
 * {@link #create(Object)} with the transaction object, this method
 * is only available on Fabric.
 * <p>
 * This class must be closed with {@link #close()}, you can use try-and-resources
 * block to ensure that the context is closed.
 */
public interface TransferContext extends AutoCloseable {
    @ExpectPlatform
    static TransferContext create() {
        throw new AssertionError();
    }
    
    @PlatformOnly(PlatformOnly.FABRIC)
    @ExpectPlatform
    static TransferContext create(@Nullable Object transaction) {
        throw new AssertionError();
    }
    
    int nestingDepth();
}
