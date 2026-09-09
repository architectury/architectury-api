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

package dev.architectury.event.fabric;

import dev.architectury.event.events.client.ClientLevelRenderEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;

@Environment(EnvType.CLIENT)
final class LevelExtractionContextImpl extends LevelRenderContextImpl implements ClientLevelRenderEvent.ExtractionContext {
    private final LevelExtractionContext context;
    
    LevelExtractionContextImpl(LevelExtractionContext context) {
        super(context);
        this.context = context;
    }
    
    @Override
    public ClientLevel level() {
        return context.level();
    }
    
    @Override
    public Camera camera() {
        return context.camera();
    }
    
    @Override
    public DeltaTracker deltaTracker() {
        return context.deltaTracker();
    }
}
