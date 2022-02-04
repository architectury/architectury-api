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

package dev.architectury.transfer.access;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Direction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class PlatformLookup {
    public static <O, T> void attachBlock(BlockLookupAccess<T, Direction> access, Object lookup, Function<O, T> wrapper, BiFunction<T, Direction, O> unwrapper) {
        attachBlockQuery(access, lookup, wrapper);
        attachBlockRegistration(access, lookup, unwrapper);
    }
    
    /**
     * Attaches a block query to the given lookup.<br><br>
     * Lookup accepts {@code Capability<O>} on Forge.<br>
     * Lookup accepts {@code BlockApiLookup<O, Direction>} on Fabric.
     * <br><br>
     * This method allows the lookup to <b>read</b> the lookup object, while converting
     * it to the platform-less abstracted type {@code T}.
     *
     * @param access  the access to attach to
     * @param lookup  the platform lookup object
     * @param wrapper the wrapper function, to convert the platform object to the abstracted object
     * @param <O>     the platform object type
     * @param <T>     the abstracted object type
     */
    @ExpectPlatform
    public static <O, T> void attachBlockQuery(BlockLookupAccess<T, Direction> access, Object lookup, Function<O, T> wrapper) {
        throw new AssertionError();
    }
    
    /**
     * Attaches a block registration handler to the given lookup.<br><br>
     * Lookup accepts {@code Capability<O>} on Forge.<br>
     * Lookup accepts {@code BlockApiLookup<O, Direction>} on Fabric.
     * <br><br>
     * This method allows other mods on the platform to look up the abstracted object, by
     * converting the abstracted object to the platform object.
     *
     * @param access    the access to attach to
     * @param lookup    the platform lookup object
     * @param unwrapper the unwrapper function, to convert the abstracted object to the platform object
     * @param <O>       the platform object type
     * @param <T>       the abstracted object type
     */
    @ExpectPlatform
    public static <O, T> void attachBlockRegistration(BlockLookupAccess<T, Direction> access, Object lookup, BiFunction<T, Direction, O> unwrapper) {
        throw new AssertionError();
    }
}
