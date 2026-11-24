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

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public final class ConventionalTags {
    public static final String NAMESPACE = "c";

    private ConventionalTags() {
    }

    private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registry, String path) {
        return TagKey.create(registry, Identifier.fromNamespaceAndPath(NAMESPACE, path));
    }

    public static final class Items {
        private Items() {
        }

        public static final TagKey<Item> STONES = tag("stones");
        public static final TagKey<Item> COBBLESTONES = tag("cobblestones");
        public static final TagKey<Item> DEEPSLATE_COBBLESTONES = tag("cobblestones/deepslate");
        public static final TagKey<Item> INFESTED_COBBLESTONES = tag("cobblestones/infested");
        public static final TagKey<Item> MOSSY_COBBLESTONES = tag("cobblestones/mossy");
        public static final TagKey<Item> NORMAL_COBBLESTONES = tag("cobblestones/normal");
        public static final TagKey<Item> NETHERRACKS = tag("netherracks");
        public static final TagKey<Item> END_STONES = tag("end_stones");
        public static final TagKey<Item> GRAVELS = tag("gravels");
        public static final TagKey<Item> OBSIDIANS = tag("obsidians");
        public static final TagKey<Item> NORMAL_OBSIDIANS = tag("obsidians/normal");
        public static final TagKey<Item> CRYING_OBSIDIANS = tag("obsidians/crying");
        public static final TagKey<Item> FROGLIGHTS = tag("froglights");

        public static final TagKey<Item> TOOLS = tag("tools");
        public static final TagKey<Item> SHEAR_TOOLS = tag("tools/shear");
        public static final TagKey<Item> TRIDENT_TOOLS = tag("tools/trident");
        public static final TagKey<Item> BOW_TOOLS = tag("tools/bow");
        public static final TagKey<Item> CROSSBOW_TOOLS = tag("tools/crossbow");
        public static final TagKey<Item> SHIELD_TOOLS = tag("tools/shield");
        public static final TagKey<Item> FISHING_ROD_TOOLS = tag("tools/fishing_rod");
        public static final TagKey<Item> BRUSH_TOOLS = tag("tools/brush");
        public static final TagKey<Item> IGNITER_TOOLS = tag("tools/igniter");
        public static final TagKey<Item> MACE_TOOLS = tag("tools/mace");
        public static final TagKey<Item> WRENCH_TOOLS = tag("tools/wrench");

        public static final TagKey<Item> MELEE_WEAPON_TOOLS = tag("tools/melee_weapon");
        public static final TagKey<Item> RANGED_WEAPON_TOOLS = tag("tools/ranged_weapon");
        public static final TagKey<Item> MINING_TOOL_TOOLS = tag("tools/mining_tool");

        public static final TagKey<Item> ARMORS = tag("armors");
        public static final TagKey<Item> HUMANOID_ARMORS = tag("armors/humanoid");
        public static final TagKey<Item> HORSE_ARMORS = tag("armors/horse");
        public static final TagKey<Item> NAUTILUS_ARMORS = tag("armors/nautilus");
        public static final TagKey<Item> WOLF_ARMORS = tag("armors/wolf");

        public static final TagKey<Item> ENCHANTABLES = tag("enchantables");

        public static final TagKey<Item> BRICKS = tag("bricks");
        public static final TagKey<Item> DUSTS = tag("dusts");
        public static final TagKey<Item> CLUMPS = tag("clumps");
        public static final TagKey<Item> GEMS = tag("gems");
        public static final TagKey<Item> INGOTS = tag("ingots");
        public static final TagKey<Item> NUGGETS = tag("nuggets");
        public static final TagKey<Item> ORES = tag("ores");
        public static final TagKey<Item> RAW_MATERIALS = tag("raw_materials");

        public static final TagKey<Item> IRON_RAW_MATERIALS = tag("raw_materials/iron");
        public static final TagKey<Item> GOLD_RAW_MATERIALS = tag("raw_materials/gold");
        public static final TagKey<Item> COPPER_RAW_MATERIALS = tag("raw_materials/copper");
        public static final TagKey<Item> IRIDIUM_RAW_MATERIALS = tag("raw_materials/iridium");
        public static final TagKey<Item> LEAD_RAW_MATERIALS = tag("raw_materials/lead");
        public static final TagKey<Item> SILVER_RAW_MATERIALS = tag("raw_materials/silver");
        public static final TagKey<Item> TIN_RAW_MATERIALS = tag("raw_materials/tin");
        public static final TagKey<Item> TITANIUM_RAW_MATERIALS = tag("raw_materials/titanium");
        public static final TagKey<Item> TUNGSTEN_RAW_MATERIALS = tag("raw_materials/tungsten");
        public static final TagKey<Item> URANIUM_RAW_MATERIALS = tag("raw_materials/uranium");
        public static final TagKey<Item> ZINC_RAW_MATERIALS = tag("raw_materials/zinc");

        public static final TagKey<Item> NORMAL_BRICKS = tag("bricks/normal");
        public static final TagKey<Item> NETHER_BRICKS = tag("bricks/nether");
        public static final TagKey<Item> RESIN_BRICKS = tag("bricks/resin");

        public static final TagKey<Item> IRON_INGOTS = tag("ingots/iron");
        public static final TagKey<Item> GOLD_INGOTS = tag("ingots/gold");
        public static final TagKey<Item> COPPER_INGOTS = tag("ingots/copper");
        public static final TagKey<Item> NETHERITE_INGOTS = tag("ingots/netherite");
        public static final TagKey<Item> ALUMINUM_INGOTS = tag("ingots/aluminum");
        public static final TagKey<Item> BRASS_INGOTS = tag("ingots/brass");
        public static final TagKey<Item> BRONZE_INGOTS = tag("ingots/bronze");
        public static final TagKey<Item> CHROMIUM_INGOTS = tag("ingots/chromium");
        public static final TagKey<Item> ELECTRUM_INGOTS = tag("ingots/electrum");
        public static final TagKey<Item> INVAR_INGOTS = tag("ingots/invar");
        public static final TagKey<Item> IRIDIUM_INGOTS = tag("ingots/iridium");
        public static final TagKey<Item> LEAD_INGOTS = tag("ingots/lead");
        public static final TagKey<Item> NICKEL_INGOTS = tag("ingots/nickel");
        public static final TagKey<Item> PLATINUM_INGOTS = tag("ingots/platinum");
        public static final TagKey<Item> SILVER_INGOTS = tag("ingots/silver");
        public static final TagKey<Item> STEEL_INGOTS = tag("ingots/steel");
        public static final TagKey<Item> TIN_INGOTS = tag("ingots/tin");
        public static final TagKey<Item> TITANIUM_INGOTS = tag("ingots/titanium");
        public static final TagKey<Item> TUNGSTEN_INGOTS = tag("ingots/tungsten");
        public static final TagKey<Item> URANIUM_INGOTS = tag("ingots/uranium");
        public static final TagKey<Item> ZINC_INGOTS = tag("ingots/zinc");

        public static final TagKey<Item> COAL_ORES = tag("ores/coal");
        public static final TagKey<Item> COPPER_ORES = tag("ores/copper");
        public static final TagKey<Item> DIAMOND_ORES = tag("ores/diamond");
        public static final TagKey<Item> EMERALD_ORES = tag("ores/emerald");
        public static final TagKey<Item> GOLD_ORES = tag("ores/gold");
        public static final TagKey<Item> IRON_ORES = tag("ores/iron");
        public static final TagKey<Item> LAPIS_ORES = tag("ores/lapis");
        public static final TagKey<Item> NETHERITE_SCRAP_ORES = tag("ores/netherite_scrap");
        public static final TagKey<Item> QUARTZ_ORES = tag("ores/quartz");
        public static final TagKey<Item> REDSTONE_ORES = tag("ores/redstone");
        public static final TagKey<Item> BAUXITE_ORES = tag("ores/bauxite");
        public static final TagKey<Item> CINNABAR_ORES = tag("ores/cinnabar");
        public static final TagKey<Item> GALENA_ORES = tag("ores/galena");
        public static final TagKey<Item> IRIDIUM_ORES = tag("ores/iridium");
        public static final TagKey<Item> LEAD_ORES = tag("ores/lead");
        public static final TagKey<Item> PERIDOT_ORES = tag("ores/peridot");
        public static final TagKey<Item> PYRITE_ORES = tag("ores/pyrite");
        public static final TagKey<Item> RUBY_ORES = tag("ores/ruby");
        public static final TagKey<Item> SAPPHIRE_ORES = tag("ores/sapphire");
        public static final TagKey<Item> SILVER_ORES = tag("ores/silver");
        public static final TagKey<Item> SPHALERITE_ORES = tag("ores/sphalerite");
        public static final TagKey<Item> SULFUR_ORES = tag("ores/sulfur");
        public static final TagKey<Item> TIN_ORES = tag("ores/tin");
        public static final TagKey<Item> TITANIUM_ORES = tag("ores/titanium");
        public static final TagKey<Item> TUNGSTEN_ORES = tag("ores/tungsten");
        public static final TagKey<Item> URANIUM_ORES = tag("ores/uranium");
        public static final TagKey<Item> ZINC_ORES = tag("ores/zinc");

        public static final TagKey<Item> QUARTZ_GEMS = tag("gems/quartz");
        public static final TagKey<Item> LAPIS_GEMS = tag("gems/lapis");
        public static final TagKey<Item> DIAMOND_GEMS = tag("gems/diamond");
        public static final TagKey<Item> AMETHYST_GEMS = tag("gems/amethyst");
        public static final TagKey<Item> EMERALD_GEMS = tag("gems/emerald");
        public static final TagKey<Item> PRISMARINE_GEMS = tag("gems/prismarine");
        public static final TagKey<Item> PERIDOT_GEMS = tag("gems/peridot");
        public static final TagKey<Item> RUBY_GEMS = tag("gems/ruby");
        public static final TagKey<Item> SAPPHIRE_GEMS = tag("gems/sapphire");
        public static final TagKey<Item> PLATES = tag("plates");
        public static final TagKey<Item> ALUMINUM_PLATES = tag("plates/aluminum");
        public static final TagKey<Item> BRASS_PLATES = tag("plates/brass");
        public static final TagKey<Item> BRONZE_PLATES = tag("plates/bronze");
        public static final TagKey<Item> CHROMIUM_PLATES = tag("plates/chromium");
        public static final TagKey<Item> COPPER_PLATES = tag("plates/copper");
        public static final TagKey<Item> ELECTRUM_PLATES = tag("plates/electrum");
        public static final TagKey<Item> GOLD_PLATES = tag("plates/gold");
        public static final TagKey<Item> INVAR_PLATES = tag("plates/invar");
        public static final TagKey<Item> IRIDIUM_PLATES = tag("plates/iridium");
        public static final TagKey<Item> IRON_PLATES = tag("plates/iron");
        public static final TagKey<Item> LEAD_PLATES = tag("plates/lead");
        public static final TagKey<Item> NICKEL_PLATES = tag("plates/nickel");
        public static final TagKey<Item> PERIDOT_PLATES = tag("plates/peridot");
        public static final TagKey<Item> PLATINUM_PLATES = tag("plates/platinum");
        public static final TagKey<Item> RUBY_PLATES = tag("plates/ruby");
        public static final TagKey<Item> SAPPHIRE_PLATES = tag("plates/sapphire");
        public static final TagKey<Item> SILVER_PLATES = tag("plates/silver");
        public static final TagKey<Item> STEEL_PLATES = tag("plates/steel");
        public static final TagKey<Item> TIN_PLATES = tag("plates/tin");
        public static final TagKey<Item> TITANIUM_PLATES = tag("plates/titanium");
        public static final TagKey<Item> TUNGSTEN_PLATES = tag("plates/tungsten");
        public static final TagKey<Item> ZINC_PLATES = tag("plates/zinc");
        public static final TagKey<Item> GEARS = tag("gears");
        public static final TagKey<Item> IRON_GEARS = tag("gears/iron");

        public static final TagKey<Item> COPPER_NUGGETS = tag("nuggets/copper");
        public static final TagKey<Item> IRON_NUGGETS = tag("nuggets/iron");
        public static final TagKey<Item> GOLD_NUGGETS = tag("nuggets/gold");
        public static final TagKey<Item> ALUMINUM_NUGGETS = tag("nuggets/aluminum");
        public static final TagKey<Item> BRASS_NUGGETS = tag("nuggets/brass");
        public static final TagKey<Item> BRONZE_NUGGETS = tag("nuggets/bronze");
        public static final TagKey<Item> CHROMIUM_NUGGETS = tag("nuggets/chromium");
        public static final TagKey<Item> ELECTRUM_NUGGETS = tag("nuggets/electrum");
        public static final TagKey<Item> INVAR_NUGGETS = tag("nuggets/invar");
        public static final TagKey<Item> IRIDIUM_NUGGETS = tag("nuggets/iridium");
        public static final TagKey<Item> LEAD_NUGGETS = tag("nuggets/lead");
        public static final TagKey<Item> NICKEL_NUGGETS = tag("nuggets/nickel");
        public static final TagKey<Item> PLATINUM_NUGGETS = tag("nuggets/platinum");
        public static final TagKey<Item> SILVER_NUGGETS = tag("nuggets/silver");
        public static final TagKey<Item> STEEL_NUGGETS = tag("nuggets/steel");
        public static final TagKey<Item> TIN_NUGGETS = tag("nuggets/tin");
        public static final TagKey<Item> TITANIUM_NUGGETS = tag("nuggets/titanium");
        public static final TagKey<Item> TUNGSTEN_NUGGETS = tag("nuggets/tungsten");
        public static final TagKey<Item> ZINC_NUGGETS = tag("nuggets/zinc");

        public static final TagKey<Item> REDSTONE_DUSTS = tag("dusts/redstone");
        public static final TagKey<Item> GLOWSTONE_DUSTS = tag("dusts/glowstone");
        public static final TagKey<Item> ALUMINUM_DUSTS = tag("dusts/aluminum");
        public static final TagKey<Item> AMETHYST_DUSTS = tag("dusts/amethyst");
        public static final TagKey<Item> BRASS_DUSTS = tag("dusts/brass");
        public static final TagKey<Item> BRONZE_DUSTS = tag("dusts/bronze");
        public static final TagKey<Item> CHARCOAL_DUSTS = tag("dusts/charcoal");
        public static final TagKey<Item> CHROMIUM_DUSTS = tag("dusts/chromium");
        public static final TagKey<Item> CLAY_DUSTS = tag("dusts/clay");
        public static final TagKey<Item> COAL_DUSTS = tag("dusts/coal");
        public static final TagKey<Item> COPPER_DUSTS = tag("dusts/copper");
        public static final TagKey<Item> DIAMOND_DUSTS = tag("dusts/diamond");
        public static final TagKey<Item> ELECTRUM_DUSTS = tag("dusts/electrum");
        public static final TagKey<Item> EMERALD_DUSTS = tag("dusts/emerald");
        public static final TagKey<Item> ENDER_PEARL_DUSTS = tag("dusts/ender_pearl");
        public static final TagKey<Item> FLINT_DUSTS = tag("dusts/flint");
        public static final TagKey<Item> GOLD_DUSTS = tag("dusts/gold");
        public static final TagKey<Item> INVAR_DUSTS = tag("dusts/invar");
        public static final TagKey<Item> IRON_DUSTS = tag("dusts/iron");
        public static final TagKey<Item> NETHERRACK_DUSTS = tag("dusts/netherrack");
        public static final TagKey<Item> NICKEL_DUSTS = tag("dusts/nickel");
        public static final TagKey<Item> OBSIDIAN_DUSTS = tag("dusts/obsidian");
        public static final TagKey<Item> PERIDOT_DUSTS = tag("dusts/peridot");
        public static final TagKey<Item> PLATINUM_DUSTS = tag("dusts/platinum");
        public static final TagKey<Item> QUARTZ_DUSTS = tag("dusts/quartz");
        public static final TagKey<Item> RUBY_DUSTS = tag("dusts/ruby");
        public static final TagKey<Item> SALT_DUSTS = tag("dusts/salt");
        public static final TagKey<Item> SALTPETER_DUSTS = tag("dusts/saltpeter");
        public static final TagKey<Item> SAPPHIRE_DUSTS = tag("dusts/sapphire");
        public static final TagKey<Item> STEEL_DUSTS = tag("dusts/steel");
        public static final TagKey<Item> SULFUR_DUSTS = tag("dusts/sulfur");
        public static final TagKey<Item> TIN_DUSTS = tag("dusts/tin");
        public static final TagKey<Item> TITANIUM_DUSTS = tag("dusts/titanium");
        public static final TagKey<Item> WOOD_DUSTS = tag("dusts/wood");
        public static final TagKey<Item> ZINC_DUSTS = tag("dusts/zinc");

        public static final TagKey<Item> RESIN_CLUMPS = tag("clumps/resin");

        public static final TagKey<Item> POTIONS = tag("potions");
        public static final TagKey<Item> BOTTLE_POTIONS = tag("potions/bottle");

        public static final TagKey<Item> FOODS = tag("foods");
        public static final TagKey<Item> ANIMAL_FOODS = tag("animal_foods");
        public static final TagKey<Item> FRUIT_FOODS = tag("foods/fruit");
        public static final TagKey<Item> VEGETABLE_FOODS = tag("foods/vegetable");
        public static final TagKey<Item> BERRY_FOODS = tag("foods/berry");
        public static final TagKey<Item> BREAD_FOODS = tag("foods/bread");
        public static final TagKey<Item> COOKIE_FOODS = tag("foods/cookie");
        public static final TagKey<Item> DOUGH_FOODS = tag("foods/dough");
        public static final TagKey<Item> RAW_MEAT_FOODS = tag("foods/raw_meat");
        public static final TagKey<Item> COOKED_MEAT_FOODS = tag("foods/cooked_meat");
        public static final TagKey<Item> RAW_FISH_FOODS = tag("foods/raw_fish");
        public static final TagKey<Item> COOKED_FISH_FOODS = tag("foods/cooked_fish");
        public static final TagKey<Item> SOUP_FOODS = tag("foods/soup");
        public static final TagKey<Item> CANDY_FOODS = tag("foods/candy");
        public static final TagKey<Item> PIE_FOODS = tag("foods/pie");
        public static final TagKey<Item> GOLDEN_FOODS = tag("foods/golden");
        public static final TagKey<Item> EDIBLE_WHEN_PLACED_FOODS = tag("foods/edible_when_placed");
        public static final TagKey<Item> FOOD_POISONING_FOODS = tag("foods/food_poisoning");

        public static final TagKey<Item> DRINKS = tag("drinks");
        public static final TagKey<Item> WATER_DRINKS = tag("drinks/water");
        public static final TagKey<Item> WATERY_DRINKS = tag("drinks/watery");
        public static final TagKey<Item> MILK_DRINKS = tag("drinks/milk");
        public static final TagKey<Item> HONEY_DRINKS = tag("drinks/honey");
        public static final TagKey<Item> MAGIC_DRINKS = tag("drinks/magic");
        public static final TagKey<Item> OMINOUS_DRINKS = tag("drinks/ominous");
        public static final TagKey<Item> JUICE_DRINKS = tag("drinks/juice");

        public static final TagKey<Item> DRINK_CONTAINING_BUCKET = tag("drink_containing/bucket");
        public static final TagKey<Item> DRINK_CONTAINING_BOTTLE = tag("drink_containing/bottle");

        public static final TagKey<Item> BUCKETS = tag("buckets");
        public static final TagKey<Item> EMPTY_BUCKETS = tag("buckets/empty");
        public static final TagKey<Item> WATER_BUCKETS = tag("buckets/water");
        public static final TagKey<Item> LAVA_BUCKETS = tag("buckets/lava");
        public static final TagKey<Item> MILK_BUCKETS = tag("buckets/milk");
        public static final TagKey<Item> POWDER_SNOW_BUCKETS = tag("buckets/powder_snow");
        public static final TagKey<Item> ENTITY_WATER_BUCKETS = tag("buckets/entity_water");
        public static final TagKey<Item> ENTITY_DRY_BUCKETS = tag("buckets/entity_dry");

        public static final TagKey<Item> BARRELS = tag("barrels");
        public static final TagKey<Item> WOODEN_BARRELS = tag("barrels/wooden");
        public static final TagKey<Item> BOOKSHELVES = tag("bookshelves");
        public static final TagKey<Item> CHESTS = tag("chests");
        public static final TagKey<Item> WOODEN_CHESTS = tag("chests/wooden");
        public static final TagKey<Item> TRAPPED_CHESTS = tag("chests/trapped");
        public static final TagKey<Item> ENDER_CHESTS = tag("chests/ender");
        public static final TagKey<Item> GLASS_BLOCKS = tag("glass_blocks");
        public static final TagKey<Item> GLASS_BLOCKS_COLORLESS = tag("glass_blocks/colorless");
        public static final TagKey<Item> GLASS_BLOCKS_CHEAP = tag("glass_blocks/cheap");
        public static final TagKey<Item> GLASS_BLOCKS_TINTED = tag("glass_blocks/tinted");
        public static final TagKey<Item> GLASS_PANES = tag("glass_panes");
        public static final TagKey<Item> GLASS_PANES_COLORLESS = tag("glass_panes/colorless");
        public static final TagKey<Item> SHULKER_BOXES = tag("shulker_boxes");
        public static final TagKey<Item> GLAZED_TERRACOTTAS = tag("glazed_terracottas");
        public static final TagKey<Item> CONCRETES = tag("concretes");
        public static final TagKey<Item> CONCRETE_POWDERS = tag("concrete_powders");

        public static final TagKey<Item> BUDDING_BLOCKS = tag("budding_blocks");
        public static final TagKey<Item> BUDS = tag("buds");
        public static final TagKey<Item> CLUSTERS = tag("clusters");

        public static final TagKey<Item> VILLAGER_JOB_SITES = tag("villager_job_sites");

        public static final TagKey<Item> SANDS = tag("sands");
        public static final TagKey<Item> RED_SANDS = tag("sands/red");
        public static final TagKey<Item> COLORLESS_SANDS = tag("sands/colorless");

        public static final TagKey<Item> SANDSTONE_BLOCKS = tag("sandstone/blocks");
        public static final TagKey<Item> SANDSTONE_SLABS = tag("sandstone/slabs");
        public static final TagKey<Item> SANDSTONE_STAIRS = tag("sandstone/stairs");
        public static final TagKey<Item> RED_SANDSTONE_BLOCKS = tag("sandstone/red_blocks");
        public static final TagKey<Item> RED_SANDSTONE_SLABS = tag("sandstone/red_slabs");
        public static final TagKey<Item> RED_SANDSTONE_STAIRS = tag("sandstone/red_stairs");
        public static final TagKey<Item> UNCOLORED_SANDSTONE_BLOCKS = tag("sandstone/uncolored_blocks");
        public static final TagKey<Item> UNCOLORED_SANDSTONE_SLABS = tag("sandstone/uncolored_slabs");
        public static final TagKey<Item> UNCOLORED_SANDSTONE_STAIRS = tag("sandstone/uncolored_stairs");

        public static final TagKey<Item> SMALL_FLOWERS = tag("flowers/small");
        public static final TagKey<Item> TALL_FLOWERS = tag("flowers/tall");
        public static final TagKey<Item> FLOWERS = tag("flowers");

        public static final TagKey<Item> FENCES = tag("fences");
        public static final TagKey<Item> WOODEN_FENCES = tag("fences/wooden");
        public static final TagKey<Item> NETHER_BRICK_FENCES = tag("fences/nether_brick");
        public static final TagKey<Item> FENCE_GATES = tag("fence_gates");
        public static final TagKey<Item> WOODEN_FENCE_GATES = tag("fence_gates/wooden");

        public static final TagKey<Item> BARS = tag("bars");
        public static final TagKey<Item> IRON_BARS = tag("bars/iron");
        public static final TagKey<Item> COPPER_BARS = tag("bars/copper");

        public static final TagKey<Item> PUMPKINS = tag("pumpkins");
        public static final TagKey<Item> NORMAL_PUMPKINS = tag("pumpkins/normal");
        public static final TagKey<Item> CARVED_PUMPKINS = tag("pumpkins/carved");

        public static final TagKey<Item> JACK_O_LANTERNS_PUMPKINS = tag("pumpkins/jack_o_lanterns");

        public static final TagKey<Item> DYES = tag("dyes");
        public static final TagKey<Item> BLACK_DYES = tag("dyes/black");
        public static final TagKey<Item> BLUE_DYES = tag("dyes/blue");
        public static final TagKey<Item> BROWN_DYES = tag("dyes/brown");
        public static final TagKey<Item> CYAN_DYES = tag("dyes/cyan");
        public static final TagKey<Item> GRAY_DYES = tag("dyes/gray");
        public static final TagKey<Item> GREEN_DYES = tag("dyes/green");
        public static final TagKey<Item> LIGHT_BLUE_DYES = tag("dyes/light_blue");
        public static final TagKey<Item> LIGHT_GRAY_DYES = tag("dyes/light_gray");
        public static final TagKey<Item> LIME_DYES = tag("dyes/lime");
        public static final TagKey<Item> MAGENTA_DYES = tag("dyes/magenta");
        public static final TagKey<Item> ORANGE_DYES = tag("dyes/orange");
        public static final TagKey<Item> PINK_DYES = tag("dyes/pink");
        public static final TagKey<Item> PURPLE_DYES = tag("dyes/purple");
        public static final TagKey<Item> RED_DYES = tag("dyes/red");
        public static final TagKey<Item> WHITE_DYES = tag("dyes/white");
        public static final TagKey<Item> YELLOW_DYES = tag("dyes/yellow");

        public static final TagKey<Item> DYED = tag("dyed");
        public static final TagKey<Item> BLACK_DYED = tag("dyed/black");
        public static final TagKey<Item> BLUE_DYED = tag("dyed/blue");
        public static final TagKey<Item> BROWN_DYED = tag("dyed/brown");
        public static final TagKey<Item> CYAN_DYED = tag("dyed/cyan");
        public static final TagKey<Item> GRAY_DYED = tag("dyed/gray");
        public static final TagKey<Item> GREEN_DYED = tag("dyed/green");
        public static final TagKey<Item> LIGHT_BLUE_DYED = tag("dyed/light_blue");
        public static final TagKey<Item> LIGHT_GRAY_DYED = tag("dyed/light_gray");
        public static final TagKey<Item> LIME_DYED = tag("dyed/lime");
        public static final TagKey<Item> MAGENTA_DYED = tag("dyed/magenta");
        public static final TagKey<Item> ORANGE_DYED = tag("dyed/orange");
        public static final TagKey<Item> PINK_DYED = tag("dyed/pink");
        public static final TagKey<Item> PURPLE_DYED = tag("dyed/purple");
        public static final TagKey<Item> RED_DYED = tag("dyed/red");
        public static final TagKey<Item> WHITE_DYED = tag("dyed/white");
        public static final TagKey<Item> YELLOW_DYED = tag("dyed/yellow");

        public static final TagKey<Item> STORAGE_BLOCKS = tag("storage_blocks");
        public static final TagKey<Item> STORAGE_BLOCKS_BONE_MEAL = tag("storage_blocks/bone_meal");
        public static final TagKey<Item> STORAGE_BLOCKS_COAL = tag("storage_blocks/coal");
        public static final TagKey<Item> STORAGE_BLOCKS_COPPER = tag("storage_blocks/copper");
        public static final TagKey<Item> STORAGE_BLOCKS_DIAMOND = tag("storage_blocks/diamond");
        public static final TagKey<Item> STORAGE_BLOCKS_DRIED_KELP = tag("storage_blocks/dried_kelp");
        public static final TagKey<Item> STORAGE_BLOCKS_EMERALD = tag("storage_blocks/emerald");
        public static final TagKey<Item> STORAGE_BLOCKS_GOLD = tag("storage_blocks/gold");
        public static final TagKey<Item> STORAGE_BLOCKS_IRON = tag("storage_blocks/iron");
        public static final TagKey<Item> STORAGE_BLOCKS_LAPIS = tag("storage_blocks/lapis");
        public static final TagKey<Item> STORAGE_BLOCKS_NETHERITE = tag("storage_blocks/netherite");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_COPPER = tag("storage_blocks/raw_copper");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_GOLD = tag("storage_blocks/raw_gold");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_IRON = tag("storage_blocks/raw_iron");
        public static final TagKey<Item> STORAGE_BLOCKS_REDSTONE = tag("storage_blocks/redstone");
        public static final TagKey<Item> STORAGE_BLOCKS_RESIN = tag("storage_blocks/resin");
        public static final TagKey<Item> STORAGE_BLOCKS_SLIME = tag("storage_blocks/slime");
        public static final TagKey<Item> STORAGE_BLOCKS_WHEAT = tag("storage_blocks/wheat");
        public static final TagKey<Item> STORAGE_BLOCKS_ALUMINUM = tag("storage_blocks/aluminum");
        public static final TagKey<Item> STORAGE_BLOCKS_BRASS = tag("storage_blocks/brass");
        public static final TagKey<Item> STORAGE_BLOCKS_BRONZE = tag("storage_blocks/bronze");
        public static final TagKey<Item> STORAGE_BLOCKS_CHROMIUM = tag("storage_blocks/chromium");
        public static final TagKey<Item> STORAGE_BLOCKS_ELECTRUM = tag("storage_blocks/electrum");
        public static final TagKey<Item> STORAGE_BLOCKS_INVAR = tag("storage_blocks/invar");
        public static final TagKey<Item> STORAGE_BLOCKS_IRIDIUM = tag("storage_blocks/iridium");
        public static final TagKey<Item> STORAGE_BLOCKS_LEAD = tag("storage_blocks/lead");
        public static final TagKey<Item> STORAGE_BLOCKS_NICKEL = tag("storage_blocks/nickel");
        public static final TagKey<Item> STORAGE_BLOCKS_PERIDOT = tag("storage_blocks/peridot");
        public static final TagKey<Item> STORAGE_BLOCKS_PLATINUM = tag("storage_blocks/platinum");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_IRIDIUM = tag("storage_blocks/raw_iridium");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_LEAD = tag("storage_blocks/raw_lead");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_SILVER = tag("storage_blocks/raw_silver");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TIN = tag("storage_blocks/raw_tin");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TITANIUM = tag("storage_blocks/raw_titanium");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_TUNGSTEN = tag("storage_blocks/raw_tungsten");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_URANIUM = tag("storage_blocks/raw_uranium");
        public static final TagKey<Item> STORAGE_BLOCKS_RAW_ZINC = tag("storage_blocks/raw_zinc");
        public static final TagKey<Item> STORAGE_BLOCKS_RUBY = tag("storage_blocks/ruby");
        public static final TagKey<Item> STORAGE_BLOCKS_SAPPHIRE = tag("storage_blocks/sapphire");
        public static final TagKey<Item> STORAGE_BLOCKS_SILVER = tag("storage_blocks/silver");
        public static final TagKey<Item> STORAGE_BLOCKS_STEEL = tag("storage_blocks/steel");
        public static final TagKey<Item> STORAGE_BLOCKS_TIN = tag("storage_blocks/tin");
        public static final TagKey<Item> STORAGE_BLOCKS_TITANIUM = tag("storage_blocks/titanium");
        public static final TagKey<Item> STORAGE_BLOCKS_TUNGSTEN = tag("storage_blocks/tungsten");
        public static final TagKey<Item> STORAGE_BLOCKS_URANIUM = tag("storage_blocks/uranium");
        public static final TagKey<Item> STORAGE_BLOCKS_ZINC = tag("storage_blocks/zinc");

        public static final TagKey<Item> OVERWORLD_NATURAL_LOGS = tag("natural_logs/overworld");
        public static final TagKey<Item> NETHER_NATURAL_LOGS = tag("natural_logs/nether");
        public static final TagKey<Item> NATURAL_LOGS = tag("natural_logs");
        public static final TagKey<Item> NATURAL_WOODS = tag("natural_woods");
        public static final TagKey<Item> STRIPPED_LOGS = tag("stripped_logs");
        public static final TagKey<Item> STRIPPED_WOODS = tag("stripped_woods");

        public static final TagKey<Item> CROPS = tag("crops");
        public static final TagKey<Item> BEETROOT_CROPS = tag("crops/beetroot");
        public static final TagKey<Item> CACTUS_CROPS = tag("crops/cactus");
        public static final TagKey<Item> CARROT_CROPS = tag("crops/carrot");
        public static final TagKey<Item> COCOA_BEAN_CROPS = tag("crops/cocoa_bean");
        public static final TagKey<Item> MELON_CROPS = tag("crops/melon");
        public static final TagKey<Item> NETHER_WART_CROPS = tag("crops/nether_wart");
        public static final TagKey<Item> POTATO_CROPS = tag("crops/potato");
        public static final TagKey<Item> PUMPKIN_CROPS = tag("crops/pumpkin");
        public static final TagKey<Item> SUGAR_CANE_CROPS = tag("crops/sugar_cane");
        public static final TagKey<Item> WHEAT_CROPS = tag("crops/wheat");

        public static final TagKey<Item> SEEDS = tag("seeds");
        public static final TagKey<Item> BEETROOT_SEEDS = tag("seeds/beetroot");
        public static final TagKey<Item> MELON_SEEDS = tag("seeds/melon");
        public static final TagKey<Item> PUMPKIN_SEEDS = tag("seeds/pumpkin");
        public static final TagKey<Item> TORCHFLOWER_SEEDS = tag("seeds/torchflower");
        public static final TagKey<Item> PITCHER_PLANT_SEEDS = tag("seeds/pitcher_plant");
        public static final TagKey<Item> WHEAT_SEEDS = tag("seeds/wheat");

        public static final TagKey<Item> PLAYER_WORKSTATIONS_CRAFTING_TABLES = tag("player_workstations/crafting_tables");
        public static final TagKey<Item> PLAYER_WORKSTATIONS_FURNACES = tag("player_workstations/furnaces");
        public static final TagKey<Item> STRINGS = tag("strings");
        public static final TagKey<Item> LEATHERS = tag("leathers");
        public static final TagKey<Item> BONES = tag("bones");
        public static final TagKey<Item> EGGS = tag("eggs");
        public static final TagKey<Item> FEATHERS = tag("feathers");
        public static final TagKey<Item> GUNPOWDERS = tag("gunpowders");
        public static final TagKey<Item> MUSHROOMS = tag("mushrooms");
        public static final TagKey<Item> NETHER_STARS = tag("nether_stars");
        public static final TagKey<Item> MUSIC_DISCS = tag("music_discs");
        public static final TagKey<Item> RODS = tag("rods");
        public static final TagKey<Item> WOODEN_RODS = tag("rods/wooden");
        public static final TagKey<Item> BLAZE_RODS = tag("rods/blaze");
        public static final TagKey<Item> BREEZE_RODS = tag("rods/breeze");
        public static final TagKey<Item> IRON_RODS = tag("rods/iron");
        public static final TagKey<Item> WIRES = tag("wires");
        public static final TagKey<Item> COPPER_WIRES = tag("wires/copper");
        public static final TagKey<Item> GOLD_WIRES = tag("wires/gold");
        public static final TagKey<Item> TIN_WIRES = tag("wires/tin");
        public static final TagKey<Item> ROPES = tag("ropes");
        public static final TagKey<Item> CHAINS = tag("chains");
        public static final TagKey<Item> ENDER_PEARLS = tag("ender_pearls");
        public static final TagKey<Item> SLIME_BALLS = tag("slime_balls");
        public static final TagKey<Item> FERTILIZERS = tag("fertilizers");

        public static final TagKey<Item> HIDDEN_FROM_RECIPE_VIEWERS = tag("hidden_from_recipe_viewers");

        public static final TagKey<Item> ORE_BEARING_GROUND_DEEPSLATE = tag("ore_bearing_ground/deepslate");
        public static final TagKey<Item> ORE_BEARING_GROUND_NETHERRACK = tag("ore_bearing_ground/netherrack");
        public static final TagKey<Item> ORE_BEARING_GROUND_STONE = tag("ore_bearing_ground/stone");
        public static final TagKey<Item> ORE_RATES_DENSE = tag("ore_rates/dense");
        public static final TagKey<Item> ORE_RATES_SINGULAR = tag("ore_rates/singular");
        public static final TagKey<Item> ORE_RATES_SPARSE = tag("ore_rates/sparse");
        public static final TagKey<Item> ORES_IN_GROUND_DEEPSLATE = tag("ores_in_ground/deepslate");
        public static final TagKey<Item> ORES_IN_GROUND_NETHERRACK = tag("ores_in_ground/netherrack");
        public static final TagKey<Item> ORES_IN_GROUND_STONE = tag("ores_in_ground/stone");

        private static TagKey<Item> tag(String path) {
            return create(Registries.ITEM, path);
        }
    }

    public static final class Blocks {
        private Blocks() {
        }

        public static final TagKey<Block> STONES = tag("stones");
        public static final TagKey<Block> COBBLESTONES = tag("cobblestones");
        public static final TagKey<Block> DEEPSLATE_COBBLESTONES = tag("cobblestones/deepslate");
        public static final TagKey<Block> INFESTED_COBBLESTONES = tag("cobblestones/infested");
        public static final TagKey<Block> MOSSY_COBBLESTONES = tag("cobblestones/mossy");
        public static final TagKey<Block> NORMAL_COBBLESTONES = tag("cobblestones/normal");
        public static final TagKey<Block> NETHERRACKS = tag("netherracks");
        public static final TagKey<Block> END_STONES = tag("end_stones");
        public static final TagKey<Block> GRAVELS = tag("gravels");
        public static final TagKey<Block> OBSIDIANS = tag("obsidians");
        public static final TagKey<Block> NORMAL_OBSIDIANS = tag("obsidians/normal");
        public static final TagKey<Block> CRYING_OBSIDIANS = tag("obsidians/crying");
        public static final TagKey<Block> FROGLIGHTS = tag("froglights");

        public static final TagKey<Block> ORES = tag("ores");

        public static final TagKey<Block> COAL_ORES = tag("ores/coal");
        public static final TagKey<Block> COPPER_ORES = tag("ores/copper");
        public static final TagKey<Block> DIAMOND_ORES = tag("ores/diamond");
        public static final TagKey<Block> EMERALD_ORES = tag("ores/emerald");
        public static final TagKey<Block> GOLD_ORES = tag("ores/gold");
        public static final TagKey<Block> IRON_ORES = tag("ores/iron");
        public static final TagKey<Block> LAPIS_ORES = tag("ores/lapis");
        public static final TagKey<Block> NETHERITE_SCRAP_ORES = tag("ores/netherite_scrap");
        public static final TagKey<Block> QUARTZ_ORES = tag("ores/quartz");
        public static final TagKey<Block> REDSTONE_ORES = tag("ores/redstone");
        public static final TagKey<Block> BAUXITE_ORES = tag("ores/bauxite");
        public static final TagKey<Block> CINNABAR_ORES = tag("ores/cinnabar");
        public static final TagKey<Block> GALENA_ORES = tag("ores/galena");
        public static final TagKey<Block> IRIDIUM_ORES = tag("ores/iridium");
        public static final TagKey<Block> LEAD_ORES = tag("ores/lead");
        public static final TagKey<Block> PERIDOT_ORES = tag("ores/peridot");
        public static final TagKey<Block> PYRITE_ORES = tag("ores/pyrite");
        public static final TagKey<Block> RUBY_ORES = tag("ores/ruby");
        public static final TagKey<Block> SAPPHIRE_ORES = tag("ores/sapphire");
        public static final TagKey<Block> SILVER_ORES = tag("ores/silver");
        public static final TagKey<Block> SPHALERITE_ORES = tag("ores/sphalerite");
        public static final TagKey<Block> SULFUR_ORES = tag("ores/sulfur");
        public static final TagKey<Block> TIN_ORES = tag("ores/tin");
        public static final TagKey<Block> TITANIUM_ORES = tag("ores/titanium");
        public static final TagKey<Block> TUNGSTEN_ORES = tag("ores/tungsten");
        public static final TagKey<Block> URANIUM_ORES = tag("ores/uranium");
        public static final TagKey<Block> ZINC_ORES = tag("ores/zinc");

        public static final TagKey<Block> BARRELS = tag("barrels");
        public static final TagKey<Block> WOODEN_BARRELS = tag("barrels/wooden");
        public static final TagKey<Block> BOOKSHELVES = tag("bookshelves");
        public static final TagKey<Block> CHESTS = tag("chests");
        public static final TagKey<Block> WOODEN_CHESTS = tag("chests/wooden");
        public static final TagKey<Block> TRAPPED_CHESTS = tag("chests/trapped");
        public static final TagKey<Block> ENDER_CHESTS = tag("chests/ender");
        public static final TagKey<Block> GLASS_BLOCKS = tag("glass_blocks");
        public static final TagKey<Block> GLASS_BLOCKS_COLORLESS = tag("glass_blocks/colorless");
        public static final TagKey<Block> GLASS_BLOCKS_CHEAP = tag("glass_blocks/cheap");
        public static final TagKey<Block> GLASS_BLOCKS_TINTED = tag("glass_blocks/tinted");
        public static final TagKey<Block> GLASS_PANES = tag("glass_panes");
        public static final TagKey<Block> GLASS_PANES_COLORLESS = tag("glass_panes/colorless");
        public static final TagKey<Block> GLAZED_TERRACOTTAS = tag("glazed_terracottas");
        public static final TagKey<Block> CONCRETES = tag("concretes");

        public static final TagKey<Block> BUDDING_BLOCKS = tag("budding_blocks");
        public static final TagKey<Block> BUDS = tag("buds");
        public static final TagKey<Block> CLUSTERS = tag("clusters");

        public static final TagKey<Block> VILLAGER_JOB_SITES = tag("villager_job_sites");

        public static final TagKey<Block> SANDS = tag("sands");
        public static final TagKey<Block> RED_SANDS = tag("sands/red");
        public static final TagKey<Block> COLORLESS_SANDS = tag("sands/colorless");

        public static final TagKey<Block> SMALL_FLOWERS = tag("flowers/small");
        public static final TagKey<Block> TALL_FLOWERS = tag("flowers/tall");
        public static final TagKey<Block> FLOWERS = tag("flowers");

        public static final TagKey<Block> SANDSTONE_BLOCKS = tag("sandstone/blocks");
        public static final TagKey<Block> SANDSTONE_SLABS = tag("sandstone/slabs");
        public static final TagKey<Block> SANDSTONE_STAIRS = tag("sandstone/stairs");
        public static final TagKey<Block> RED_SANDSTONE_BLOCKS = tag("sandstone/red_blocks");
        public static final TagKey<Block> RED_SANDSTONE_SLABS = tag("sandstone/red_slabs");
        public static final TagKey<Block> RED_SANDSTONE_STAIRS = tag("sandstone/red_stairs");
        public static final TagKey<Block> UNCOLORED_SANDSTONE_BLOCKS = tag("sandstone/uncolored_blocks");
        public static final TagKey<Block> UNCOLORED_SANDSTONE_SLABS = tag("sandstone/uncolored_slabs");
        public static final TagKey<Block> UNCOLORED_SANDSTONE_STAIRS = tag("sandstone/uncolored_stairs");

        public static final TagKey<Block> FENCES = tag("fences");
        public static final TagKey<Block> WOODEN_FENCES = tag("fences/wooden");
        public static final TagKey<Block> NETHER_BRICK_FENCES = tag("fences/nether_brick");
        public static final TagKey<Block> FENCE_GATES = tag("fence_gates");
        public static final TagKey<Block> WOODEN_FENCE_GATES = tag("fence_gates/wooden");

        public static final TagKey<Block> BARS = tag("bars");
        public static final TagKey<Block> IRON_BARS = tag("bars/iron");
        public static final TagKey<Block> COPPER_BARS = tag("bars/copper");

        public static final TagKey<Block> PUMPKINS = tag("pumpkins");
        public static final TagKey<Block> NORMAL_PUMPKINS = tag("pumpkins/normal");
        public static final TagKey<Block> CARVED_PUMPKINS = tag("pumpkins/carved");

        public static final TagKey<Block> JACK_O_LANTERNS_PUMPKINS = tag("pumpkins/jack_o_lanterns");

        public static final TagKey<Block> DYED = tag("dyed");
        public static final TagKey<Block> BLACK_DYED = tag("dyed/black");
        public static final TagKey<Block> BLUE_DYED = tag("dyed/blue");
        public static final TagKey<Block> BROWN_DYED = tag("dyed/brown");
        public static final TagKey<Block> CYAN_DYED = tag("dyed/cyan");
        public static final TagKey<Block> GRAY_DYED = tag("dyed/gray");
        public static final TagKey<Block> GREEN_DYED = tag("dyed/green");
        public static final TagKey<Block> LIGHT_BLUE_DYED = tag("dyed/light_blue");
        public static final TagKey<Block> LIGHT_GRAY_DYED = tag("dyed/light_gray");
        public static final TagKey<Block> LIME_DYED = tag("dyed/lime");
        public static final TagKey<Block> MAGENTA_DYED = tag("dyed/magenta");
        public static final TagKey<Block> ORANGE_DYED = tag("dyed/orange");
        public static final TagKey<Block> PINK_DYED = tag("dyed/pink");
        public static final TagKey<Block> PURPLE_DYED = tag("dyed/purple");
        public static final TagKey<Block> RED_DYED = tag("dyed/red");
        public static final TagKey<Block> WHITE_DYED = tag("dyed/white");
        public static final TagKey<Block> YELLOW_DYED = tag("dyed/yellow");

        public static final TagKey<Block> STORAGE_BLOCKS = tag("storage_blocks");
        public static final TagKey<Block> STORAGE_BLOCKS_BONE_MEAL = tag("storage_blocks/bone_meal");
        public static final TagKey<Block> STORAGE_BLOCKS_COAL = tag("storage_blocks/coal");
        public static final TagKey<Block> STORAGE_BLOCKS_COPPER = tag("storage_blocks/copper");
        public static final TagKey<Block> STORAGE_BLOCKS_DIAMOND = tag("storage_blocks/diamond");
        public static final TagKey<Block> STORAGE_BLOCKS_DRIED_KELP = tag("storage_blocks/dried_kelp");
        public static final TagKey<Block> STORAGE_BLOCKS_EMERALD = tag("storage_blocks/emerald");
        public static final TagKey<Block> STORAGE_BLOCKS_GOLD = tag("storage_blocks/gold");
        public static final TagKey<Block> STORAGE_BLOCKS_IRON = tag("storage_blocks/iron");
        public static final TagKey<Block> STORAGE_BLOCKS_LAPIS = tag("storage_blocks/lapis");
        public static final TagKey<Block> STORAGE_BLOCKS_NETHERITE = tag("storage_blocks/netherite");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_COPPER = tag("storage_blocks/raw_copper");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_GOLD = tag("storage_blocks/raw_gold");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRON = tag("storage_blocks/raw_iron");
        public static final TagKey<Block> STORAGE_BLOCKS_REDSTONE = tag("storage_blocks/redstone");
        public static final TagKey<Block> STORAGE_BLOCKS_RESIN = tag("storage_blocks/resin");
        public static final TagKey<Block> STORAGE_BLOCKS_SLIME = tag("storage_blocks/slime");
        public static final TagKey<Block> STORAGE_BLOCKS_WHEAT = tag("storage_blocks/wheat");
        public static final TagKey<Block> STORAGE_BLOCKS_ALUMINUM = tag("storage_blocks/aluminum");
        public static final TagKey<Block> STORAGE_BLOCKS_BRASS = tag("storage_blocks/brass");
        public static final TagKey<Block> STORAGE_BLOCKS_BRONZE = tag("storage_blocks/bronze");
        public static final TagKey<Block> STORAGE_BLOCKS_CHROMIUM = tag("storage_blocks/chromium");
        public static final TagKey<Block> STORAGE_BLOCKS_ELECTRUM = tag("storage_blocks/electrum");
        public static final TagKey<Block> STORAGE_BLOCKS_INVAR = tag("storage_blocks/invar");
        public static final TagKey<Block> STORAGE_BLOCKS_IRIDIUM = tag("storage_blocks/iridium");
        public static final TagKey<Block> STORAGE_BLOCKS_LEAD = tag("storage_blocks/lead");
        public static final TagKey<Block> STORAGE_BLOCKS_NICKEL = tag("storage_blocks/nickel");
        public static final TagKey<Block> STORAGE_BLOCKS_PERIDOT = tag("storage_blocks/peridot");
        public static final TagKey<Block> STORAGE_BLOCKS_PLATINUM = tag("storage_blocks/platinum");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_IRIDIUM = tag("storage_blocks/raw_iridium");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_LEAD = tag("storage_blocks/raw_lead");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_SILVER = tag("storage_blocks/raw_silver");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_TIN = tag("storage_blocks/raw_tin");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_TITANIUM = tag("storage_blocks/raw_titanium");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_TUNGSTEN = tag("storage_blocks/raw_tungsten");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_URANIUM = tag("storage_blocks/raw_uranium");
        public static final TagKey<Block> STORAGE_BLOCKS_RAW_ZINC = tag("storage_blocks/raw_zinc");
        public static final TagKey<Block> STORAGE_BLOCKS_RUBY = tag("storage_blocks/ruby");
        public static final TagKey<Block> STORAGE_BLOCKS_SAPPHIRE = tag("storage_blocks/sapphire");
        public static final TagKey<Block> STORAGE_BLOCKS_SILVER = tag("storage_blocks/silver");
        public static final TagKey<Block> STORAGE_BLOCKS_STEEL = tag("storage_blocks/steel");
        public static final TagKey<Block> STORAGE_BLOCKS_TIN = tag("storage_blocks/tin");
        public static final TagKey<Block> STORAGE_BLOCKS_TITANIUM = tag("storage_blocks/titanium");
        public static final TagKey<Block> STORAGE_BLOCKS_TUNGSTEN = tag("storage_blocks/tungsten");
        public static final TagKey<Block> STORAGE_BLOCKS_URANIUM = tag("storage_blocks/uranium");
        public static final TagKey<Block> STORAGE_BLOCKS_ZINC = tag("storage_blocks/zinc");

        public static final TagKey<Block> OVERWORLD_NATURAL_LOGS = tag("natural_logs/overworld");
        public static final TagKey<Block> NETHER_NATURAL_LOGS = tag("natural_logs/nether");
        public static final TagKey<Block> NATURAL_LOGS = tag("natural_logs");
        public static final TagKey<Block> NATURAL_WOODS = tag("natural_woods");
        public static final TagKey<Block> STRIPPED_LOGS = tag("stripped_logs");
        public static final TagKey<Block> STRIPPED_WOODS = tag("stripped_woods");

        public static final TagKey<Block> PLAYER_WORKSTATIONS_CRAFTING_TABLES = tag("player_workstations/crafting_tables");
        public static final TagKey<Block> PLAYER_WORKSTATIONS_FURNACES = tag("player_workstations/furnaces");
        public static final TagKey<Block> RELOCATION_NOT_SUPPORTED = tag("relocation_not_supported");
        public static final TagKey<Block> SKULLS = tag("skulls");
        public static final TagKey<Block> ROPES = tag("ropes");
        public static final TagKey<Block> CHAINS = tag("chains");

        public static final TagKey<Block> HIDDEN_FROM_RECIPE_VIEWERS = tag("hidden_from_recipe_viewers");

        public static final TagKey<Block> ORE_BEARING_GROUND_DEEPSLATE = tag("ore_bearing_ground/deepslate");
        public static final TagKey<Block> ORE_BEARING_GROUND_NETHERRACK = tag("ore_bearing_ground/netherrack");
        public static final TagKey<Block> ORE_BEARING_GROUND_STONE = tag("ore_bearing_ground/stone");
        public static final TagKey<Block> ORE_RATES_DENSE = tag("ore_rates/dense");
        public static final TagKey<Block> ORE_RATES_SINGULAR = tag("ore_rates/singular");
        public static final TagKey<Block> ORE_RATES_SPARSE = tag("ore_rates/sparse");
        public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = tag("ores_in_ground/deepslate");
        public static final TagKey<Block> ORES_IN_GROUND_NETHERRACK = tag("ores_in_ground/netherrack");
        public static final TagKey<Block> ORES_IN_GROUND_STONE = tag("ores_in_ground/stone");

        private static TagKey<Block> tag(String path) {
            return create(Registries.BLOCK, path);
        }
    }

    public static final class Fluids {
        private Fluids() {
        }

        public static final TagKey<Fluid> LAVA = tag("lava");
        public static final TagKey<Fluid> WATER = tag("water");
        public static final TagKey<Fluid> MILK = tag("milk");
        public static final TagKey<Fluid> HONEY = tag("honey");
        public static final TagKey<Fluid> GASEOUS = tag("gaseous");
        public static final TagKey<Fluid> EXPERIENCE = tag("experience");
        public static final TagKey<Fluid> POTION = tag("potion");
        public static final TagKey<Fluid> SUSPICIOUS_STEW = tag("suspicious_stew");
        public static final TagKey<Fluid> MUSHROOM_STEW = tag("mushroom_stew");
        public static final TagKey<Fluid> RABBIT_STEW = tag("rabbit_stew");
        public static final TagKey<Fluid> BEETROOT_SOUP = tag("beetroot_soup");
        public static final TagKey<Fluid> HIDDEN_FROM_RECIPE_VIEWERS = tag("hidden_from_recipe_viewers");

        private static TagKey<Fluid> tag(String path) {
            return create(Registries.FLUID, path);
        }
    }

    public static final class EntityTypes {
        private EntityTypes() {
        }

        public static final TagKey<EntityType<?>> BOSSES = tag("bosses");

        public static final TagKey<EntityType<?>> MINECARTS = tag("minecarts");
        public static final TagKey<EntityType<?>> BOATS = tag("boats");
        public static final TagKey<EntityType<?>> ITEM_FRAMES = tag("item_frames");

        public static final TagKey<EntityType<?>> CAPTURING_NOT_SUPPORTED = tag("capturing_not_supported");

        public static final TagKey<EntityType<?>> TELEPORTING_NOT_SUPPORTED = tag("teleporting_not_supported");

        private static TagKey<EntityType<?>> tag(String path) {
            return create(Registries.ENTITY_TYPE, path);
        }
    }

    public static final class Enchantments {
        private Enchantments() {
        }

        public static final TagKey<Enchantment> INCREASE_BLOCK_DROPS = tag("increase_block_drops");
        public static final TagKey<Enchantment> INCREASE_ENTITY_DROPS = tag("increase_entity_drops");
        public static final TagKey<Enchantment> WEAPON_DAMAGE_ENHANCEMENTS = tag("weapon_damage_enhancements");
        public static final TagKey<Enchantment> ENTITY_SPEED_ENHANCEMENTS = tag("entity_speed_enhancements");
        public static final TagKey<Enchantment> ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS = tag("entity_auxiliary_movement_enhancements");
        public static final TagKey<Enchantment> ENTITY_DEFENSE_ENHANCEMENTS = tag("entity_defense_enhancements");
        public static final TagKey<Enchantment> HIDDEN_FROM_RECIPE_VIEWERS = tag("hidden_from_recipe_viewers");

        private static TagKey<Enchantment> tag(String path) {
            return create(Registries.ENCHANTMENT, path);
        }
    }

    public static final class Biomes {
        private Biomes() {
        }

        public static final TagKey<Biome> NO_DEFAULT_MONSTERS = tag("no_default_monsters");
        public static final TagKey<Biome> HIDDEN_FROM_LOCATOR_SELECTION = tag("hidden_from_locator_selection");

        public static final TagKey<Biome> IS_VOID = tag("is_void");

        public static final TagKey<Biome> IS_OVERWORLD = tag("is_overworld");

        public static final TagKey<Biome> IS_HOT = tag("is_hot");
        public static final TagKey<Biome> IS_HOT_OVERWORLD = tag("is_hot/overworld");
        public static final TagKey<Biome> IS_HOT_NETHER = tag("is_hot/nether");
        public static final TagKey<Biome> IS_HOT_END = tag("is_hot/end");

        public static final TagKey<Biome> IS_TEMPERATE = tag("is_temperate");
        public static final TagKey<Biome> IS_TEMPERATE_OVERWORLD = tag("is_temperate/overworld");
        public static final TagKey<Biome> IS_TEMPERATE_NETHER = tag("is_temperate/nether");
        public static final TagKey<Biome> IS_TEMPERATE_END = tag("is_temperate/end");

        public static final TagKey<Biome> IS_COLD = tag("is_cold");
        public static final TagKey<Biome> IS_COLD_OVERWORLD = tag("is_cold/overworld");
        public static final TagKey<Biome> IS_COLD_NETHER = tag("is_cold/nether");
        public static final TagKey<Biome> IS_COLD_END = tag("is_cold/end");

        public static final TagKey<Biome> IS_WET = tag("is_wet");
        public static final TagKey<Biome> IS_WET_OVERWORLD = tag("is_wet/overworld");
        public static final TagKey<Biome> IS_WET_NETHER = tag("is_wet/nether");
        public static final TagKey<Biome> IS_WET_END = tag("is_wet/end");

        public static final TagKey<Biome> IS_DRY = tag("is_dry");
        public static final TagKey<Biome> IS_DRY_OVERWORLD = tag("is_dry/overworld");
        public static final TagKey<Biome> IS_DRY_NETHER = tag("is_dry/nether");
        public static final TagKey<Biome> IS_DRY_END = tag("is_dry/end");

        public static final TagKey<Biome> IS_VEGETATION_SPARSE = tag("is_sparse_vegetation");
        public static final TagKey<Biome> IS_VEGETATION_SPARSE_OVERWORLD = tag("is_sparse_vegetation/overworld");
        public static final TagKey<Biome> IS_VEGETATION_SPARSE_NETHER = tag("is_sparse_vegetation/nether");
        public static final TagKey<Biome> IS_VEGETATION_SPARSE_END = tag("is_sparse_vegetation/end");

        public static final TagKey<Biome> IS_VEGETATION_DENSE = tag("is_dense_vegetation");
        public static final TagKey<Biome> IS_VEGETATION_DENSE_OVERWORLD = tag("is_dense_vegetation/overworld");
        public static final TagKey<Biome> IS_VEGETATION_DENSE_NETHER = tag("is_dense_vegetation/nether");
        public static final TagKey<Biome> IS_VEGETATION_DENSE_END = tag("is_dense_vegetation/end");

        public static final TagKey<Biome> PRIMARY_WOOD_TYPE = tag("primary_wood_type");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_OAK = tag("primary_wood_type/oak");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_BIRCH = tag("primary_wood_type/birch");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_SPRUCE = tag("primary_wood_type/spruce");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_JUNGLE = tag("primary_wood_type/jungle");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_ACACIA = tag("primary_wood_type/acacia");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_DARK_OAK = tag("primary_wood_type/dark_oak");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_MANGROVE = tag("primary_wood_type/mangrove");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_CHERRY = tag("primary_wood_type/cherry");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_PALE_OAK = tag("primary_wood_type/pale_oak");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_BAMBOO = tag("primary_wood_type/bamboo");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_CRIMSON = tag("primary_wood_type/crimson");
        public static final TagKey<Biome> PRIMARY_WOOD_TYPE_WARPED = tag("primary_wood_type/warped");

        public static final TagKey<Biome> IS_CONIFEROUS_TREE = tag("is_tree/coniferous");
        public static final TagKey<Biome> IS_SAVANNA_TREE = tag("is_tree/savanna");
        public static final TagKey<Biome> IS_JUNGLE_TREE = tag("is_tree/jungle");
        public static final TagKey<Biome> IS_DECIDUOUS_TREE = tag("is_tree/deciduous");

        public static final TagKey<Biome> IS_MOUNTAIN = tag("is_mountain");
        public static final TagKey<Biome> IS_MOUNTAIN_PEAK = tag("is_mountain/peak");
        public static final TagKey<Biome> IS_MOUNTAIN_SLOPE = tag("is_mountain/slope");

        public static final TagKey<Biome> IS_PLAINS = tag("is_plains");
        public static final TagKey<Biome> IS_SNOWY_PLAINS = tag("is_snowy_plains");
        public static final TagKey<Biome> IS_FOREST = tag("is_forest");
        public static final TagKey<Biome> IS_BIRCH_FOREST = tag("is_birch_forest");
        public static final TagKey<Biome> IS_DARK_FOREST = tag("is_dark_forest");
        public static final TagKey<Biome> IS_FLOWER_FOREST = tag("is_flower_forest");
        public static final TagKey<Biome> IS_TAIGA = tag("is_taiga");
        public static final TagKey<Biome> IS_OLD_GROWTH = tag("is_old_growth");
        public static final TagKey<Biome> IS_HILL = tag("is_hill");
        public static final TagKey<Biome> IS_WINDSWEPT = tag("is_windswept");
        public static final TagKey<Biome> IS_JUNGLE = tag("is_jungle");
        public static final TagKey<Biome> IS_SAVANNA = tag("is_savanna");
        public static final TagKey<Biome> IS_SWAMP = tag("is_swamp");
        public static final TagKey<Biome> IS_DESERT = tag("is_desert");
        public static final TagKey<Biome> IS_BADLANDS = tag("is_badlands");
        public static final TagKey<Biome> IS_BEACH = tag("is_beach");
        public static final TagKey<Biome> IS_STONY_SHORES = tag("is_stony_shores");
        public static final TagKey<Biome> IS_MUSHROOM = tag("is_mushroom");

        public static final TagKey<Biome> IS_RIVER = tag("is_river");
        public static final TagKey<Biome> IS_OCEAN = tag("is_ocean");
        public static final TagKey<Biome> IS_DEEP_OCEAN = tag("is_deep_ocean");
        public static final TagKey<Biome> IS_SHALLOW_OCEAN = tag("is_shallow_ocean");

        public static final TagKey<Biome> IS_UNDERGROUND = tag("is_underground");
        public static final TagKey<Biome> IS_CAVE = tag("is_cave");

        public static final TagKey<Biome> IS_WASTELAND = tag("is_wasteland");
        public static final TagKey<Biome> IS_DEAD = tag("is_dead");
        public static final TagKey<Biome> IS_LUSH = tag("is_lush");
        public static final TagKey<Biome> IS_MAGICAL = tag("is_magical");
        public static final TagKey<Biome> IS_RARE = tag("is_rare");
        public static final TagKey<Biome> IS_PLATEAU = tag("is_plateau");
        public static final TagKey<Biome> IS_SPOOKY = tag("is_spooky");
        public static final TagKey<Biome> IS_FLORAL = tag("is_floral");
        public static final TagKey<Biome> IS_SANDY = tag("is_sandy");
        public static final TagKey<Biome> IS_SNOWY = tag("is_snowy");
        public static final TagKey<Biome> IS_ICY = tag("is_icy");
        public static final TagKey<Biome> IS_AQUATIC = tag("is_aquatic");
        public static final TagKey<Biome> IS_AQUATIC_ICY = tag("is_aquatic_icy");

        public static final TagKey<Biome> IS_NETHER = tag("is_nether");
        public static final TagKey<Biome> IS_NETHER_FOREST = tag("is_nether_forest");

        public static final TagKey<Biome> IS_END = tag("is_end");
        public static final TagKey<Biome> IS_OUTER_END_ISLAND = tag("is_outer_end_island");

        private static TagKey<Biome> tag(String path) {
            return create(Registries.BIOME, path);
        }
    }

    public static final class Structures {
        private Structures() {
        }

        public static final TagKey<Structure> HIDDEN_FROM_DISPLAYERS = tag("hidden_from_displayers");

        public static final TagKey<Structure> HIDDEN_FROM_LOCATOR_SELECTION = tag("hidden_from_locator_selection");

        private static TagKey<Structure> tag(String path) {
            return create(Registries.STRUCTURE, path);
        }
    }

    public static final class Potions {
        private Potions() {
        }

        public static final TagKey<Potion> HIDDEN_FROM_RECIPE_VIEWERS = tag("hidden_from_recipe_viewers");

        private static TagKey<Potion> tag(String path) {
            return create(Registries.POTION, path);
        }
    }
}
