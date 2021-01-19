package me.shedaniel.architectury.core;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * The equivalent of {@link RecipeSerializer} to use in common that has forge registry entries extended.
 */
public abstract class AbstractRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
}
