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

package dev.architectury.hooks.forgelike.forge;

import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;

public class ForgeLikeClientHooksImpl {
    public static void preMouseScroll(ScreenEvent.MouseScrolled.Pre event) {
        if (ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY()).isFalse()) {
            event.setCanceled(true);
        }
    }
    
    public static void postMouseScroll(ScreenEvent.MouseScrolled.Post event) {
        ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(Minecraft.getInstance(), event.getScreen(), event.getMouseX(), event.getMouseY(), event.getDeltaX(), event.getDeltaY());
    }
    
    public static void inputMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(Minecraft.getInstance(), event.getDeltaX(), event.getDeltaY()).isFalse()) {
            event.setCanceled(true);
        }
    }
}
