/*
 * This file is part of architectury.
 * Copyright (C) 2020 shedaniel
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

package me.shedaniel.architectury.hooks.forge;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ScreenHooksImpl {
    public static List<AbstractWidget> getButtons(Screen screen) {
        return screen.buttons;
    }
    
    public static <T extends AbstractWidget> T addButton(Screen screen, T t) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "func_230480_a_", AbstractWidget.class).invoke(screen, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T extends GuiEventListener> T addChild(Screen screen, T t) {
        try {
            return (T) ObfuscationReflectionHelper.findMethod(Screen.class, "func_230481_d_", GuiEventListener.class).invoke(screen, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
