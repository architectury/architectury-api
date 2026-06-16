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

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.hooks.client.screen.ScreenAccess;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface ClientGuiEvent {
    /**
     * @see RenderHud#renderHud(GuiGraphicsExtractor, DeltaTracker)
     */
    Event<RenderHud> RENDER_HUD = EventFactory.createLoop();
    /**
     * @see ScreenInitPre#init(Screen, ScreenAccess)
     */
    Event<ScreenInitPre> INIT_PRE = EventFactory.createEventResult();
    /**
     * @see ScreenInitPost#init(Screen, ScreenAccess)
     */
    Event<ScreenInitPost> INIT_POST = EventFactory.createLoop();
    /**
     * @see ScreenRenderPre#render(Screen, GuiGraphicsExtractor, int, int, float)
     */
    Event<ScreenRenderPre> RENDER_PRE = EventFactory.createEventResult();
    /**
     * @see ScreenRenderPost#render(Screen, GuiGraphicsExtractor, int, int, float)
     */
    Event<ScreenRenderPost> RENDER_POST = EventFactory.createLoop();
    /**
     * @see ContainerScreenRenderBackground#render(AbstractContainerScreen, GuiGraphicsExtractor, int, int, float)
     */
    Event<ContainerScreenRenderBackground> RENDER_CONTAINER_BACKGROUND = EventFactory.createLoop();
    /**
     * @see ContainerScreenRenderForeground#render(AbstractContainerScreen, GuiGraphicsExtractor, int, int, float)
     */
    Event<ContainerScreenRenderForeground> RENDER_CONTAINER_FOREGROUND = EventFactory.createLoop();
    /**
     * @see SetScreen#modifyScreen(Screen)
     */
    Event<SetScreen> SET_SCREEN = EventFactory.createCompoundEventResult();
    
    interface RenderHud {
        /**
         * Invoked after the in-game hud has been rendered.
         * Equivalent to NeoForge's {@code RenderGameOverlayEvent.Post@ElementType#ALL} and Fabric's {@code HudRenderCallback}.
         *
         * @param graphics  The graphics context.
         * @param deltaTracker The tick delta.
         */
        void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker);
    }
    
    interface ScreenInitPre {
        /**
         * Invoked when a screen is being initialized and after the previous widgets have been cleared.
         * Equivalent to NeoForge's {@code GuiScreenEvent.InitGuiEvent.Pre} event.
         *
         * @param screen The screen.
         * @param access The accessor of the screen.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla initialization may be cancelled by the result.
         */
        EventResult init(Screen screen, ScreenAccess access);
    }
    
    interface ScreenInitPost {
        /**
         * Invoked after a screen has been initialized and all the vanilla initialization logic has happened.
         * Equivalent to NeoForge's {@code GuiScreenEvent.InitGuiEvent.Post} event.
         *
         * @param screen The screen.
         * @param access The accessor of the screen.
         */
        void init(Screen screen, ScreenAccess access);
    }
    
    interface ScreenRenderPre {
        /**
         * Invoked before any screen is rendered.
         * Equivalent to NeoForge's {@code GuiScreenEvent.DrawScreenEvent.Pre} event.
         *
         * @param screen   The screen.
         * @param graphics The graphics context.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         * @return A {@link EventResult} determining the outcome of the event,
         * the vanilla render may be cancelled by the result.
         */
        EventResult render(Screen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);
    }
    
    interface ScreenRenderPost {
        /**
         * Invoked after a screen has finished rendering using the vanilla logic.
         * Equivalent to NeoForge's {@code GuiScreenEvent.DrawScreenEvent.Post} event.
         *
         * @param screen   The screen.
         * @param graphics The graphics context.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         */
        void render(Screen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);
    }
    
    interface ContainerScreenRenderBackground {
        /**
         * Invoked after a container screen's background are rendered.
         * Equivalent to NeoForge's {@code ContainerScreenEvent.DrawBackground} event.
         *
         * @param screen   The screen.
         * @param graphics The graphics context.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         */
        void render(AbstractContainerScreen<?> screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);
    }
    
    interface ContainerScreenRenderForeground {
        /**
         * Invoked after a screen has finished rendering most of the foreground, but before any floating widgets are rendered.
         * Equivalent to NeoForge's {@code ContainerScreenEvent.DrawForeground} event.
         *
         * @param screen   The screen.
         * @param graphics The graphics context.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         */
        void render(AbstractContainerScreen<?> screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta);
    }
    
    interface SetScreen {
        /**
         * Invoked before a new screen is set to open.
         * Equivalent to NeoForge's {@code GuiOpenEvent} event.
         *
         * @param screen The screen that is going to be opened.
         * @return A {@link CompoundEventResult} determining the outcome of the event,
         * if an outcome is set, the vanilla screen is overridden.
         */
        CompoundEventResult<Screen> modifyScreen(Screen screen);
    }
}
