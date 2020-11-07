/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
