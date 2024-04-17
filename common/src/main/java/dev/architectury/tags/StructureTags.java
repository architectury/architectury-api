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

package dev.architectury.tags;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.PlatformOnly;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class StructureTags {
    public static TagKey<Structure> HIDDEN_FROM_DISPLAYERS = impl_HIDDEN_FROM_DISPLAYERS();
    public static TagKey<Structure> HIDDEN_FROM_LOCATOR_SELECTION = impl_HIDDEN_FROM_LOCATOR_SELECTION();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Structure> impl_HIDDEN_FROM_DISPLAYERS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Structure> impl_HIDDEN_FROM_LOCATOR_SELECTION() {
        throw new AssertionError();
    }
}