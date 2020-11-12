/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
