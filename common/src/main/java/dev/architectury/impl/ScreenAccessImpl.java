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

package dev.architectury.impl;

import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.hooks.client.screen.ScreenHooks;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class ScreenAccessImpl implements ScreenAccess {
    private Screen screen;
    
    public ScreenAccessImpl(Screen screen) {
        this.screen = screen;
    }
    
    public void setScreen(Screen screen) {
        this.screen = screen;
    }
    
    @Override
    public Screen getScreen() {
        return screen;
    }
    
    @Override
    public List<NarratableEntry> getNarratables() {
        return ScreenHooks.getNarratables(screen);
    }
    
    @Override
    public List<Widget> getRenderables() {
        return ScreenHooks.getRenderables(screen);
    }
    
    @Override
    public <T extends AbstractWidget & Widget & NarratableEntry> T addRenderableWidget(T widget) {
        return ScreenHooks.addRenderableWidget(screen, widget);
    }
    
    @Override
    public <T extends Widget> T addRenderableOnly(T listener) {
        return ScreenHooks.addRenderableOnly(screen, listener);
    }
    
    @Override
    public <T extends GuiEventListener & NarratableEntry> T addWidget(T listener) {
        return ScreenHooks.addWidget(screen, listener);
    }
}
