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

package dev.architectury.registry.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.architectury.platform.hooks.EventBusesHooks;
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.utils.ArchitecturyConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabRegistryImpl {
    private static final Logger LOGGER = LogManager.getLogger(CreativeTabRegistryImpl.class);
    
    private static final List<Consumer<BuildCreativeModeTabContentsEvent>> BUILD_CONTENTS_LISTENERS = new ArrayList<>();
    private static final Multimap<TabKey, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();
    
    static {
        EventBusesHooks.whenAvailable(ArchitecturyConstants.MOD_ID, bus -> {
            bus.addListener(CreativeTabRegistryImpl::event);
        });
        
        BUILD_CONTENTS_LISTENERS.add(event -> {
            for (Map.Entry<TabKey, Collection<Supplier<ItemStack>>> keyEntry : APPENDS.asMap().entrySet()) {
                Supplier<List<ItemStack>> stacks = Suppliers.memoize(() -> keyEntry.getValue().stream()
                        .map(Supplier::get)
                        .toList());
                if (keyEntry.getKey() instanceof TabKey.SupplierTabKey supplierTabKey) {
                    if (Objects.equals(CreativeModeTabRegistry.getName(event.getTab()), supplierTabKey.supplier().getId())) {
                        for (ItemStack stack : stacks.get()) {
                            event.getEntries().put(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                    }
                } else if (keyEntry.getKey() instanceof TabKey.DirectTabKey directTabKey) {
                    if (event.getTab().equals(directTabKey.tab())) {
                        for (ItemStack stack : stacks.get()) {
                            event.getEntries().put(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                    }
                }
            }
        });
    }
    
    public static void event(BuildCreativeModeTabContentsEvent event) {
        for (Consumer<BuildCreativeModeTabContentsEvent> listener : BUILD_CONTENTS_LISTENERS) {
            listener.accept(event);
        }
    }
    
    @ApiStatus.Experimental
    public static CreativeModeTab create(Consumer<CreativeModeTab.Builder> callback) {
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        callback.accept(builder);
        return builder.build();
    }
    
    @ApiStatus.Experimental
    public static DeferredSupplier<CreativeModeTab> ofBuiltin(CreativeModeTab tab) {
        ResourceLocation key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key == null) {
            throw new IllegalArgumentException("Builtin tab %s is not registered!".formatted(tab));
        }
        return new DeferredSupplier<>() {
            @Override
            public ResourceLocation getRegistryId() {
                return Registries.CREATIVE_MODE_TAB.location();
            }
            
            @Override
            public ResourceLocation getId() {
                return BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
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
    public static DeferredSupplier<CreativeModeTab> defer(ResourceLocation name) {
        return new DeferredSupplier<>() {
            @Nullable
            private CreativeModeTab tab;
            
            @Override
            public ResourceLocation getRegistryId() {
                return Registries.CREATIVE_MODE_TAB.location();
            }
            
            @Override
            public ResourceLocation getId() {
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
                    this.tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(name);
                }
            }
        };
    }
    
    public static void modify(DeferredSupplier<CreativeModeTab> tab, CreativeTabRegistry.ModifyTabCallback filler) {
        BUILD_CONTENTS_LISTENERS.add(event -> {
            if (tab.isPresent()) {
                if (event.getTab().equals(tab.get())) {
                    filler.accept(event.getFlags(), wrapTabOutput(event.getEntries()), event.hasPermissions());
                }
            } else if (Objects.equals(CreativeModeTabRegistry.getName(event.getTab()), tab.getId())) {
                filler.accept(event.getFlags(), wrapTabOutput(event.getEntries()), event.hasPermissions());
            }
        });
    }
    
    private static CreativeTabOutput wrapTabOutput(MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
        return new CreativeTabOutput() {
            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (after.isEmpty()) {
                    entries.put(stack, visibility);
                } else {
                    entries.putAfter(after, stack, visibility);
                }
            }
            
            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (before.isEmpty()) {
                    entries.put(stack, visibility);
                } else {
                    entries.putBefore(before, stack, visibility);
                }
            }
        };
    }
    
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack> item) {
        APPENDS.put(new TabKey.SupplierTabKey(tab), item);
    }
    
    private interface TabKey {
        record SupplierTabKey(DeferredSupplier<CreativeModeTab> supplier) implements TabKey {
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof SupplierTabKey that)) return false;
                return Objects.equals(supplier.getId(), that.supplier.getId());
            }
            
            @Override
            public int hashCode() {
                return Objects.hash(supplier.getId());
            }
        }
        
        record DirectTabKey(CreativeModeTab tab) implements TabKey {
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof DirectTabKey that)) return false;
                return tab == that.tab;
            }
            
            @Override
            public int hashCode() {
                return System.identityHashCode(tab);
            }
        }
    }
}
