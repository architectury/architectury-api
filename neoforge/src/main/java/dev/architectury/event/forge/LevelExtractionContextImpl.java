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

package dev.architectury.event.forge;

import dev.architectury.event.events.client.ClientLevelRenderEvent;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;

final class LevelExtractionContextImpl implements ClientLevelRenderEvent.ExtractionContext {
    private final ExtractLevelRenderStateEvent event;
    
    LevelExtractionContextImpl(ExtractLevelRenderStateEvent event) {
        this.event = event;
    }
    
    @Override
    public LevelRenderer levelRenderer() {
        return event.getLevelRenderer();
    }
    
    @Override
    public LevelRenderState levelRenderState() {
        return event.getRenderState();
    }
    
    @Override
    public ClientLevel level() {
        return event.getLevel();
    }
    
    @Override
    public Camera camera() {
        return event.getCamera();
    }
    
    @Override
    public DeltaTracker deltaTracker() {
        return event.getDeltaTracker();
    }
}
