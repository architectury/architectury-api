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
import dev.architectury.core.RegistryEntry;
import dev.architectury.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.FireworkRocketRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.Objects;

public class TestRecipeSerializer extends RegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CustomRecipe> {
    public TestRecipeSerializer() {
        if (Platform.isForge() && !Objects.equals(getRegistryType(), RecipeSerializer.class)) {
            throw new IllegalStateException("getRegistryType() must be of type " + RecipeSerializer.class.getName());
        }
    }
    
    @Override
    public CustomRecipe fromJson(ResourceLocation id, JsonObject json) {
        return new FireworkRocketRecipe(id);
    }
    
    @Override
    public CustomRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return new FireworkRocketRecipe(id);
    }
    
    @Override
    public void toNetwork(FriendlyByteBuf buf, CustomRecipe recipe) {
    }
}
