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
import net.minecraft.world.level.block.Block;

/**
  * Convention Tags for blocks.<br>
  * <b style="color:red;">WARNING! This class will not work on Forge!</b>
  * @see net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags
  * @see net.neoforged.neoforge.common.Tags.Blocks
  */
@SuppressWarnings("unused")
public class BlockTags {
    public static TagKey<Block> STONES = impl_STONES();
    public static TagKey<Block> COBBLESTONES = impl_COBBLESTONES();
    public static TagKey<Block> ORES = impl_ORES();
    public static TagKey<Block> ORES_QUARTZ = impl_ORES_QUARTZ();
    public static TagKey<Block> ORES_NETHERITE_SCRAP = impl_ORES_NETHERITE_SCRAP();
    public static TagKey<Block> BARRELS = impl_BARRELS();
    public static TagKey<Block> BARRELS_WOODEN = impl_BARRELS_WOODEN();
    public static TagKey<Block> BOOKSHELVES = impl_BOOKSHELVES();
    public static TagKey<Block> CHESTS = impl_CHESTS();
    public static TagKey<Block> CHESTS_WOODEN = impl_CHESTS_WOODEN();
    public static TagKey<Block> GLASS_BLOCKS = impl_GLASS_BLOCKS();
    public static TagKey<Block> GLASS_PANES = impl_GLASS_PANES();
    // Fabric only
    public static TagKey<Block> SHULKER_BOXES = impl_SHULKER_BOXES();
    public static TagKey<Block> BUDDING_BLOCKS = impl_BUDDING_BLOCKS();
    public static TagKey<Block> BUDS = impl_BUDS();
    public static TagKey<Block> CLUSTERS = impl_CLUSTERS();
    public static TagKey<Block> VILLAGER_JOB_SITES = impl_VILLAGER_JOB_SITES();
    public static TagKey<Block> SANDSTONE_BLOCKS = impl_SANDSTONE_BLOCKS();
    public static TagKey<Block> SANDSTONE_SLABS = impl_SANDSTONE_SLABS();
    public static TagKey<Block> SANDSTONE_STAIRS = impl_SANDSTONE_STAIRS();
    public static TagKey<Block> SANDSTONE_RED_BLOCKS = impl_SANDSTONE_RED_BLOCKS();
    public static TagKey<Block> SANDSTONE_RED_SLABS = impl_SANDSTONE_RED_SLABS();
    public static TagKey<Block> SANDSTONE_RED_STAIRS = impl_SANDSTONE_RED_STAIRS();
    public static TagKey<Block> SANDSTONE_UNCOLORED_BLOCKS = impl_SANDSTONE_UNCOLORED_BLOCKS();
    public static TagKey<Block> SANDSTONE_UNCOLORED_SLABS = impl_SANDSTONE_UNCOLORED_SLABS();
    public static TagKey<Block> SANDSTONE_UNCOLORED_STAIRS = impl_SANDSTONE_UNCOLORED_STAIRS();
    public static TagKey<Block> DYED = impl_DYED();
    public static TagKey<Block> DYED_BLACK = impl_DYED_BLACK();
    public static TagKey<Block> DYED_BLUE = impl_DYED_BLUE();
    public static TagKey<Block> DYED_BROWN = impl_DYED_BROWN();
    public static TagKey<Block> DYED_CYAN = impl_DYED_CYAN();
    public static TagKey<Block> DYED_GRAY = impl_DYED_GRAY();
    public static TagKey<Block> DYED_GREEN = impl_DYED_GREEN();
    public static TagKey<Block> DYED_LIGHT_BLUE = impl_DYED_LIGHT_BLUE();
    public static TagKey<Block> DYED_LIGHT_GRAY = impl_DYED_LIGHT_GRAY();
    public static TagKey<Block> DYED_LIME = impl_DYED_LIME();
    public static TagKey<Block> DYED_MAGENTA = impl_DYED_MAGENTA();
    public static TagKey<Block> DYED_ORANGE = impl_DYED_ORANGE();
    public static TagKey<Block> DYED_PINK = impl_DYED_PINK();
    public static TagKey<Block> DYED_PURPLE = impl_DYED_PURPLE();
    public static TagKey<Block> DYED_RED = impl_DYED_RED();
    public static TagKey<Block> DYED_WHITE = impl_DYED_WHITE();
    public static TagKey<Block> DYED_YELLOW = impl_DYED_YELLOW();
    public static TagKey<Block> STORAGE_BLOCKS = impl_STORAGE_BLOCKS();
    public static TagKey<Block> STORAGE_BLOCKS_BONE_MEAL = impl_STORAGE_BLOCKS_BONE_MEAL();
    public static TagKey<Block> STORAGE_BLOCKS_COAL = impl_STORAGE_BLOCKS_COAL();
    public static TagKey<Block> STORAGE_BLOCKS_COPPER = impl_STORAGE_BLOCKS_COPPER();
    public static TagKey<Block> STORAGE_BLOCKS_DIAMOND = impl_STORAGE_BLOCKS_DIAMOND();
    public static TagKey<Block> STORAGE_BLOCKS_DRIED_KELP = impl_STORAGE_BLOCKS_DRIED_KELP();
    public static TagKey<Block> STORAGE_BLOCKS_EMERALD = impl_STORAGE_BLOCKS_EMERALD();
    public static TagKey<Block> STORAGE_BLOCKS_GOLD = impl_STORAGE_BLOCKS_GOLD();
    public static TagKey<Block> STORAGE_BLOCKS_IRON = impl_STORAGE_BLOCKS_IRON();
    public static TagKey<Block> STORAGE_BLOCKS_LAPIS = impl_STORAGE_BLOCKS_LAPIS();
    public static TagKey<Block> STORAGE_BLOCKS_NETHERITE = impl_STORAGE_BLOCKS_NETHERITE();
    public static TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = impl_STORAGE_BLOCKS_RAW_COPPER();
    public static TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = impl_STORAGE_BLOCKS_RAW_GOLD();
    public static TagKey<Block> STORAGE_BLOCKS_RAW_IRON = impl_STORAGE_BLOCKS_RAW_IRON();
    public static TagKey<Block> STORAGE_BLOCKS_REDSTONE = impl_STORAGE_BLOCKS_REDSTONE();
    public static TagKey<Block> STORAGE_BLOCKS_SLIME = impl_STORAGE_BLOCKS_SLIME();
    public static TagKey<Block> STORAGE_BLOCKS_WHEAT = impl_STORAGE_BLOCKS_WHEAT();
    public static TagKey<Block> PLAYER_WORKSTATIONS_CRAFTING_TABLES = impl_PLAYER_WORKSTATIONS_CRAFTING_TABLES();
    public static TagKey<Block> PLAYER_WORKSTATIONS_FURNACES = impl_PLAYER_WORKSTATIONS_FURNACES();
    public static TagKey<Block> RELOCATION_NOT_SUPPORTED = impl_RELOCATION_NOT_SUPPORTED();
    public static TagKey<Block> SKULLS = impl_SKULLS();
    public static TagKey<Block> ROPES = impl_ROPES();
    public static TagKey<Block> CHAINS = impl_CHAINS();
    public static TagKey<Block> HIDDEN_FROM_RECIPE_VIEWERS = impl_HIDDEN_FROM_RECIPE_VIEWERS();
    // NeoForge only
    public static TagKey<Block> CHESTS_ENDER = impl_CHESTS_ENDER();
    // NeoForge only
    public static TagKey<Block> CHESTS_TRAPPED = impl_CHESTS_TRAPPED();
    // NeoForge only
    public static TagKey<Block> COBBLESTONES_NORMAL = impl_COBBLESTONES_NORMAL();
    // NeoForge only
    public static TagKey<Block> COBBLESTONES_INFESTED = impl_COBBLESTONES_INFESTED();
    // NeoForge only
    public static TagKey<Block> COBBLESTONES_MOSSY = impl_COBBLESTONES_MOSSY();
    // NeoForge only
    public static TagKey<Block> COBBLESTONES_DEEPSLATE = impl_COBBLESTONES_DEEPSLATE();
    // NeoForge only
    public static TagKey<Block> END_STONES = impl_END_STONES();
    // NeoForge only
    public static TagKey<Block> FENCE_GATES = impl_FENCE_GATES();
    // NeoForge only
    public static TagKey<Block> FENCE_GATES_WOODEN = impl_FENCE_GATES_WOODEN();
    // NeoForge only
    public static TagKey<Block> FENCES = impl_FENCES();
    // NeoForge only
    public static TagKey<Block> FENCES_NETHER_BRICK = impl_FENCES_NETHER_BRICK();
    // NeoForge only
    public static TagKey<Block> FENCES_WOODEN = impl_FENCES_WOODEN();
    // NeoForge only
    public static TagKey<Block> GLASS_BLOCKS_COLORLESS = impl_GLASS_BLOCKS_COLORLESS();
    // NeoForge only
    public static TagKey<Block> GLASS_BLOCKS_CHEAP = impl_GLASS_BLOCKS_CHEAP();
    // NeoForge only
    public static TagKey<Block> GLASS_BLOCKS_TINTED = impl_GLASS_BLOCKS_TINTED();
    // NeoForge only
    public static TagKey<Block> GLASS_PANES_COLORLESS = impl_GLASS_PANES_COLORLESS();
    // NeoForge only
    public static TagKey<Block> GRAVEL = impl_GRAVEL();
    // NeoForge only
    public static TagKey<Block> NETHERRACK = impl_NETHERRACK();
    // NeoForge only
    public static TagKey<Block> OBSIDIANS = impl_OBSIDIANS();
    // NeoForge only
    public static TagKey<Block> ORE_BEARING_GROUND_DEEPSLATE = impl_ORE_BEARING_GROUND_DEEPSLATE();
    // NeoForge only
    public static TagKey<Block> ORE_BEARING_GROUND_NETHERRACK = impl_ORE_BEARING_GROUND_NETHERRACK();
    // NeoForge only
    public static TagKey<Block> ORE_BEARING_GROUND_STONE = impl_ORE_BEARING_GROUND_STONE();
    // NeoForge only
    public static TagKey<Block> ORE_RATES_DENSE = impl_ORE_RATES_DENSE();
    // NeoForge only
    public static TagKey<Block> ORE_RATES_SINGULAR = impl_ORE_RATES_SINGULAR();
    // NeoForge only
    public static TagKey<Block> ORE_RATES_SPARSE = impl_ORE_RATES_SPARSE();
    // NeoForge only
    public static TagKey<Block> ORES_COAL = impl_ORES_COAL();
    // NeoForge only
    public static TagKey<Block> ORES_COPPER = impl_ORES_COPPER();
    // NeoForge only
    public static TagKey<Block> ORES_DIAMOND = impl_ORES_DIAMOND();
    // NeoForge only
    public static TagKey<Block> ORES_EMERALD = impl_ORES_EMERALD();
    // NeoForge only
    public static TagKey<Block> ORES_GOLD = impl_ORES_GOLD();
    // NeoForge only
    public static TagKey<Block> ORES_IRON = impl_ORES_IRON();
    // NeoForge only
    public static TagKey<Block> ORES_LAPIS = impl_ORES_LAPIS();
    // NeoForge only
    public static TagKey<Block> ORES_REDSTONE = impl_ORES_REDSTONE();
    // NeoForge only
    public static TagKey<Block> ORES_IN_GROUND_DEEPSLATE = impl_ORES_IN_GROUND_DEEPSLATE();
    // NeoForge only
    public static TagKey<Block> ORES_IN_GROUND_NETHERRACK = impl_ORES_IN_GROUND_NETHERRACK();
    // NeoForge only
    public static TagKey<Block> ORES_IN_GROUND_STONE = impl_ORES_IN_GROUND_STONE();
    // NeoForge only
    public static TagKey<Block> SANDS = impl_SANDS();
    // NeoForge only
    public static TagKey<Block> SANDS_COLORLESS = impl_SANDS_COLORLESS();
    // NeoForge only
    public static TagKey<Block> SANDS_RED = impl_SANDS_RED();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STONES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_COBBLESTONES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_QUARTZ() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_NETHERITE_SCRAP() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_BARRELS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_BARRELS_WOODEN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_BOOKSHELVES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CHESTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CHESTS_WOODEN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_PANES() {
        throw new AssertionError();
    }
    // Returns null on NeoForge
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SHULKER_BOXES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_BUDDING_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_BUDS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CLUSTERS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_VILLAGER_JOB_SITES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_SLABS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_STAIRS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_RED_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_RED_SLABS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_RED_STAIRS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_UNCOLORED_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_UNCOLORED_SLABS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDSTONE_UNCOLORED_STAIRS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_BLACK() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_BLUE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_BROWN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_CYAN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_GRAY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_GREEN() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_LIGHT_BLUE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_LIGHT_GRAY() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_LIME() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_MAGENTA() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_ORANGE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_PINK() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_PURPLE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_RED() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_WHITE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_DYED_YELLOW() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_BONE_MEAL() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_COAL() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_COPPER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_DIAMOND() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_DRIED_KELP() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_EMERALD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_GOLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_IRON() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_LAPIS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_NETHERITE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_RAW_COPPER() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_RAW_GOLD() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_RAW_IRON() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_REDSTONE() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_SLIME() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_STORAGE_BLOCKS_WHEAT() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_PLAYER_WORKSTATIONS_CRAFTING_TABLES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_PLAYER_WORKSTATIONS_FURNACES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_RELOCATION_NOT_SUPPORTED() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SKULLS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ROPES() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CHAINS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_HIDDEN_FROM_RECIPE_VIEWERS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CHESTS_ENDER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_CHESTS_TRAPPED() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_COBBLESTONES_NORMAL() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_COBBLESTONES_INFESTED() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_COBBLESTONES_MOSSY() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_COBBLESTONES_DEEPSLATE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_END_STONES() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_FENCE_GATES() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_FENCE_GATES_WOODEN() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_FENCES() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_FENCES_NETHER_BRICK() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_FENCES_WOODEN() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_BLOCKS_COLORLESS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_BLOCKS_CHEAP() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_BLOCKS_TINTED() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GLASS_PANES_COLORLESS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_GRAVEL() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_NETHERRACK() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_OBSIDIANS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_BEARING_GROUND_DEEPSLATE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_BEARING_GROUND_NETHERRACK() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_BEARING_GROUND_STONE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_RATES_DENSE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_RATES_SINGULAR() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORE_RATES_SPARSE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_COAL() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_COPPER() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_DIAMOND() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_EMERALD() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_GOLD() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_IRON() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_LAPIS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_REDSTONE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_IN_GROUND_DEEPSLATE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_IN_GROUND_NETHERRACK() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_ORES_IN_GROUND_STONE() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDS_COLORLESS() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Block> impl_SANDS_RED() {
        throw new AssertionError();
    }
}