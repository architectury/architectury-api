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
import net.minecraft.client.gui.screens.Screen;

@Environment(EnvType.CLIENT)
public interface ClientScreenInputEvent {
    /**
     * @see MouseScrolled#mouseScrolled(Minecraft, Screen, double, double, double, double)
     */
    Event<MouseScrolled> MOUSE_SCROLLED_PRE = EventFactory.createEventResult();
    Event<MouseScrolled> MOUSE_SCROLLED_POST = EventFactory.createEventResult();
    /**
     * @see MouseClicked#mouseClicked(Minecraft, Screen, double, double, int)
     */
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createEventResult();
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.createEventResult();
    /**
     * @see MouseReleased#mouseReleased(Minecraft, Screen, double, double, int)
     */
    Event<MouseReleased> MOUSE_RELEASED_PRE = EventFactory.createEventResult();
    Event<MouseReleased> MOUSE_RELEASED_POST = EventFactory.createEventResult();
    /**
     * @see MouseDragged#mouseDragged(Minecraft, Screen, double, double, int, double, double)
     */
    Event<MouseDragged> MOUSE_DRAGGED_PRE = EventFactory.createEventResult();
    Event<MouseDragged> MOUSE_DRAGGED_POST = EventFactory.createEventResult();
    /**
     * @see KeyTyped#charTyped(Minecraft, Screen, char, int)
     */
    Event<KeyTyped> CHAR_TYPED_PRE = EventFactory.createEventResult();
    Event<KeyTyped> CHAR_TYPED_POST = EventFactory.createEventResult();
    /**
     * @see KeyPressed#keyPressed(Minecraft, Screen, int, int, int)
     */
    Event<KeyPressed> KEY_PRESSED_PRE = EventFactory.createEventResult();
    Event<KeyPressed> KEY_PRESSED_POST = EventFactory.createEventResult();
    /**
     * @see KeyReleased#keyReleased(Minecraft, Screen, int, int, int)
     */
    Event<KeyReleased> KEY_RELEASED_PRE = EventFactory.createEventResult();
    Event<KeyReleased> KEY_RELEASED_POST = EventFactory.createEventResult();
    
    interface KeyPressed {
        /**
         * Invoked whenever a key press is performed inside a screen.
         * Equivalent to Forge's {@code GuiScreenEvent.KeyboardKeyPressedEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client    The Minecraft instance performing it.
         * @param screen    The screen this keystroke was performed in.
         * @param keyCode   The key code.
         * @param scanCode  The raw keyboard scan code.
         * @param modifiers Additional modifiers.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla pressing mechanism may be cancelled by the result.
         */
        EventResult keyPressed(Minecraft client, Screen screen, int keyCode, int scanCode, int modifiers);
    }
    
    interface KeyReleased {
        /**
         * Invoked whenever a held key is released inside a screen.
         * Equivalent to Forge's {@code GuiScreenEvent.KeyboardKeyReleasedEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client    The Minecraft instance performing it.
         * @param screen    The screen this keystroke was performed in.
         * @param keyCode   The key code.
         * @param scanCode  The raw keyboard scan code.
         * @param modifiers Additional modifiers.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla releasing mechanism may be cancelled by the result.
         */
        EventResult keyReleased(Minecraft client, Screen screen, int keyCode, int scanCode, int modifiers);
    }
    
    interface KeyTyped {
        /**
         * Invoked whenever a character is typed within a screen.
         * Equivalent to Forge's {@code GuiScreenEvent.KeyboardCharTypedEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client    The Minecraft instance performing it.
         * @param screen    The screen this keystroke was performed in.
         * @param character The typed character.
         * @param keyCode   The key code.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla typing mechanism may be cancelled by the result.
         */
        EventResult charTyped(Minecraft client, Screen screen, char character, int keyCode);
    }
    
    interface MouseScrolled {
        /**
         * Invoked whenever the mouse scroll wheel is moved.
         * Equivalent to Forge's {@code GuiScreenEvent.MouseScrollEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client  The Minecraft instance performing it.
         * @param screen  The screen this keystroke was performed in.
         * @param mouseX  The scaled x-coordinate of the mouse cursor.
         * @param mouseY  The scaled y-coordinate of the mouse cursor.
         * @param amountX The amount the scroll wheel is moved horizontally.
         * @param amountY The amount the scroll wheel is moved vertically.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla scrolling mechanism may be cancelled by the result.
         */
        EventResult mouseScrolled(Minecraft client, Screen screen, double mouseX, double mouseY, double amountX, double amountY);
    }
    
    interface MouseReleased {
        /**
         * Invoked whenever a mouse button is released.
         * Equivalent to Forge's {@code GuiScreenEvent.MouseReleasedEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client The Minecraft instance performing it.
         * @param screen The screen this keystroke was performed in.
         * @param mouseX The scaled x-coordinate of the mouse cursor.
         * @param mouseY The scaled y-coordinate of the mouse cursor.
         * @param button The released mouse button.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla releasing mechanism may be cancelled by the result.
         */
        EventResult mouseReleased(Minecraft client, Screen screen, double mouseX, double mouseY, int button);
    }
    
    interface MouseDragged {
        /**
         * Invoked whenever the mouse is dragged across a screen.
         * Equivalent to Forge's {@code GuiScreenEvent.MouseDragEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client  The Minecraft instance performing it.
         * @param screen  The screen this keystroke was performed in.
         * @param mouseX1 The initial scaled x-coordinate of the mouse cursor.
         * @param mouseY1 The initial scaled y-coordinate of the mouse cursor.
         * @param button  The dragged mouse button.
         * @param mouseX2 The final scaled x-coordinate of the mouse cursor.
         * @param mouseY2 The final scaled y-coordinate of the mouse cursor.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla dragging mechanism may be cancelled by the result.
         */
        EventResult mouseDragged(Minecraft client, Screen screen, double mouseX1, double mouseY1, int button, double mouseX2, double mouseY2);
    }
    
    interface MouseClicked {
        /**
         * Invoked whenever a mouse click is performed. A click consists of the press and release of a mouse button.
         * Equivalent to Forge's {@code GuiScreenEvent.MouseClickedEvent} event.
         *
         * <p> This event is handled in two phases PRE and POST, which are invoked
         * before and after the keys have been processed by the screen, respectively.
         *
         * @param client The Minecraft instance performing it.
         * @param screen The screen this keystroke was performed in.
         * @param mouseX The scaled x-coordinate of the mouse cursor.
         * @param mouseY The scaled y-coordinate of the mouse cursor.
         * @param button The clicked mouse button.
         * @return A {@link EventResult} determining the outcome of the event,
         * the execution of the vanilla clicking mechanism may be cancelled by the result.
         */
        EventResult mouseClicked(Minecraft client, Screen screen, double mouseX, double mouseY, int button);
    }
}
