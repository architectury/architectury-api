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

package me.shedaniel.architectury.event.events.client;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

@Environment(EnvType.CLIENT)
public interface ClientRawInputEvent {
    /**
     * Invoked after the mouse has scrolled, but doesn't have a screen opened, and in a world, equivalent to forge's {@code InputEvent.MouseScrollEvent}.
     */
    Event<MouseScrolled> MOUSE_SCROLLED = EventFactory.createInteractionResult();
    /**
     * Invoked after the mouse has clicked, before the screen intercepts, equivalent to forge's {@code InputEvent.RawMouseEvent}.
     */
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createInteractionResult();
    /**
     * Invoked after the mouse has clicked, after the screen intercepts, equivalent to forge's {@code InputEvent.MouseInputEvent}.
     */
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.createInteractionResult();
    /**
     * Invoked after a key was pressed, after the screen intercepts, equivalent to forge's {@code InputEvent.KeyInputEvent}.
     */
    Event<KeyPressed> KEY_PRESSED = EventFactory.createInteractionResult();
    
    interface KeyPressed {
        InteractionResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers);
    }
    
    interface MouseScrolled {
        InteractionResult mouseScrolled(Minecraft client, double amount);
    }
    
    interface MouseClicked {
        InteractionResult mouseClicked(Minecraft client, int button, int action, int mods);
    }
}
