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

package dev.architectury.hooks.client.screen.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ScreenHooksImpl {
    public static List<NarratableEntry> getNarratables(Screen screen) {
        return screen.narratables;
    }
    
    public static List<Renderable> getRenderables(Screen screen) {
        return screen.renderables;
    }
    
    public static <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        return screen.addRenderableWidget(widget);
    }
    
    public static <T extends Renderable> T addRenderableOnly(Screen screen, T listener) {
        return screen.addRenderableOnly(listener);
    }
    
    public static <T extends GuiEventListener & NarratableEntry> T addWidget(Screen screen, T listener) {
        return screen.addWidget(listener);
    }
}
