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

package dev.architectury.hooks.tags;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.mixin.FluidTagsAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public final class TagHooks {
    private TagHooks() {
    }
    
    @ExpectPlatform
    public static <T> Tag.Named<T> optional(ResourceLocation id, Supplier<TagCollection<T>> collection) {
        throw new AssertionError();
    }
    
    public static Tag.Named<Item> optionalItem(ResourceLocation id) {
        return optional(id, ItemTags::getAllTags);
    }
    
    public static Tag.Named<Block> optionalBlock(ResourceLocation id) {
        return optional(id, BlockTags::getAllTags);
    }
    
    public static Tag.Named<Fluid> optionalFluid(ResourceLocation id) {
        return optional(id, FluidTagsAccessor.getHelper()::getAllTags);
    }
    
    public static Tag.Named<EntityType<?>> optionalEntityType(ResourceLocation id) {
        return optional(id, EntityTypeTags::getAllTags);
    }
}
