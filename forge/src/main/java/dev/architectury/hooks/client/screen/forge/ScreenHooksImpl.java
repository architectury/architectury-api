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

package dev.architectury.hooks.client.screen.forge;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ScreenHooksImpl {
    @SuppressWarnings("unchecked")
    public static List<NarratableEntry> getNarratables(Screen screen) {
        return (List<NarratableEntry>) getField(screen, "narratables");
    }

    @SuppressWarnings("unchecked")
    public static List<Renderable> getRenderables(Screen screen) {
        return (List<Renderable>) getField(screen, "renderables");
    }

    public static <T extends AbstractWidget & Renderable & NarratableEntry> T addRenderableWidget(Screen screen, T widget) {
        return invoke(screen, "addRenderableWidget", widget);
    }

    public static <T extends Renderable> T addRenderableOnly(Screen screen, T listener) {
        return invoke(screen, "addRenderableOnly", listener);
    }

    public static <T extends GuiEventListener & NarratableEntry> T addWidget(Screen screen, T listener) {
        return invoke(screen, "addWidget", listener);
    }

    private static Object getField(Screen screen, String name) {
        Class<?> current = screen.getClass();
        while (current != null) {
            try {
                Field field = current.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(screen);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            } catch (IllegalAccessException exception) {
                throw new IllegalStateException("Failed to access Screen field '" + name + "'", exception);
            }
        }
        throw new IllegalStateException("Could not find Screen field '" + name + "'");
    }

    @SuppressWarnings("unchecked")
    private static <T> T invoke(Screen screen, String name, T arg) {
        Class<?> current = screen.getClass();
        while (current != null) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(arg.getClass())) {
                    try {
                        method.setAccessible(true);
                        return (T) method.invoke(screen, arg);
                    } catch (ReflectiveOperationException exception) {
                        throw new IllegalStateException("Failed to invoke Screen method '" + name + "'", exception);
                    }
                }
            }
            current = current.getSuperclass();
        }
        throw new IllegalStateException("Could not find Screen method '" + name + "'");
    }
}
