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
import dev.architectury.registry.registries.DeferredSupplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
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
     * Creates a creative tab, with a custom icon.
     * This has to be registered manually.
     *
     * @param title the title of the creative tab
     * @param icon  the icon of the creative tab
     * @return the creative tab
     */
    public static CreativeModeTab create(Component title, Supplier<ItemStack> icon) {
        return create(builder -> {
            builder.title(title);
            builder.icon(icon);
        });
    }
    
    
    /**
     * Creates a creative tab, with a configurable builder callback.
     * This has to be registered manually.
     *
     * @param callback the builder callback
     * @return the creative tab
     */
    @ExpectPlatform
    @ApiStatus.Experimental
    public static CreativeModeTab create(Consumer<CreativeModeTab.Builder> callback) {
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
    public static DeferredSupplier<CreativeModeTab> ofBuiltin(CreativeModeTab tab) {
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
    public static DeferredSupplier<CreativeModeTab> defer(ResourceLocation name) {
        throw new AssertionError();
    }
    
    /**
     * Returns a tab supplier for a tab to be created later.
     *
     * @param name the key of the creative tab
     * @return the tab supplier
     */
    @ApiStatus.Experimental
    public static DeferredSupplier<CreativeModeTab> defer(ResourceKey<CreativeModeTab> name) {
        return defer(name.location());
    }
    
    @ApiStatus.Experimental
    public static void modifyBuiltin(CreativeModeTab tab, ModifyTabCallback filler) {
        modify(ofBuiltin(tab), filler);
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void modify(DeferredSupplier<CreativeModeTab> tab, ModifyTabCallback filler) {
        throw new AssertionError();
    }
    
    @ApiStatus.Experimental
    public static void appendBuiltin(CreativeModeTab tab, ItemLike... items) {
        append(ofBuiltin(tab), items);
    }
    
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void appendBuiltin(CreativeModeTab tab, T... items) {
        appendStack(ofBuiltin(tab), Stream.of(items).map(supplier -> () -> new ItemStack(supplier.get())));
    }
    
    @ApiStatus.Experimental
    public static void appendBuiltinStack(CreativeModeTab tab, ItemStack... items) {
        appendStack(ofBuiltin(tab), items);
    }
    
    @ApiStatus.Experimental
    public static void appendBuiltinStack(CreativeModeTab tab, Supplier<ItemStack>... items) {
        appendStack(ofBuiltin(tab), items);
    }
    
    @ApiStatus.Experimental
    public static void append(DeferredSupplier<CreativeModeTab> tab, ItemLike... items) {
        appendStack(tab, Stream.of(items).map(item -> () -> new ItemStack(item)));
    }
    
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(DeferredSupplier<CreativeModeTab> tab, T... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> new ItemStack(supplier.get())));
    }
    
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, ItemStack... items) {
        appendStack(tab, Stream.of(items).map(supplier -> () -> supplier));
    }
    
    @ExpectPlatform
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack> item) {
        throw new AssertionError();
    }
    
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Supplier<ItemStack>... items) {
        for (Supplier<ItemStack> item : items) {
            appendStack(tab, item);
        }
    }
    
    @ApiStatus.Experimental
    public static void appendStack(DeferredSupplier<CreativeModeTab> tab, Stream<Supplier<ItemStack>> items) {
        items.forEach(item -> appendStack(tab, item));
    }
    
    @ApiStatus.Experimental
    public static void append(ResourceKey<CreativeModeTab> tab, ItemLike... items) {
        appendStack(defer(tab), Stream.of(items).map(item -> () -> new ItemStack(item)));
    }
    
    @ApiStatus.Experimental
    public static <I extends ItemLike, T extends Supplier<I>> void append(ResourceKey<CreativeModeTab> tab, T... items) {
        appendStack(defer(tab), Stream.of(items).map(supplier -> () -> new ItemStack(supplier.get())));
    }
    
    @ApiStatus.Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, ItemStack... items) {
        appendStack(defer(tab), Stream.of(items).map(supplier -> () -> supplier));
    }
    
    @ApiStatus.Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack> item) {
        appendStack(defer(tab), item);
    }
    
    @ApiStatus.Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Supplier<ItemStack>... items) {
        appendStack(defer(tab), items);
    }
    
    @ApiStatus.Experimental
    public static void appendStack(ResourceKey<CreativeModeTab> tab, Stream<Supplier<ItemStack>> items) {
        appendStack(defer(tab), items);
    }
    
    @FunctionalInterface
    public interface ModifyTabCallback {
        void accept(FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks);
    }
}
