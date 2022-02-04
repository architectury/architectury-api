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

package dev.architectury.transfer.energy;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.transfer.access.BlockLookupAccess;
import dev.architectury.transfer.access.PlatformLookup;
import dev.architectury.transfer.wrapper.single.SingleTransferHandler;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public class EnergyTransfer {
    public static final BlockLookupAccess<SingleTransferHandler<Long>, Direction> BLOCK = BlockLookupAccess.create();
    
    static {
        PlatformLookup.attachBlock(BLOCK, platformBlockLookup(), EnergyTransfer::wrap, (handler, direction) -> unwrap(handler));
    }
    
    @ExpectPlatform
    private static Object platformBlockLookup() {
        throw new AssertionError();
    }
    
    /**
     * Wraps a platform-specific item transfer handler into the architectury transfer handler.<br><br>
     * This accepts {@code IEnergyStorage} on Forge.<br>
     * This accepts {@code EnergyStorage} on Fabric.
     *
     * @param object the handler to wrap
     * @return the wrapped handler, or {@code null} if {@code object} is null
     * @throws IllegalArgumentException if {@code object} is not a supported handler
     */
    @ExpectPlatform
    @Nullable
    public static SingleTransferHandler<Long> wrap(@Nullable Object object) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @Nullable
    public static Object unwrap(@Nullable SingleTransferHandler<Long> handler) {
        throw new AssertionError();
    }
}
