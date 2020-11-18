/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury;

import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@ApiStatus.Internal
public class PlatformMethods {
    public static CallSite platform(MethodHandles.Lookup lookup, String name, MethodType type)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        Class<?> lookupClass = lookup.lookupClass();
        String lookupType = lookupClass.getName().replace("$", "") + "Impl";
        Class<?> newClass = Class.forName(lookupType.substring(0, lookupType.lastIndexOf('.')) + "." + Architectury.getModLoader() + "." + lookupType.substring(lookupType.lastIndexOf('.') + 1), false, lookupClass.getClassLoader());
        return new ConstantCallSite(lookup.findStatic(newClass, name, type));
    }
}
