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

package dev.architectury.transfer.access.fabric;

import dev.architectury.transfer.access.BlockLookupAccess;
import dev.architectury.transfer.fabric.BlockApiLookupWrapper;
import dev.architectury.transfer.fabric.FabricBlockLookupRegistration;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.Direction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PlatformLookupImpl {
    public static <O, T> void attachBlockQuery(BlockLookupAccess<T, Direction> access, Object lookup, Function<O, T> wrapper) {
        if (!(lookup instanceof BlockApiLookup)) {
            throw new IllegalArgumentException("Lookup must be an instance of BlockApiLookup on Fabric!");
        }
        access.addQueryHandler(new BlockApiLookupWrapper<>((BlockApiLookup<O, Direction>) lookup, wrapper));
    }
    
    public static <O, T> void attachBlockRegistration(BlockLookupAccess<T, Direction> access, Object lookup, BiFunction<T, Direction, O> unwrapper) {
        if (!(lookup instanceof BlockApiLookup)) {
            throw new IllegalArgumentException("Lookup must be an instance of BlockApiLookup on Fabric!");
        }
        access.addRegistrationHandler(FabricBlockLookupRegistration.create((BlockApiLookup<O, Direction>) lookup, unwrapper));
    }
}
