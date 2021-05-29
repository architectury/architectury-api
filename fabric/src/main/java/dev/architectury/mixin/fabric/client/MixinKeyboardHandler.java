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

package dev.architectury.mixin.fabric.client;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import dev.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @Shadow
    private boolean sendRepeatsToGui;
    
    @ModifyVariable(method = {"method_1458", "lambda$charTyped$5"}, at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static GuiEventListener wrapCharTypedFirst(GuiEventListener screen) {
        if (screen instanceof ScreenInputDelegate delegate) {
            return delegate.architectury_delegateInputs();
        }
        return screen;
    }
    
    @ModifyVariable(method = {"method_1473", "lambda$charTyped$6"}, at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static GuiEventListener wrapCharTypedSecond(GuiEventListener screen) {
        if (screen instanceof ScreenInputDelegate delegate) {
            return delegate.architectury_delegateInputs();
        }
        return screen;
    }
    
    @Inject(method = "keyPress", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
            ordinal = 0), cancellable = true)
    public void onKey(long long_1, int int_1, int int_2, int int_3, int int_4, CallbackInfo info) {
        if (!info.isCancelled()) {
            if (int_3 != 1 && (int_3 != 2 || !this.sendRepeatsToGui)) {
                if (int_3 == 0) {
                    var result = ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(minecraft, minecraft.screen, int_1, int_2, int_4);
                    if (result.isPresent())
                        info.cancel();
                }
            } else {
                var result = ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(minecraft, minecraft.screen, int_1, int_2, int_4);
                if (result.isPresent())
                    info.cancel();
            }
        }
    }
    
    @Inject(method = "keyPress", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
            ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void onKeyAfter(long long_1, int int_1, int int_2, int int_3, int int_4, CallbackInfo info, Screen screen, boolean bls[]) {
        if (!info.isCancelled() && !bls[0]) {
            EventResult result;
            if (int_3 != 1 && (int_3 != 2 || !this.sendRepeatsToGui)) {
                result = ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(minecraft, screen, int_1, int_2, int_4);
            } else {
                result = ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(minecraft, screen, int_1, int_2, int_4);
            }
            if (result.isPresent())
                info.cancel();
        }
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
