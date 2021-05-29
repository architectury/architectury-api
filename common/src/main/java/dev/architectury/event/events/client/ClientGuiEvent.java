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

package dev.architectury.event.events.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventFactory;
import dev.architectury.event.Event;
import dev.architectury.event.EventResult;
import dev.architectury.hooks.screen.ScreenAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface ClientGuiEvent {
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
     * @see ScreenInitPre#init(Screen, ScreenAccess)
     */
    Event<ScreenInitPre> INIT_PRE = EventFactory.createEventResult();
    /**
     * @see ScreenInitPost#init(Screen, ScreenAccess)
     */
    Event<ScreenInitPost> INIT_POST = EventFactory.createLoop();
    /**
     * @see ScreenRenderPre#render(Screen, PoseStack, int, int, float)
     */
    Event<ScreenRenderPre> RENDER_PRE = EventFactory.createEventResult();
    /**
     * @see ScreenRenderPost#render(Screen, PoseStack, int, int, float)
     */
    Event<ScreenRenderPost> RENDER_POST = EventFactory.createLoop();
    /**
     * @see SetScreen#modifyScreen(Screen)
     */
    Event<SetScreen> SET_SCREEN = EventFactory.createCompoundEventResult();
    
    @Environment(EnvType.CLIENT)
    interface RenderHud {
        /**
         * Invoked after the in-game hud has been rendered.
         * Equivalent to Forge's {@code RenderGameOverlayEvent.Post@ElementType#ALL} and Fabric's {@code HudRenderCallback}.
         *
         * @param matrices  The pose stack.
         * @param tickDelta The tick delta.
         */
        void renderHud(PoseStack matrices, float tickDelta);
    }
    
    @Environment(EnvType.CLIENT)
    interface DebugText {
        /**
         * Invoked when the debug text is being gathered for rendering.
         * There are two different versions of this event, one for the left and one for the right side.
         * Equivalent to Forge's {@code RenderGameOverlayEvent.Text}, when {@code Minecraft.getInstance().options.renderDebug} is true.
         *
         * @param strings The current debug text strings.
         */
        void gatherText(List<String> strings);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenInitPre {
        /**
         * Invoked when a screen is being initialized and after the previous widgets have been cleared.
         * Equivalent to Forge's {@code GuiScreenEvent.InitGuiEvent.Pre} event.
         *
         * @param screen The screen.
         * @param access The accessor of the screen.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla initialization may be cancelled by the result.
         */
        EventResult init(Screen screen, ScreenAccess access);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenInitPost {
        /**
         * Invoked after a screen has been initialized and all the vanilla initialization logic has happened.
         * Equivalent to Forge's {@code GuiScreenEvent.InitGuiEvent.Post} event.
         *
         * @param screen The screen.
         * @param access The accessor of the screen.
         */
        void init(Screen screen, ScreenAccess access);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenRenderPre {
        /**
         * Invoked before any screen is rendered.
         * Equivalent to Forge's {@code GuiScreenEvent.DrawScreenEvent.Pre} event.
         *
         * @param screen   The screen.
         * @param matrices The pose stack.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         * @return A {@link EventResult} determining the outcome of the event,
         * the vanilla render may be cancelled by the result.
         */
        EventResult render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }
    
    @Environment(EnvType.CLIENT)
    interface ScreenRenderPost {
        /**
         * Invoked after a screen has finished rendering using the vanilla logic.
         * Equivalent to Forge's {@code GuiScreenEvent.DrawScreenEvent.Post} event.
         *
         * @param screen   The screen.
         * @param matrices The pose stack.
         * @param mouseX   The scaled x-coordinate of the mouse cursor.
         * @param mouseY   The scaled y-coordinate of the mouse cursor.
         * @param delta    The current tick delta.
         */
        void render(Screen screen, PoseStack matrices, int mouseX, int mouseY, float delta);
    }
    
    @Environment(EnvType.CLIENT)
    interface SetScreen {
        /**
         * Invoked before a new screen is set to open.
         * Equivalent to Forge's {@code GuiOpenEvent} event.
         *
         * @param screen The screen that is going to be opened.
         * @return A {@link CompoundEventResult} determining the outcome of the event,
         * if an outcome is set, the vanilla screen is overridden.
         */
        CompoundEventResult<Screen> modifyScreen(Screen screen);
    }
}
