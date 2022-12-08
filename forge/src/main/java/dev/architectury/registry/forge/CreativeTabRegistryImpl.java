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
import dev.architectury.forge.ArchitecturyForge;
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.CreativeTabRegistry.TabSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ArchitecturyForge.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CreativeTabRegistryImpl {
    private static final Logger LOGGER = LogManager.getLogger(CreativeTabRegistryImpl.class);
    
    @Nullable
    private static List<Consumer<CreativeModeTabEvent.Register>> registerListeners = new ArrayList<>();
    private static final List<Consumer<CreativeModeTabEvent.BuildContents>> BUILD_CONTENTS_LISTENERS = new ArrayList<>();
    private static final Multimap<TabKey, Supplier<ItemStack>> APPENDS = MultimapBuilder.hashKeys().arrayListValues().build();
    
    static {
        BUILD_CONTENTS_LISTENERS.add(event -> {
            for (Map.Entry<TabKey, Collection<Supplier<ItemStack>>> keyEntry : APPENDS.asMap().entrySet()) {
                Supplier<List<ItemStack>> stacks = Suppliers.memoize(() -> keyEntry.getValue().stream()
                        .map(Supplier::get)
                        .toList());
                if (keyEntry.getKey() instanceof TabKey.SupplierTabKey supplierTabKey) {
                    event.register(supplierTabKey.supplier.getName(), (arg, populator, bl) -> {
                        populator.acceptAll(stacks.get());
                    });
                } else if (keyEntry.getKey() instanceof TabKey.DirectTabKey directTabKey) {
                    event.register(directTabKey.tab, (arg, populator, bl) -> {
                        populator.acceptAll(stacks.get());
                    });
                }
            }
        });
    }
    
    @SubscribeEvent
    public static void event(CreativeModeTabEvent.Register event) {
        if (registerListeners != null) {
            for (Consumer<CreativeModeTabEvent.Register> listener : registerListeners) {
                listener.accept(event);
            }
            registerListeners = null;
        } else {
            LOGGER.warn("Creative tab listeners were already registered!");
        }
    }
    
    @SubscribeEvent
    public static void event(CreativeModeTabEvent.BuildContents event) {
        for (Consumer<CreativeModeTabEvent.BuildContents> listener : BUILD_CONTENTS_LISTENERS) {
            listener.accept(event);
        }
    }
    
    public static TabSupplier create(ResourceLocation name, Consumer<CreativeModeTab.Builder> callback) {
        if (registerListeners == null) {
            throw new IllegalStateException("Creative tab listeners were already registered!");
        }
        CreativeModeTab[] tab = new CreativeModeTab[1];
        registerListeners.add(register -> {
            tab[0] = register.registerCreativeModeTab(name, builder -> {
                builder.title(Component.translatable("itemGroup.%s.%s".formatted(name.getNamespace(), name.getPath())));
                callback.accept(builder);
            });
        });
        return new TabSupplier() {
            @Override
            public ResourceLocation getName() {
                return name;
            }
            
            @Override
            public CreativeModeTab get() {
                if (tab[0] == null) {
                    throw new IllegalStateException("Creative tab %s was not registered yet!".formatted(name));
                }
                
                return tab[0];
            }
            
            @Override
            public boolean isPresent() {
                return tab[0] != null;
            }
        };
    }
    
    @ApiStatus.Experimental
    public static TabSupplier of(CreativeModeTab tab) {
        ResourceLocation location = CreativeModeTabRegistry.getName(tab);
        if (location == null) {
            throw new IllegalArgumentException("Tab %s is not registered!".formatted(tab));
        }
        return new TabSupplier() {
            @Override
            public ResourceLocation getName() {
                return location;
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
            @Override
            public ResourceLocation getName() {
                return name;
            }
            
            @Override
            public boolean isPresent() {
                return CreativeModeTabRegistry.getTab(name) != null;
            }
            
            @Override
            public CreativeModeTab get() {
                CreativeModeTab tab = CreativeModeTabRegistry.getTab(name);
                if (tab == null) {
                    throw new IllegalStateException("Creative tab %s was not registered yet!".formatted(name));
                } else {
                    return tab;
                }
            }
        };
    }
    
    public static void modify(TabSupplier tab, CreativeTabRegistry.ModifyTabCallback filler) {
        BUILD_CONTENTS_LISTENERS.add(event -> {
            if (tab.isPresent()) {
                event.register(tab.get(), (flags, populator, canUseGameMasterBlocks) -> {
                    filler.accept(flags, wrapTabOutput(populator), canUseGameMasterBlocks);
                });
            } else {
                event.register(tab.getName(), (flags, populator, canUseGameMasterBlocks) -> {
                    filler.accept(flags, wrapTabOutput(populator), canUseGameMasterBlocks);
                });
            }
        });
    }
    
    private static CreativeTabOutput wrapTabOutput(CreativeModeTabEvent.CreativeModeTabPopulator populator) {
        return new CreativeTabOutput() {
            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                populator.accept(stack, visibility, ItemStack.EMPTY, after);
            }
            
            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                populator.accept(stack, visibility, before, ItemStack.EMPTY);
            }
        };
    }
    
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, Supplier<ItemStack> item) {
        APPENDS.put(new TabKey.SupplierTabKey(tab), item);
    }
    
    private interface TabKey {
        record SupplierTabKey(TabSupplier supplier) implements TabKey {
            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof SupplierTabKey that)) return false;
                return Objects.equals(supplier.getName(), that.supplier.getName());
            }
            
            @Override
            public int hashCode() {
                return Objects.hash(supplier.getName());
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
