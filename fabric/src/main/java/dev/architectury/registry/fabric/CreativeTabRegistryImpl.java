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
import dev.architectury.registry.registries.DeferredSupplier;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreativeTabRegistryImpl {
    private static final Multimap<Identifier, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();
    
    @ApiStatus.Experimental
    public static CreativeModeTab create(Consumer<CreativeModeTab.Builder> callback) {
        CreativeModeTab.Builder builder = FabricCreativeModeTab.builder();
        callback.accept(builder);
        return builder.build();
    }
    
    @ApiStatus.Experimental
    public static DeferredSupplier<CreativeModeTab> ofBuiltin(CreativeModeTab tab) {
        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        if (key == null) {
            throw new IllegalArgumentException("Builtin tab %s is not registered!".formatted(tab));
        }
        return new DeferredSupplier<>() {
            @Override
            public Identifier getRegistryId() {
                return Registries.CREATIVE_MODE_TAB.identifier();
            }
            
            @Override
            public Identifier getId() {
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
    public static DeferredSupplier<CreativeModeTab> defer(Identifier name) {
        return new DeferredSupplier<>() {
            @Nullable
            private CreativeModeTab tab;
            
            @Override
            public Identifier getRegistryId() {
                return Registries.CREATIVE_MODE_TAB.identifier();
            }
            
            @Override
            public Identifier getId() {
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
                    this.tab = BuiltInRegistries.CREATIVE_MODE_TAB.getValue(name);
                }
            }
        };
    }
    
    static {
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, output) -> {
            APPENDS.get(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab)).forEach(s -> output.accept(s.get()));
        });
    }
    
    public static void modify(DeferredSupplier<CreativeModeTab> tab, CreativeTabRegistry.ModifyTabCallback filler) {
        CreativeModeTabEvents.modifyOutputEvent(tab.getKey()).register(entries -> {
            filler.accept(entries.getEnabledFeatures(), new CreativeTabOutput() {
                @Override
                public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                    if (after.isEmpty()) {
                        entries.accept(stack, visibility);
                    } else {
                        entries.insertAfter(after, List.of(stack), visibility);
                    }
                }
                
                @Override
                public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                    if (before.isEmpty()) {
                        entries.accept(stack, visibility);
                    } else {
                        entries.insertBefore(before, List.of(stack), visibility);
                    }
                }
            }, entries.shouldShowOpRestrictedItems());
        });
    }
    
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack> item) {
        APPENDS.put(tab.getId(), item);
    }
}
