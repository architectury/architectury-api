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
import net.minecraft.world.item.enchantment.Enchantment;

/**
  * Convention Tags for enchantments.<br>
  * <b style="color:red;">WARNING! This class will not work on Forge!</b>
  * @see net.fabricmc.fabric.api.tag.convention.v2.ConventionalEnchantmentTags
  * @see net.neoforged.neoforge.common.Tags.Enchantments
  */
@SuppressWarnings("unused")
public class EnchantmentTags {
    public static TagKey<Enchantment> INCREASE_BLOCK_DROPS = impl_INCREASE_BLOCK_DROPS();
    public static TagKey<Enchantment> INCREASE_ENTITY_DROPS = impl_INCREASE_ENTITY_DROPS();
    public static TagKey<Enchantment> WEAPON_DAMAGE_ENHANCEMENTS = impl_WEAPON_DAMAGE_ENHANCEMENTS();
    public static TagKey<Enchantment> ENTITY_SPEED_ENHANCEMENTS = impl_ENTITY_SPEED_ENHANCEMENTS();
    public static TagKey<Enchantment> ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS = impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS();
    // Fabric only
    public static TagKey<Enchantment> ENTITY_DEFENSE_ENHANCEMENT = impl_ENTITY_DEFENSE_ENHANCEMENT();
    // NeoForge only
    public static TagKey<Enchantment> ENTITY_DEFENSE_ENHANCEMENTS = impl_ENTITY_DEFENSE_ENHANCEMENTS();
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_INCREASE_BLOCK_DROPS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_INCREASE_ENTITY_DROPS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_WEAPON_DAMAGE_ENHANCEMENTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_SPEED_ENHANCEMENTS() {
        throw new AssertionError();
    }
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_AUXILIARY_MOVEMENT_ENHANCEMENTS() {
        throw new AssertionError();
    }
    // Returns null on NeoForge
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENT() {
        throw new AssertionError();
    }
    // Returns null on Fabric
    @PlatformOnly({"fabric","neoforge"})
    @ExpectPlatform
    private static TagKey<Enchantment> impl_ENTITY_DEFENSE_ENHANCEMENTS() {
        throw new AssertionError();
    }
}