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
import net.minecraft.world.InteractionResult;

@Environment(EnvType.CLIENT)
public interface ClientRawInputEvent {
    
    /**
     * @see MouseScrolled#mouseScrolled(Minecraft, double)
     */
    Event<MouseScrolled> MOUSE_SCROLLED = EventFactory.createInteractionResult();
    
    /**
     * @see MouseClicked#mouseClicked(Minecraft, int, int, int)
     */
    Event<MouseClicked> MOUSE_CLICKED_PRE = EventFactory.createInteractionResult();
    
    /**
     * @see MouseClicked#mouseClicked(Minecraft, int, int, int)
     */
    Event<MouseClicked> MOUSE_CLICKED_POST = EventFactory.createInteractionResult();
    
    /**
     * @see KeyPressed#keyPressed(Minecraft, int, int, int, int)
     */
    Event<KeyPressed> KEY_PRESSED = EventFactory.createInteractionResult();
    
    interface KeyPressed {
        /**
         * Called whenever a key input is performed.
         * Equal to the forge {@code InputEvent.KeyInputEvent} event.
         * 
         * @param client The minecraft instance performing it.
         * @param keyCode The key code. Look at {@link org.lwjgl.glfw.GLFW} line 66 to 211 for key codes.
         * @param scanCode The raw keyboard scan code.
         * @param action The action that should be performed.
         * @param modifiers Additional modifiers.
         * @return Any other result than {@link InteractionResult#PASS} leads to the cancellation of the key press.
         * 
         * @see me.shedaniel.architectury.mixin.fabric.client.MixinKeyboardHandler#onRawKey(long, int, int, int, int, org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
         * @see me.shedaniel.architectury.event.forge.EventHandlerImplClient#event(net.minecraftforge.client.event.InputEvent.KeyInputEvent)
         */
        InteractionResult keyPressed(Minecraft client, int keyCode, int scanCode, int action, int modifiers);
    }
    
    interface MouseScrolled {
        /**
         * Called whenever the mouse scroll wheel is used.
         * Equal to the forge {@code InputEvent.MouseScrollEvent} event.
         * 
         * @param client The minecraft instance performing it.
         * @param amount The amount of movement.
         * @return Any other result than {@link InteractionResult#PASS} leads to the cancellation of the mouse scroll functions.
         * At the time this is actually called, any open screen already has processed the scroll movement and so it can't be undone.
         * 
         * @see me.shedaniel.architectury.mixin.fabric.client.MixinMouseHandler#onRawMouseScrolled(long, double, double, org.spongepowered.asm.mixin.injection.callback.CallbackInfo, double)
         * @see me.shedaniel.architectury.event.forge.EventHandlerImplClient#event(net.minecraftforge.client.event.InputEvent.MouseScrollEvent)
         */
        InteractionResult mouseScrolled(Minecraft client, double amount);
    }
    
    interface MouseClicked {
        /**
         * Called whenever a mouse button is clicked.
         * There are two variants, either a raw mouse input or the input after it is processed by the game.
         * 
         * @param client The minecraft instance performing it.
         * @param button The pressed mouse button. Look at {@link org.lwjgl.glfw.GLFW} line 214 to 226 for mouse button codes.
         * @param action The action that should be performed.
         * @param mods Additional modifiers.
         * @return Any other result than {@link InteractionResult#PASS} leads to the cancellation of the mouse click.
         * 
         * @see me.shedaniel.architectury.mixin.fabric.client.MixinMouseHandler#onRawMouseClicked(long, int, int, int, org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
         * @see me.shedaniel.architectury.mixin.fabric.client.MixinMouseHandler#onRawMouseClickedPost(long, int, int, int, org.spongepowered.asm.mixin.injection.callback.CallbackInfo)
         * @see me.shedaniel.architectury.event.forge.EventHandlerImplClient#event(net.minecraftforge.client.event.InputEvent.RawMouseEvent)
         * @see me.shedaniel.architectury.event.forge.EventHandlerImplClient#event(net.minecraftforge.client.event.InputEvent.MouseInputEvent)
         */
        InteractionResult mouseClicked(Minecraft client, int button, int action, int mods);
    }
}
