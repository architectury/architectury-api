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

package dev.architectury.registry.fabric;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.Output;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Experimental;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabRegistryImpl {
    private static final Multimap<CreativeModeTab, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();
    
    @ApiStatus.Experimental
    public static CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon, BiConsumer<FeatureFlagSet, Output> filler) {
        return new FabricItemGroup(name) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
            
            @Override
            protected void generateDisplayItems(FeatureFlagSet flags, Output output) {
                filler.accept(flags, output);
            }
        };
    }
    
    static {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, output) -> {
            APPENDS.get(tab).forEach(s -> output.accept(s.get()));
        });
    }
    
    public static void modify(CreativeModeTab tab, Consumer<Output> filler) {
        ItemGroupEvents.modifyEntriesEvent(tab).register(filler::accept);
    }
    
    @Experimental
    public static void append(CreativeModeTab tab, ItemLike item) {
        APPENDS.put(tab, () -> new ItemStack(item));
    }
    
    @Experimental
    public static void append(CreativeModeTab tab, ItemLike... items) {
        for (ItemLike item : items) {
            append(tab, item);
        }
    }
    
    @Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(CreativeModeTab tab, T item) {
        APPENDS.put(tab, () -> new ItemStack(item.get()));
    }
    
    @Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(CreativeModeTab tab, T... items) {
        for (T item : items) {
            append(tab, item);
        }
    }
    
    @Experimental
    public static void appendStack(CreativeModeTab tab, ItemStack item) {
        APPENDS.put(tab, () -> item);
    }
    
    @Experimental
    public static void appendStack(CreativeModeTab tab, ItemStack... items) {
        for (ItemStack item : items) {
            appendStack(tab, item);
        }
    }
    
    @Experimental
    public static <I extends ItemStack, T extends Supplier<I>> void appendStack(CreativeModeTab tab, T item) {
        APPENDS.put(tab, (Supplier<ItemStack>) item);
    }
    
    @Experimental
    public static <I extends ItemStack, T extends Supplier<I>> void appendStack(CreativeModeTab tab, T... items) {
        for (T item : items) {
            appendStack(tab, item);
        }
    }
}
