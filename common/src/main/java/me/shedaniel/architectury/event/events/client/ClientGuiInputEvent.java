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

package me.shedaniel.architectury.event.events.client;

import me.shedaniel.architectury.event.Event;
import me.shedaniel.architectury.event.EventFactory;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.InteractionResult;

@Environment(EnvType.CLIENT)
public interface ClientGuiInputEvent {
    Event<MouseScrolled> MOUSE_SCROLLED_PRE = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, amount) -> {
            for (MouseScrolled listener : listeners) {
                InteractionResult result = listener.mouseScrolled(client, screen, mouseX, mouseY, amount);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseScrolled> MOUSE_SCROLLED_POST = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, amount) -> {
            for (MouseScrolled listener : listeners) {
                InteractionResult result = listener.mouseScrolled(client, screen, mouseX, mouseY, amount);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, button) -> {
            for (MouseClicked listener : listeners) {
                InteractionResult result = listener.mouseClicked(client, screen, mouseX, mouseY, button);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, button) -> {
            for (MouseClicked listener : listeners) {
                InteractionResult result = listener.mouseClicked(client, screen, mouseX, mouseY, button);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseReleased> MOUSE_RELEASED_PRE = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, button) -> {
            for (MouseReleased listener : listeners) {
                InteractionResult result = listener.mouseReleased(client, screen, mouseX, mouseY, button);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseReleased> MOUSE_RELEASED_POST = EventFactory.of(listeners -> {
        return (client, screen, mouseX, mouseY, button) -> {
            for (MouseReleased listener : listeners) {
                InteractionResult result = listener.mouseReleased(client, screen, mouseX, mouseY, button);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseDragged> MOUSE_DRAGGED_PRE = EventFactory.of(listeners -> {
        return (client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2) -> {
            for (MouseDragged listener : listeners) {
                InteractionResult result = listener.mouseDragged(client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<MouseDragged> MOUSE_DRAGGED_POST = EventFactory.of(listeners -> {
        return (client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2) -> {
            for (MouseDragged listener : listeners) {
                InteractionResult result = listener.mouseDragged(client, screen, mouseX1, mouseY1, button, mouseX2, mouseY2);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyTyped> CHAR_TYPED_PRE = EventFactory.of(listeners -> {
        return (client, screen, character, keyCode) -> {
            for (KeyTyped listener : listeners) {
                InteractionResult result = listener.charTyped(client, screen, character, keyCode);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyTyped> CHAR_TYPED_POST = EventFactory.of(listeners -> {
        return (client, screen, character, keyCode) -> {
            for (KeyTyped listener : listeners) {
                InteractionResult result = listener.charTyped(client, screen, character, keyCode);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyPressed> KEY_PRESSED_PRE = EventFactory.of(listeners -> {
        return (client, screen, keyCode, scanCode, modifiers) -> {
            for (KeyPressed listener : listeners) {
                InteractionResult result = listener.keyPressed(client, screen, keyCode, scanCode, modifiers);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyPressed> KEY_PRESSED_POST = EventFactory.of(listeners -> {
        return (client, screen, keyCode, scanCode, modifiers) -> {
            for (KeyPressed listener : listeners) {
                InteractionResult result = listener.keyPressed(client, screen, keyCode, scanCode, modifiers);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyReleased> KEY_RELEASED_PRE = EventFactory.of(listeners -> {
        return (client, screen, keyCode, scanCode, modifiers) -> {
            for (KeyReleased listener : listeners) {
                InteractionResult result = listener.keyReleased(client, screen, keyCode, scanCode, modifiers);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    Event<KeyReleased> KEY_RELEASED_POST = EventFactory.of(listeners -> {
        return (client, screen, keyCode, scanCode, modifiers) -> {
            for (KeyReleased listener : listeners) {
                InteractionResult result = listener.keyReleased(client, screen, keyCode, scanCode, modifiers);
                if (result != InteractionResult.PASS)
                    return result;
            }
            return InteractionResult.PASS;
        };
    });
    
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
