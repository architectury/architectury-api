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
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.CreativeTabRegistry.TabSupplier;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabRegistryImpl {
    private static final Multimap<CreativeModeTab, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();
    
    @ApiStatus.Experimental
    public static TabSupplier create(ResourceLocation name, Consumer<CreativeModeTab.Builder> callback) {
        CreativeModeTab.Builder builder = FabricItemGroup.builder(name);
        callback.accept(builder);
        CreativeModeTab tab = builder.build();
        return new TabSupplier() {
            @Override
            public ResourceLocation getName() {
                return name;
            }
            
            @Override
            public CreativeModeTab get() {
                return tab;
            }
            
            @Override
            public boolean isPresent() {
                return true;
            }
        };
    }
    
    @ApiStatus.Experimental
    public static TabSupplier of(CreativeModeTab tab) {
        return new TabSupplier() {
            @Override
            public ResourceLocation getName() {
                return tab.getId();
            }
            
            @Override
            public boolean isPresent() {
                return true;
            }
            
            @Override
            public CreativeModeTab get() {
                return tab;
            }
        };
    }
    
    @ApiStatus.Experimental
    public static TabSupplier defer(ResourceLocation name) {
        return new TabSupplier() {
            @Nullable
            private CreativeModeTab tab;
            
            @Override
            public ResourceLocation getName() {
                return name;
            }
            
            @Override
            public CreativeModeTab get() {
                resolve();
                if (tab == null)
                    throw new IllegalStateException("Creative tab %s was not registered yet!".formatted(name));
                return tab;
            }
            
            @Override
            public boolean isPresent() {
                resolve();
                return tab != null;
            }
            
            private void resolve() {
                if (this.tab == null) {
                    for (CreativeModeTab tab : CreativeModeTabs.allTabs()) {
                        if (Objects.equals(tab.getId(), name)) {
                            this.tab = tab;
                            break;
                        }
                    }
                }
            }
        };
    }
    
    static {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((tab, output) -> {
            APPENDS.get(tab).forEach(s -> output.accept(s.get()));
        });
    }
    
    public static void modify(TabSupplier tab, CreativeTabRegistry.ModifyTabCallback filler) {
        ItemGroupEvents.modifyEntriesEvent(tab.get()).register(entries -> {
            filler.accept(entries.getEnabledFeatures(), new CreativeTabOutput() {
                @Override
                public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                    if (after.isEmpty()) {
                        entries.accept(stack, visibility);
                    } else {
                        entries.addAfter(after, List.of(stack), visibility);
                    }
                }
                
                @Override
                public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                    if (before.isEmpty()) {
                        entries.accept(stack, visibility);
                    } else {
                        entries.addBefore(before, List.of(stack), visibility);
                    }
                }
            }, entries.shouldShowOpRestrictedItems());
        });
    }
    
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, Supplier<ItemStack> item) {
        APPENDS.put(tab.get(), item);
    }
}
