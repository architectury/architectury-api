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

package dev.architectury.mixin.fabric.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @WrapOperation(method = "charTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;charTyped(Lnet/minecraft/client/input/CharacterEvent;)Z"))
    private boolean wrapCharTyped(Screen screen, CharacterEvent characterEvent, Operation<Boolean> original) {
        if (screen instanceof ScreenInputDelegate delegate) {
            return original.call(delegate.architectury_delegateInputs(), characterEvent);
        }
        return original.call(screen, characterEvent);
    }
    
    @WrapOperation(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z"))
    private boolean wrapKeyPressed(Screen screen, KeyEvent keyEvent, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(minecraft, screen, keyEvent);
        if (result.isPresent())
            return true;
        if (original.call(screen, keyEvent))
            return true;
        result = ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(minecraft, screen, keyEvent);
        return result.isPresent();
    }
    
    @WrapOperation(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyReleased(Lnet/minecraft/client/input/KeyEvent;)Z"))
    private boolean wrapKeyReleased(Screen screen, KeyEvent keyEvent, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(minecraft, screen, keyEvent);
        if (result.isPresent())
            return true;
        if (original.call(screen, keyEvent))
            return true;
        result = ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(minecraft, screen, keyEvent);
        return result.isPresent();
    }
    
    @Inject(method = "keyPress", at = @At("RETURN"), cancellable = true)
    public void onRawKey(long handle, int action, KeyEvent keyEvent, CallbackInfo info) {
        if (handle == this.minecraft.getWindow().handle()) {
            var result = ClientRawInputEvent.KEY_PRESSED.invoker().keyPressed(minecraft, action, keyEvent);
            if (result.isPresent())
                info.cancel();
        }
    }
}
