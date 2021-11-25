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

package me.shedaniel.architectury.registry;

import com.google.common.collect.Maps;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public final class ToolType {
    private static final Map<String, ToolType> TYPES = Maps.newConcurrentMap();
    public static final ToolType PICKAXE = create("pickaxe", ToolType::pickaxeTag);
    public static final ToolType AXE = create("axe", ToolType::axeTag);
    public static final ToolType HOE = create("hoe", ToolType::hoeTag);
    public static final ToolType SHOVEL = create("shovel", ToolType::shovelTag);
    
    @ExpectPlatform
    private static Tag<Item> pickaxeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> axeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> hoeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> shovelTag() {
        throw new AssertionError();
    }
    
    public final String forgeName;
    public final Supplier<Tag<Item>> fabricTag;
    private Object obj;
    
    private ToolType(String forgeName, Supplier<Tag<Item>> fabricTag) {
        this.forgeName = forgeName;
        this.fabricTag = fabricTag;
    }
    
    public static ToolType create(String forgeName, Supplier<Tag<Item>> fabricTag) {
        return TYPES.computeIfAbsent(forgeName, s -> new ToolType(s, fabricTag));
    }
    
    public static ToolType byName(String forgeName) {
        return TYPES.get(forgeName);
    }
}
