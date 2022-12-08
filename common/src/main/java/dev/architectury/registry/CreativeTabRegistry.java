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

import dev.architectury.extensions.injected.InjectedItemPropertiesExtension;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Registry for creating or modifying creative tabs.
 *
 * @see InjectedItemPropertiesExtension#arch$tab(CreativeModeTab) to add an item to a creative tab easily
 */
public final class CreativeTabRegistry {
    private CreativeTabRegistry() {
    }
    
    /**
     * Creates a deferred creative tab, with a custom icon.
     * <p>
     * On fabric, the supplier is always resolvable. On forge, the supplier is only
     * resolvable after the registration of the creative tab.
     *
     * @param name the name of the creative tab
     * @param icon the icon of the creative tab
     * @return the creative tab supplier
     */
    public static TabSupplier create(ResourceLocation name, Supplier<ItemStack> icon) {
        return create(name, builder -> {
            builder.icon(icon);
        });
    }
    
    
    /**
     * Creates a deferred creative tab, with a configurable builder callback.
     * <p>
     * On fabric, the supplier is always resolvable. On forge, the supplier is only
     * resolvable after the registration of the creative tab.
     *
     * @param name     the name of the creative tab
     * @param callback the builder callback
     * @return the creative tab supplier
     */
    @ExpectPlatform
    @ApiStatus.Experimental
    public static TabSupplier create(ResourceLocation name, Consumer<CreativeModeTab.Builder> callback) {
        throw new AssertionError();
    }
    
    /**
     * Returns a tab supplier for a tab.
     *
     * @param tab the tab
     * @return the tab supplier
     */
    @ExpectPlatform
    @ApiStatus.Experimental
    public static TabSupplier of(CreativeModeTab tab) {
        throw new AssertionError();
    }
    
    /**
     * Returns a tab supplier for a tab to be created later.
     *
     * @param name the name of the creative tab
     * @return the tab supplier
     */
    @ExpectPlatform
    @ApiStatus.Experimental
    public static TabSupplier defer(ResourceLocation name) {
        throw new AssertionError();
    }
    
    @ApiStatus.Experimental
    public static void modify(CreativeModeTab tab, ModifyTabCallback filler) {
        modify(of(tab), filler);
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void modify(TabSupplier tab, ModifyTabCallback filler) {
        throw new AssertionError();
    }
    
    @ApiStatus.Experimental
    public static void append(CreativeModeTab tab, ItemLike... items) {
        append(of(tab), items);
    }
    
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(CreativeModeTab tab, T... items) {
        appendStack(of(tab), Stream.of(items).map(supplier -> () -> new ItemStack(supplier.get())));
    }
    
    @ApiStatus.Experimental
    public static void appendStack(CreativeModeTab tab, ItemStack... items) {
        appendStack(of(tab), items);
    }
    
    @ApiStatus.Experimental
    public static void appendStack(CreativeModeTab tab, Supplier<ItemStack>... items) {
        appendStack(of(tab), items);
    }
    
    @ApiStatus.Experimental
    public static void append(TabSupplier tab, ItemLike... items) {
        appendStack(tab, Stream.of(items).map(item -> () -> new ItemStack(item)));
    }
    
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(TabSupplier tab, T... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> new ItemStack(supplier.get())));
    }
    
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, ItemStack... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> supplier));
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, Supplier<ItemStack> item) {
        throw new AssertionError();
    }
    
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, Supplier<ItemStack>... items) {
        for (Supplier<ItemStack> item : items) {
            appendStack(tab, item);
        }
    }
    
    @ApiStatus.Experimental
    public static void appendStack(TabSupplier tab, Stream<Supplier<ItemStack>> items) {
        items.forEach(item -> appendStack(tab, item));
    }
    
    @FunctionalInterface
    public interface ModifyTabCallback {
        void accept(FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks);
    }
    
    @ApiStatus.NonExtendable
    public interface TabSupplier extends Supplier<CreativeModeTab> {
        /**
         * Returns the name of the creative tab.
         *
         * @return The name of the creative tab.
         */
        ResourceLocation getName();
        
        /**
         * @return whether the creative tab is registered.
         */
        boolean isPresent();
    }
}
