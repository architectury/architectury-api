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

package me.shedaniel.architectury.event.events;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.crafting.RecipeManager;

@Environment(EnvType.CLIENT)
public interface RecipeUpdateEvent {
    /**
     * @see RecipeUpdateEvent#update(RecipeManager)
     */
    Event<RecipeUpdateEvent> EVENT = EventFactory.createLoop();
    
    /**
     * Invoked when the client has received an updated list of recipes from the server.
     * Equivalent to Forge's {@code RecipesUpdatedEvent} event.
     *
     * @param recipeManager The recipe manager.
     */
    void update(RecipeManager recipeManager);
}
