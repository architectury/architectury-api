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

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface CreativeTabOutput extends CreativeModeTab.Output {
    void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility);
    
    void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility);
    
    @Override
    default void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
        acceptAfter(ItemStack.EMPTY, stack, visibility);
    }
    
    default void acceptAfter(ItemStack after, ItemStack stack) {
        this.acceptAfter(after, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptAfter(ItemStack after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptAfter(after, new ItemStack(item), visibility);
    }
    
    default void acceptAfter(ItemStack after, ItemLike item) {
        this.acceptAfter(after, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        stacks.forEach((stack) -> acceptAfter(after, stack, visibility));
    }
    
    default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks) {
        this.acceptAllAfter(after, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptAfter(ItemLike after, ItemStack stack) {
        this.acceptAfter(new ItemStack(after), stack);
    }
    
    default void acceptAfter(ItemLike after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptAfter(new ItemStack(after), item, visibility);
    }
    
    default void acceptAfter(ItemLike after, ItemLike item) {
        this.acceptAfter(new ItemStack(after), item);
    }
    
    default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        acceptAllAfter(new ItemStack(after), stacks, visibility);
    }
    
    default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks) {
        acceptAllAfter(new ItemStack(after), stacks);
    }
    
    default void acceptBefore(ItemStack before, ItemStack stack) {
        this.acceptBefore(before, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptBefore(ItemStack before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptBefore(before, new ItemStack(item), visibility);
    }
    
    default void acceptBefore(ItemStack before, ItemLike item) {
        this.acceptBefore(before, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        stacks.forEach((stack) -> acceptBefore(before, stack, visibility));
    }
    
    default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks) {
        this.acceptAllBefore(before, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }
    
    default void acceptBefore(ItemLike before, ItemStack stack) {
        this.acceptBefore(new ItemStack(before), stack);
    }
    
    default void acceptBefore(ItemLike before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
        this.acceptBefore(new ItemStack(before), item, visibility);
    }
    
    default void acceptBefore(ItemLike before, ItemLike item) {
        this.acceptBefore(new ItemStack(before), item);
    }
    
    default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
        acceptAllBefore(new ItemStack(before), stacks, visibility);
    }
    
    default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks) {
        acceptAllBefore(new ItemStack(before), stacks);
    }
}
