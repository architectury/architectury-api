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

package me.shedaniel.architectury.event.events.client;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;

@Environment(EnvType.CLIENT)
public interface ClientScreenInputEvent {
    Event<MouseScrolled> MOUSE_SCROLLED_PRE = EventFactory.createInteractionResult();
    Event<MouseScrolled> MOUSE_SCROLLED_POST = EventFactory.createInteractionResult();
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createInteractionResult();
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.createInteractionResult();
    Event<MouseReleased> MOUSE_RELEASED_PRE = EventFactory.createInteractionResult();
    Event<MouseReleased> MOUSE_RELEASED_POST = EventFactory.createInteractionResult();
    Event<MouseDragged> MOUSE_DRAGGED_PRE = EventFactory.createInteractionResult();
    Event<MouseDragged> MOUSE_DRAGGED_POST = EventFactory.createInteractionResult();
    Event<KeyTyped> CHAR_TYPED_PRE = EventFactory.createInteractionResult();
    Event<KeyTyped> CHAR_TYPED_POST = EventFactory.createInteractionResult();
    Event<KeyPressed> KEY_PRESSED_PRE = EventFactory.createInteractionResult();
    Event<KeyPressed> KEY_PRESSED_POST = EventFactory.createInteractionResult();
    Event<KeyReleased> KEY_RELEASED_PRE = EventFactory.createInteractionResult();
    Event<KeyReleased> KEY_RELEASED_POST = EventFactory.createInteractionResult();
    
    interface KeyPressed {
        InteractionResult keyPressed(Minecraft client, Screen screen, int keyCode, int scanCode, int modifiers);
    }
    
    interface KeyReleased {
        InteractionResult keyReleased(Minecraft client, Screen screen, int keyCode, int scanCode, int modifiers);
    }
    
    interface KeyTyped {
        InteractionResult charTyped(Minecraft client, Screen screen, char character, int keyCode);
    }
    
    interface MouseScrolled {
        InteractionResult mouseScrolled(Minecraft client, Screen screen, double mouseX, double mouseY, double amount);
    }
    
    interface MouseReleased {
        InteractionResult mouseReleased(Minecraft client, Screen screen, double mouseX, double mouseY, int button);
    }
    
    interface MouseDragged {
        InteractionResult mouseDragged(Minecraft client, Screen screen, double mouseX1, double mouseY1, int button, double mouseX2, double mouseY2);
    }
    
    interface MouseClicked {
        InteractionResult mouseClicked(Minecraft client, Screen screen, double mouseX, double mouseY, int button);
    }
}
