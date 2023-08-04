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

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public interface ClientRawInputEvent {
    /**
     * @see MouseScrolled#mouseScrolled(Minecraft, double, double)
     */
    Event<MouseScrolled> MOUSE_SCROLLED = EventFactory.createEventResult();
    /**
     * @see MouseClicked#mouseClicked(Minecraft, int, int, int)
     */
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createEventResult();
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.createEventResult();
    /**
     * @see KeyPressed#keyPressed(Minecraft, int, int, int, int)
     */
    Event<KeyPressed> KEY_PRESSED = EventFactory.createEventResult();
    
    interface KeyPressed {
        /**
         * Invoked whenever a key input is performed.
         * Equivalent to Forge's {@code InputEvent.KeyInputEvent} event.
         *
         * @param client    The Minecraft instance performing it.
         * @param keyCode   The key code.
         * @param scanCode  The raw keyboard scan code.
         * @param action    The action that should be performed.
         * @param modifiers Additional modifiers.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla pressing mechanism may be cancelled by the result.
         */
        EventResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers);
    }
    
    interface MouseScrolled {
        /**
         * Invoked whenever the mouse scroll wheel is used.
         * Equivalent to Forge's {@code InputEvent.MouseScrollEvent} event.
         *
         * @param client  The Minecraft instance performing it.
         * @param amountX The amount of movement on the X axis.
         * @param amountY The amount of movement on the Y axis.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla scrolling mechanism may be cancelled by the result.
         */
        EventResult mouseScrolled(Minecraft client, double amountX, double amountY);
    }
    
    interface MouseClicked {
        /**
         * Invoked whenever a mouse button is pressed.
         * There are two variants, either a raw mouse input or the input after it is processed by the game.
         *
         * @param client The Minecraft instance performing it.
         * @param button The pressed mouse button.
         * @param action The action that should be performed.
         * @param mods   Additional modifiers.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla clicking mechanism may be cancelled by the result.
         */
        EventResult mouseClicked(Minecraft client, int button, int action, int mods);
    }
}
