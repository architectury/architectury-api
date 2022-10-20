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

package dev.architectury.test.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Objects;

public class TestRecipeSerializer implements RecipeSerializer<CustomRecipe> {
    @Override
    public CustomRecipe fromJson(ResourceLocation id, JsonObject json) {
        CraftingBookCategory category = Objects.requireNonNullElse(
                CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null)), CraftingBookCategory.MISC);
        return new FireworkRocketRecipe(id, category);
    }
    
    @Override
    public CustomRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
        return new FireworkRocketRecipe(id, category);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, CustomRecipe recipe) {
        buf.writeEnum(recipe.category());
    }
}
