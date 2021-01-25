/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 shedaniel
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

import com.mojang.blaze3d.vertex.PoseStack;
import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface GuiEvent {
    /**
     * Invoked after in-game hud is rendered, equivalent to forge's {@code RenderGameOverlayEvent.Post@ElementType#ALL} and fabric's {@code HudRenderCallback}.
     */
    Event<RenderHud> RENDER_HUD = EventFactory.createLoop(RenderHud.class);
    Event<DebugText> DEBUG_TEXT_LEFT = EventFactory.createLoop(DebugText.class);
    Event<DebugText> DEBUG_TEXT_RIGHT = EventFactory.createLoop(DebugText.class);
    /**
     * Invoked during Screen#init after previous widgets are cleared, equivalent to forge's {@code GuiScreenEvent.InitGuiEvent.Pre}.
     */
    Event<ScreenInitPre> INIT_PRE = EventFactory.createInteractionResult(ScreenInitPre.class);
    /**
     * Invoked after Screen#init, equivalent to forge's {@code GuiScreenEvent.InitGuiEvent.Post}.
     */
    Event<ScreenInitPost> INIT_POST = EventFactory.createLoop(ScreenInitPost.class);
    Event<ScreenRenderPre> RENDER_PRE = EventFactory.createInteractionResult(ScreenRenderPre.class);
    Event<ScreenRenderPost> RENDER_POST = EventFactory.createInteractionResult(ScreenRenderPost.class);

    /**
     * Invoked during Minecraft#setScreen, equivalent to forge's {@code GuiOpenEvent}.
     */
    Event<SetScreenEvent> SET_SCREEN = EventFactory.createInteractionResultHolder();

    @Environment(EnvType.CLIENT)
    interface SetScreenEvent {
        InteractionResultHolder<Screen> modifyScreen(Screen screen);
    }

    @Environment(EnvType.CLIENT)
    interface RenderHud {
        void renderHud(PoseStack matrices, float tickDelta);
    }

    @Environment(EnvType.CLIENT)
    interface DebugText {
        void gatherText(List<String> strings);
    }

    @Environment(EnvType.CLIENT)
    interface ScreenInitPre {
        InteractionResult init(Screen screen, List<AbstractWidget> widgets, List<GuiEventListener> children);
    }

    @Environment(EnvType.CLIENT)
    interface ScreenInitPost {
        void init(Screen screen, List<AbstractWidget> widgets, List<GuiEventListener> children);
    }

    @Environment(EnvType.CLIENT)
    interface ScreenRenderPre {
        InteractionResult render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }

    @Environment(EnvType.CLIENT)
    interface ScreenRenderPost {
        void render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }
}
