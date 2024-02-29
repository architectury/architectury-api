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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;

public class TestRecipeSerializer implements RecipeSerializer<CustomRecipe> {
    private static final Codec<CustomRecipe> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(CraftingBookCategory.CODEC.fieldOf("category")
                    .orElse(CraftingBookCategory.MISC)
                    .forGetter(CraftingRecipe::category)
            ).apply(instance, FireworkRocketRecipe::new)
    );
    private static final StreamCodec<RegistryFriendlyByteBuf, CustomRecipe> STREAM_CODEC = StreamCodec.of(TestRecipeSerializer::toNetwork, TestRecipeSerializer::fromNetwork);
    
    @Override
    public Codec<CustomRecipe> codec() {
        return CODEC;
    }
    
    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CustomRecipe> streamCodec() {
        return STREAM_CODEC;
    }
    
    public static CustomRecipe fromNetwork(FriendlyByteBuf buf) {
        CraftingBookCategory category = buf.readEnum(CraftingBookCategory.class);
        return new FireworkRocketRecipe(category);
    }
    
    public static void toNetwork(FriendlyByteBuf buf, CustomRecipe recipe) {
        buf.writeEnum(recipe.category());
    }
}
