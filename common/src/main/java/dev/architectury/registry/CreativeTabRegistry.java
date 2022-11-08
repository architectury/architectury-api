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

package dev.architectury.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CreativeTabRegistry {
    private CreativeTabRegistry() {
    }
    
    // I am sorry, fabric wants a resource location instead of the translation key for whatever reason
    public static CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon) {
        return create(name, icon, (flags, output, canUseGameMasterBlocks) -> {
        });
    }
    
    // I am sorry, fabric wants a resource location instead of the translation key for whatever reason
    @ExpectPlatform
    @ApiStatus.Experimental
    public static CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon, CreativeTabFiller filler) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void modify(CreativeModeTab tab, Consumer<CreativeModeTab.Output> filler) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void append(CreativeModeTab tab, ItemLike item) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void append(CreativeModeTab tab, ItemLike... items) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(CreativeModeTab tab, T item) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(CreativeModeTab tab, T... items) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void appendStack(CreativeModeTab tab, ItemStack item) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void appendStack(CreativeModeTab tab, ItemStack... items) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static <I extends ItemStack, T extends Supplier<I>> void appendStack(CreativeModeTab tab, T item) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static <I extends ItemStack, T extends Supplier<I>> void appendStack(CreativeModeTab tab, T... items) {
        throw new AssertionError();
    }
    
    public interface CreativeTabFiller {
        void fill(FeatureFlagSet flags, CreativeModeTab.Output output, boolean canUseGameMasterBlocks);
    }
}
