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

package me.shedaniel.architectury;

import dev.architectury.injectables.targets.ArchitecturyTarget;
import me.shedaniel.architectury.utils.PlatformExpectedError;
import org.jetbrains.annotations.ApiStatus;

import java.lang.invoke.*;

@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0")
@ApiStatus.Internal
public class PlatformMethods {
    public static CallSite platform(MethodHandles.Lookup lookup, String name, MethodType type) {
        Class<?> lookupClass = lookup.lookupClass();
        String lookupType = lookupClass.getName().replace("$", "") + "Impl";
        
        String platformExpectedClass = lookupType.substring(0, lookupType.lastIndexOf('.')) + "." + ArchitecturyTarget.getCurrentTarget() + "." +
                lookupType.substring(lookupType.lastIndexOf('.') + 1);
        Class<?> newClass;
        try {
            newClass = Class.forName(platformExpectedClass, false, lookupClass.getClassLoader());
        } catch (ClassNotFoundException exception) {
            throw new PlatformExpectedError(lookupClass.getName() + "#" + name + " expected platform implementation in " + platformExpectedClass +
                    "#" + name + ", but the class doesn't exist!", exception);
        }
        MethodHandle platformMethod;
        try {
            platformMethod = lookup.findStatic(newClass, name, type);
        } catch (NoSuchMethodException exception) {
            throw new PlatformExpectedError(lookupClass.getName() + "#" + name + " expected platform implementation in " + platformExpectedClass +
                    "#" + name + ", but the method doesn't exist!", exception);
        } catch (IllegalAccessException exception) {
            throw new PlatformExpectedError(lookupClass.getName() + "#" + name + " expected platform implementation in " + platformExpectedClass +
                    "#" + name + ", but the method's modifier doesn't match the access requirements!", exception);
        }
        return new ConstantCallSite(platformMethod);
    }
}
