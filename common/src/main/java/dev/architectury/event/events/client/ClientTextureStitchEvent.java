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

package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface ClientTextureStitchEvent {
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
         * Invoked before the texture atlas is stitched together.
         * Equivalent to Forge's {@code TextureStitchEvent.Pre} event.
         *
         * @param atlas       The TextureAtlas.
         * @param spriteAdder A consumer where you can add your own sprites to be stitched.
         */
        void stitch(TextureAtlas atlas, Consumer<ResourceLocation> spriteAdder);
    }
    
    @Environment(EnvType.CLIENT)
    interface Post {
        /**
         * Invoked after the texture atlas has been fully stitched.
         * Equivalent to Forge's {@code TextureStitchEvent.Post} event.
         *
         * @param atlas The ready-to-use TextureAtlas.
         */
        void stitch(TextureAtlas atlas);
    }
}
