/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

package me.shedaniel.architectury.mixin.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagCollection;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Supplier;

public class TagHooksImpl {
    public static <T> Tag.Named<T> getOptional(ResourceLocation id, Supplier<TagCollection<T>> collection) {
        return new Tag.Named<T>() {
            private volatile Tag<T> backend;
            private volatile WeakReference<TagCollection<T>> backendCollection;
            
            @Override
            public ResourceLocation getName() {
                return id;
            }
    
            @Override
            public boolean contains(T object) {
                return getBackend().contains(object);
            }
    
            @Override
            public List<T> getValues() {
                return getBackend().getValues();
            }
            
            private Tag<T> getBackend() {
                TagCollection<T> currentCollection = collection.get();
    
                if (backend == null || backendCollection == null || backendCollection.get() != currentCollection) { // If not initialized or was tag changed.
                    backendCollection = new WeakReference<>(currentCollection);
                    return backend = currentCollection.getTagOrEmpty(id);
                } else {
                    return backend;
                }
            }
        };
    }
}
