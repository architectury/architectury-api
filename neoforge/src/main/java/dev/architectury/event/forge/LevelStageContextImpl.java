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

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.events.client.ClientLevelRenderEvent;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

final class LevelStageContextImpl implements ClientLevelRenderEvent.StageContext {
    private final RenderLevelStageEvent event;
    
    LevelStageContextImpl(RenderLevelStageEvent event) {
        this.event = event;
    }
    
    @Override
    public LevelRenderer levelRenderer() {
        return event.getLevelRenderer();
    }
    
    @Override
    public LevelRenderState levelRenderState() {
        return event.getLevelRenderState();
    }
    
    @Override
    public PoseStack poseStack() {
        return event.getPoseStack();
    }
}
