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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

/**
 * Should be moved to the client package in version 2.0
 */
@Environment(EnvType.CLIENT)
public interface TextureStitchEvent {
    /**
     * @see Pre#stitch(TextureAtlas, Consumer)
     */
    Event<Pre> PRE = EventFactory.createLoop();
    /**
     * @see Post#stitch(TextureAtlas)
     */
    Event<Post> POST = EventFactory.createLoop();
    
    @Environment(EnvType.CLIENT)
    interface Pre {
        /**
         * Called before the texture atlas is stitched.
         * Equal to the forge {@code TextureStitchEvent.Pre} event.
         *
         * @param atlas The TextureAtlas.
         * @param spriteAdder A consumer where you can add your own sprites to be stitched.
         */
        void stitch(TextureAtlas atlas, Consumer<ResourceLocation> spriteAdder);
    }
    
    @Environment(EnvType.CLIENT)
    interface Post {
        /**
         * Called after the texture atlas has been completely stitched.
         * Equal to the forge {@code TextureStitchEvent.Post} event.
         *
         * @param atlas The ready-to-use TextureAtlas.
         */
        void stitch(TextureAtlas atlas);
    }
}
