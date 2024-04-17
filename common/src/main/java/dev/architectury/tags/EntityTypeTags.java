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

// AUTO GENERATED CLASS, DO NOT MANUALLY EDIT
package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class EntityTypeTags {
    public static TagKey<EntityType<?>> BOSSES = impl_BOSSES();
    public static TagKey<EntityType<?>> MINECARTS = impl_MINECARTS();
    public static TagKey<EntityType<?>> BOATS = impl_BOATS();
    public static TagKey<EntityType<?>> CAPTURING_NOT_SUPPORTED = impl_CAPTURING_NOT_SUPPORTED();
    public static TagKey<EntityType<?>> TELEPORTING_NOT_SUPPORTED = impl_TELEPORTING_NOT_SUPPORTED();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_BOSSES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_MINECARTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_BOATS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_CAPTURING_NOT_SUPPORTED() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<EntityType<?>> impl_TELEPORTING_NOT_SUPPORTED() {
        throw new AssertionError();
    }
}