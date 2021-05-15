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

/**
 * Should be moved to the client package and renamed to ScreenEvent in version 2.0
 */
@Environment(EnvType.CLIENT)
public interface GuiEvent {
    /**
     * @see RenderHud#renderHud(PoseStack, float)
     */
    Event<RenderHud> RENDER_HUD = EventFactory.createLoop();
    /**
     * @see DebugText#gatherText(List)
     */
    Event<DebugText> DEBUG_TEXT_LEFT = EventFactory.createLoop();
    Event<DebugText> DEBUG_TEXT_RIGHT = EventFactory.createLoop();
    /**
     * @see ScreenInitPre#init(Screen, List, List)
     */
    Event<ScreenInitPre> INIT_PRE = EventFactory.createInteractionResult();
    /**
     * @see ScreenInitPost#init(Screen, List, List)
     */
    Event<ScreenInitPost> INIT_POST = EventFactory.createLoop();
    /**
     * @see ScreenRenderPre#render(Screen, PoseStack, int, int, float)
     */
    Event<ScreenRenderPre> RENDER_PRE = EventFactory.createInteractionResult();
    /**
     * @see ScreenRenderPost#render(Screen, PoseStack, int, int, float)
     */
    Event<ScreenRenderPost> RENDER_POST = EventFactory.createLoop();
    /**
     * @see SetScreen#modifyScreen(Screen)
     */
    Event<SetScreen> SET_SCREEN = EventFactory.createInteractionResultHolder();
    
    @Environment(EnvType.CLIENT)
    interface RenderHud {
        /**
         * Called after the in-game hud is rendered.
         * Equal to the forge {@code RenderGameOverlayEvent.Post@ElementType#ALL} event and fabric's {@code HudRenderCallback}.
         * 
         * @param matrices The render buffer.
         * @param tickDelta The delta tick.
         */
        void renderHud(PoseStack matrices, float tickDelta);
    }
    
    @Environment(EnvType.CLIENT)
    interface DebugText {
        /**
         * Called when the debug text is rendered. There are two different versions, for th left and for the right side.
         * Equal to the forge {@code RenderGameOverlayEvent.Text} event, when {@code Minecraft.getInstance().options.renderDebug} is true.
         * 
         * @param strings Contains the already added strings. You can add your own lines by adding to the list.
         */
        void gatherText(List<String> strings);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenInitPre {
        /**
         * Called when a screen is initialized and after all widgets are cleared.
         * Equal to the forge {@code GuiScreenEvent.InitGuiEvent.Pre} event.
         * 
         * @param screen The screen.
         * @param widgets The widgets that are added after this event.
         * @param children The listeners for the screen events.
         * @return Returning {@link InteractionResult#FAIL} results in the rest of the init method being ignored.
         */
        InteractionResult init(Screen screen, List<AbstractWidget> widgets, List<GuiEventListener> children);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenInitPost {
        /**
         * Called when a screen is initialized after all the vanilla logic happened.
         * Equal to the forge {@code GuiScreenEvent.InitGuiEvent.Post} event.
         *
         * @param screen The screen.
         * @param widgets The widgets that were added.
         * @param children The listeners for the screen events.
         */
        void init(Screen screen, List<AbstractWidget> widgets, List<GuiEventListener> children);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenRenderPre {
        /**
         * Called before any rendering is happening.
         * Equal to the forge {@code GuiScreenEvent.DrawScreenEvent.Pre} event.
         * 
         * @param screen The screen.
         * @param matrices The render buffer.
         * @param mouseX The mouse x position.
         * @param mouseY The mouse y position.
         * @param delta The current tick delta.
         * @return Returning {@link InteractionResult#FAIL} prevents any other rendering.
         */
        InteractionResult render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenRenderPost {
        /**
         * Called after all things have been rendered.
         * Equal to the forge {@code GuiScreenEvent.DrawScreenEvent.Post} event.
         *
         * @param screen The screen.
         * @param matrices The render buffer.
         * @param mouseX The mouse x position.
         * @param mouseY The mouse y position.
         * @param delta The current tick delta.
         */
        void render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }
    
    @Environment(EnvType.CLIENT)
    interface SetScreen {
        /**
         * Called when a screen should be opened.
         * Equal to the forge {@code GuiOpenEvent} event.
         * 
         * @param screen The screen that is gonna be opened.
         * @return Return {@link InteractionResultHolder#fail(Object)} leads to the screen not being opened. The result is ignored. {@link InteractionResultHolder#success(Object)} leads to the passed new screen being used instead of the old one.
         */
        InteractionResultHolder<Screen> modifyScreen(Screen screen);
    }
}
