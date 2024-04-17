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
import net.minecraft.world.level.biome.Biome;

// Only available on Fabric and NeoForge
@SuppressWarnings({"UnimplementedExpectPlatform", "unused"})
public class BiomeTags {
    public static TagKey<Biome> NO_DEFAULT_MONSTERS = impl_NO_DEFAULT_MONSTERS();
    public static TagKey<Biome> HIDDEN_FROM_LOCATOR_SELECTION = impl_HIDDEN_FROM_LOCATOR_SELECTION();
    public static TagKey<Biome> IS_VOID = impl_IS_VOID();
    public static TagKey<Biome> IS_OVERWORLD = impl_IS_OVERWORLD();
    public static TagKey<Biome> IS_HOT = impl_IS_HOT();
    public static TagKey<Biome> IS_HOT_OVERWORLD = impl_IS_HOT_OVERWORLD();
    public static TagKey<Biome> IS_HOT_NETHER = impl_IS_HOT_NETHER();
    // Fabric only
    public static TagKey<Biome> IS_TEMPERATE = impl_IS_TEMPERATE();
    // Fabric only
    public static TagKey<Biome> IS_TEMPERATE_OVERWORLD = impl_IS_TEMPERATE_OVERWORLD();
    public static TagKey<Biome> IS_COLD = impl_IS_COLD();
    public static TagKey<Biome> IS_COLD_OVERWORLD = impl_IS_COLD_OVERWORLD();
    public static TagKey<Biome> IS_COLD_END = impl_IS_COLD_END();
    public static TagKey<Biome> IS_WET = impl_IS_WET();
    public static TagKey<Biome> IS_WET_OVERWORLD = impl_IS_WET_OVERWORLD();
    public static TagKey<Biome> IS_DRY = impl_IS_DRY();
    public static TagKey<Biome> IS_DRY_OVERWORLD = impl_IS_DRY_OVERWORLD();
    public static TagKey<Biome> IS_DRY_NETHER = impl_IS_DRY_NETHER();
    public static TagKey<Biome> IS_DRY_END = impl_IS_DRY_END();
    public static TagKey<Biome> IS_SPARSE_VEGETATION = impl_IS_SPARSE_VEGETATION();
    public static TagKey<Biome> IS_SPARSE_VEGETATION_OVERWORLD = impl_IS_SPARSE_VEGETATION_OVERWORLD();
    public static TagKey<Biome> IS_DENSE_VEGETATION = impl_IS_DENSE_VEGETATION();
    public static TagKey<Biome> IS_DENSE_VEGETATION_OVERWORLD = impl_IS_DENSE_VEGETATION_OVERWORLD();
    public static TagKey<Biome> IS_TREE_CONIFEROUS = impl_IS_TREE_CONIFEROUS();
    public static TagKey<Biome> IS_TREE_SAVANNA = impl_IS_TREE_SAVANNA();
    public static TagKey<Biome> IS_TREE_JUNGLE = impl_IS_TREE_JUNGLE();
    public static TagKey<Biome> IS_TREE_DECIDUOUS = impl_IS_TREE_DECIDUOUS();
    public static TagKey<Biome> IS_MOUNTAIN = impl_IS_MOUNTAIN();
    public static TagKey<Biome> IS_MOUNTAIN_PEAK = impl_IS_MOUNTAIN_PEAK();
    public static TagKey<Biome> IS_MOUNTAIN_SLOPE = impl_IS_MOUNTAIN_SLOPE();
    public static TagKey<Biome> IS_PLAINS = impl_IS_PLAINS();
    public static TagKey<Biome> IS_SNOWY_PLAINS = impl_IS_SNOWY_PLAINS();
    public static TagKey<Biome> IS_FOREST = impl_IS_FOREST();
    public static TagKey<Biome> IS_BIRCH_FOREST = impl_IS_BIRCH_FOREST();
    public static TagKey<Biome> IS_FLOWER_FOREST = impl_IS_FLOWER_FOREST();
    public static TagKey<Biome> IS_TAIGA = impl_IS_TAIGA();
    public static TagKey<Biome> IS_OLD_GROWTH = impl_IS_OLD_GROWTH();
    public static TagKey<Biome> IS_HILL = impl_IS_HILL();
    public static TagKey<Biome> IS_WINDSWEPT = impl_IS_WINDSWEPT();
    public static TagKey<Biome> IS_JUNGLE = impl_IS_JUNGLE();
    public static TagKey<Biome> IS_SAVANNA = impl_IS_SAVANNA();
    public static TagKey<Biome> IS_SWAMP = impl_IS_SWAMP();
    public static TagKey<Biome> IS_DESERT = impl_IS_DESERT();
    public static TagKey<Biome> IS_BADLANDS = impl_IS_BADLANDS();
    public static TagKey<Biome> IS_BEACH = impl_IS_BEACH();
    public static TagKey<Biome> IS_STONY_SHORES = impl_IS_STONY_SHORES();
    public static TagKey<Biome> IS_MUSHROOM = impl_IS_MUSHROOM();
    public static TagKey<Biome> IS_RIVER = impl_IS_RIVER();
    public static TagKey<Biome> IS_OCEAN = impl_IS_OCEAN();
    public static TagKey<Biome> IS_DEEP_OCEAN = impl_IS_DEEP_OCEAN();
    public static TagKey<Biome> IS_SHALLOW_OCEAN = impl_IS_SHALLOW_OCEAN();
    public static TagKey<Biome> IS_UNDERGROUND = impl_IS_UNDERGROUND();
    public static TagKey<Biome> IS_CAVE = impl_IS_CAVE();
    public static TagKey<Biome> IS_WASTELAND = impl_IS_WASTELAND();
    public static TagKey<Biome> IS_DEAD = impl_IS_DEAD();
    public static TagKey<Biome> IS_FLORAL = impl_IS_FLORAL();
    public static TagKey<Biome> IS_SNOWY = impl_IS_SNOWY();
    public static TagKey<Biome> IS_ICY = impl_IS_ICY();
    public static TagKey<Biome> IS_AQUATIC = impl_IS_AQUATIC();
    public static TagKey<Biome> IS_AQUATIC_ICY = impl_IS_AQUATIC_ICY();
    public static TagKey<Biome> IS_NETHER = impl_IS_NETHER();
    public static TagKey<Biome> IS_NETHER_FOREST = impl_IS_NETHER_FOREST();
    public static TagKey<Biome> IS_END = impl_IS_END();
    public static TagKey<Biome> IS_OUTER_END_ISLAND = impl_IS_OUTER_END_ISLAND();
    // NeoForge only
    public static TagKey<Biome> IS_HOT_END = impl_IS_HOT_END();
    // NeoForge only
    public static TagKey<Biome> IS_COLD_NETHER = impl_IS_COLD_NETHER();
    // NeoForge only
    public static TagKey<Biome> IS_SPARSE_VEGETATION_NETHER = impl_IS_SPARSE_VEGETATION_NETHER();
    // NeoForge only
    public static TagKey<Biome> IS_SPARSE_VEGETATION_END = impl_IS_SPARSE_VEGETATION_END();
    // NeoForge only
    public static TagKey<Biome> IS_DENSE_VEGETATION_NETHER = impl_IS_DENSE_VEGETATION_NETHER();
    // NeoForge only
    public static TagKey<Biome> IS_DENSE_VEGETATION_END = impl_IS_DENSE_VEGETATION_END();
    // NeoForge only
    public static TagKey<Biome> IS_WET_NETHER = impl_IS_WET_NETHER();
    // NeoForge only
    public static TagKey<Biome> IS_WET_END = impl_IS_WET_END();
    // NeoForge only
    public static TagKey<Biome> IS_LUSH = impl_IS_LUSH();
    // NeoForge only
    public static TagKey<Biome> IS_MAGICAL = impl_IS_MAGICAL();
    // NeoForge only
    public static TagKey<Biome> IS_RARE = impl_IS_RARE();
    // NeoForge only
    public static TagKey<Biome> IS_PLATEAU = impl_IS_PLATEAU();
    // NeoForge only
    public static TagKey<Biome> IS_MODIFIED = impl_IS_MODIFIED();
    // NeoForge only
    public static TagKey<Biome> IS_SPOOKY = impl_IS_SPOOKY();
    // NeoForge only
    public static TagKey<Biome> IS_SANDY = impl_IS_SANDY();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_NO_DEFAULT_MONSTERS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_HIDDEN_FROM_LOCATOR_SELECTION() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_VOID() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_HOT() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_HOT_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_HOT_NETHER() {
        throw new AssertionError();
    }
    // Returns null on NeoForge
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TEMPERATE() {
        throw new AssertionError();
    }
    // Returns null on NeoForge
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TEMPERATE_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_COLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_COLD_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_COLD_END() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WET() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WET_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DRY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DRY_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DRY_NETHER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DRY_END() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SPARSE_VEGETATION() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SPARSE_VEGETATION_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DENSE_VEGETATION() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DENSE_VEGETATION_OVERWORLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TREE_CONIFEROUS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TREE_SAVANNA() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TREE_JUNGLE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TREE_DECIDUOUS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MOUNTAIN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MOUNTAIN_PEAK() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MOUNTAIN_SLOPE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_PLAINS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SNOWY_PLAINS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_FOREST() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_BIRCH_FOREST() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_FLOWER_FOREST() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_TAIGA() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_OLD_GROWTH() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_HILL() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WINDSWEPT() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_JUNGLE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SAVANNA() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SWAMP() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DESERT() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_BADLANDS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_BEACH() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_STONY_SHORES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MUSHROOM() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_RIVER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_OCEAN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DEEP_OCEAN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SHALLOW_OCEAN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_UNDERGROUND() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_CAVE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WASTELAND() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DEAD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_FLORAL() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SNOWY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_ICY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_AQUATIC() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_AQUATIC_ICY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_NETHER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_NETHER_FOREST() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_END() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_OUTER_END_ISLAND() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_HOT_END() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_COLD_NETHER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SPARSE_VEGETATION_NETHER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SPARSE_VEGETATION_END() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DENSE_VEGETATION_NETHER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_DENSE_VEGETATION_END() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WET_NETHER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_WET_END() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_LUSH() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MAGICAL() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_RARE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_PLATEAU() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_MODIFIED() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SPOOKY() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Biome> impl_IS_SANDY() {
        throw new AssertionError();
    }
}