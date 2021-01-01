/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.registry;

import com.google.common.base.Predicates;
import me.shedaniel.architectury.ExpectPlatform;
import me.shedaniel.architectury.hooks.biome.BiomeProperties;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public final class BiomeModifications {
    public static void addProperties(BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModifications.addProperties(Predicates.alwaysTrue(), modifier);
    }
    
    @ExpectPlatform
    public static void addProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        throw new AssertionError();
    }
    
    public static void postProcessProperties(BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModifications.postProcessProperties(Predicates.alwaysTrue(), modifier);
    }
    
    @ExpectPlatform
    public static void postProcessProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        throw new AssertionError();
    }
    
    public static void removeProperties(BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModifications.removeProperties(Predicates.alwaysTrue(), modifier);
    }
    
    @ExpectPlatform
    public static void removeProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        throw new AssertionError();
    }
    
    public static void replaceProperties(BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        BiomeModifications.replaceProperties(Predicates.alwaysTrue(), modifier);
    }
    
    @ExpectPlatform
    public static void replaceProperties(Predicate<BiomeContext> predicate, BiConsumer<BiomeContext, BiomeProperties.Mutable> modifier) {
        throw new AssertionError();
    }
    
    public interface BiomeContext {
        @NotNull
        ResourceLocation getKey();
        
        @NotNull
        BiomeProperties getProperties();
    }
}
