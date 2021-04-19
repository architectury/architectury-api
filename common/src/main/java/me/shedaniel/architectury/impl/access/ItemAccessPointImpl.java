/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
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

package me.shedaniel.architectury.impl.access;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.shedaniel.architectury.core.access.AccessPoint;
import me.shedaniel.architectury.core.access.specific.ItemAccessPoint;
import me.shedaniel.architectury.core.access.specific.ItemAccess;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class ItemAccessPointImpl<T, SELF extends ItemAccessPointImpl<T, SELF>> implements ItemAccessPoint<T, SELF> {
    private final AccessPoint<ItemAccess<T>, ?> parent;
    
    private final Multimap<Item, ItemAccess<T>> itemBasedAccessors = HashMultimap.create();
    private final Multimap<ItemTag, ItemAccess<T>> itemTagBasedAccessors = HashMultimap.create();
    
    public ItemAccessPointImpl() {
        this.parent = AccessPoint.create(ts -> stack -> {
            return processIterable(ts, stack);
        });
        add(stack -> {
            Collection<ItemAccess<T>> accessors = itemBasedAccessors.get(stack.getItem());
            return processIterable(accessors, stack);
        });
        add(stack -> {
            for (Map.Entry<ItemTag, Collection<ItemAccess<T>>> entry : itemTagBasedAccessors.asMap().entrySet()) {
                if (stack.getItem().is(entry.getKey().tag)) {
                    return processIterable(entry.getValue(), stack);
                }
            }
            return null;
        });
    }
    
    @Override
    public SELF forItem(Item item, ItemAccess<T> access) {
        itemBasedAccessors.put(item, access);
        return (SELF) this;
    }
    
    @Override
    public SELF forItem(Tag.Named<Item> tag, ItemAccess<T> access) {
        itemTagBasedAccessors.put(new ItemTag(tag), access);
        return (SELF) this;
    }
    
    private T processIterable(Iterable<ItemAccess<T>> iterable, ItemStack stack) {
        for (ItemAccess<T> accessor : iterable) {
            T t = accessor.getByItem(stack);
            if (t != null) {
                return t;
            }
        }
        
        return null;
    }
    
    @Override
    public AccessPoint<ItemAccess<T>, ?> getParent() {
        return parent;
    }
    
    @Override
    public T getByItem(ItemStack stack) {
        return get().getByItem(stack);
    }
    
    private static class ItemTag {
        private final Tag.Named<Item> tag;
        private final int hash;
        
        public ItemTag(Tag.Named<Item> tag) {
            this.tag = tag;
            this.hash = tag.getName().hashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemTag itemTag = (ItemTag) o;
            return Objects.equals(tag.getName(), itemTag.tag.getName());
        }
        
        @Override
        public int hashCode() {
            return hash;
        }
    }
}
