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

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ArchitecturyPopulator {
    private ArchitecturyPopulator() {}
    
    public static void populate() {
        try {
            populate(Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void populate(Object o) {
        try {
            if (o instanceof Class) {
                Class<?> aClass = (Class<?>) o;
                for (Field field : aClass.getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Populatable.class)) continue;
                    if (Modifier.isStatic(field.getModifiers())) {
                        FieldUtils.removeFinalModifier(field);
                        field.setAccessible(true);
                        String type = field.getType().getName().replace("$", "");
                        Class<?> newClass = Class.forName(type.substring(0, type.lastIndexOf('.')) + "." + Architectury.getModLoader() + "." + type.substring(type.lastIndexOf('.') + 1));
                        field.set(null, newClass.getConstructor().newInstance());
                    }
                }
            } else {
                for (Field field : o.getClass().getDeclaredFields()) {
                    if (!field.isAnnotationPresent(Populatable.class)) continue;
                    if (!Modifier.isStatic(field.getModifiers())) {
                        FieldUtils.removeFinalModifier(field);
                        field.setAccessible(true);
                        String type = field.getType().getName().replace("$", "");
                        Class<?> newClass = Class.forName(type.substring(0, type.lastIndexOf('.')) + "." + Architectury.getModLoader() + "." + type.substring(type.lastIndexOf('.') + 1));
                        field.set(o, newClass.getConstructor().newInstance());
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
