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
    
    @WrapOperation(method = "charTyped", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;charTyped(CI)Z"))
    private boolean wrapCharTyped(Screen screen, char chr, int mods, Operation<Boolean> original) {
        if (screen instanceof ScreenInputDelegate delegate) {
            return original.call(delegate.architectury_delegateInputs(), chr, mods);
        }
        return original.call(screen, chr, mods);
    }
    
    @WrapOperation(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyPressed(III)Z"))
    private boolean wrapKeyPressed(Screen screen, int keyCode, int scanCode, int modifiers, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(minecraft, screen, keyCode, scanCode, modifiers);
        if (result.isPresent())
            return true;
        if (original.call(screen, keyCode, scanCode, modifiers))
            return true;
        result = ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(minecraft, screen, keyCode, scanCode, modifiers);
        return result.isPresent();
    }
    
    @WrapOperation(method = "keyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;keyReleased(III)Z"))
    private boolean wrapKeyReleased(Screen screen, int keyCode, int scanCode, int modifiers, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(minecraft, screen, keyCode, scanCode, modifiers);
        if (result.isPresent())
            return true;
        if (original.call(screen, keyCode, scanCode, modifiers))
            return true;
        result = ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(minecraft, screen, keyCode, scanCode, modifiers);
        return result.isPresent();
    }
    
    @Inject(method = "keyPress", at = @At("RETURN"), cancellable = true)
    public void onRawKey(long handle, int key, int scanCode, int action, int modifiers, CallbackInfo info) {
        if (handle == this.minecraft.getWindow().getWindow()) {
            var result = ClientRawInputEvent.KEY_PRESSED.invoker().keyPressed(minecraft, key, scanCode, action, modifiers);
            if (result.isPresent())
                info.cancel();
        }
    }
}
