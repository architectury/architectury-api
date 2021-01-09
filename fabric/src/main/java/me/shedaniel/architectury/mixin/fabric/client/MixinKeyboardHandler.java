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

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @Shadow
    private boolean sendRepeatsToGui;
    
    @Inject(method = "charTyped", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                           ordinal = 0), cancellable = true)
    public void onCharFirst(long long_1, int int_1, int int_2, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(minecraft, minecraft.screen, (char) int_1, int_2);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "charTyped", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                           ordinal = 1), cancellable = true)
    public void onCharSecond(long long_1, int int_1, int int_2, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.CHAR_TYPED_PRE.invoker().charTyped(minecraft, minecraft.screen, (char) int_1, int_2);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "charTyped", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                           ordinal = 0, shift = At.Shift.AFTER), cancellable = true)
    public void onCharFirstPost(long long_1, int int_1, int int_2, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(minecraft, minecraft.screen, (char) int_1, int_2);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "charTyped", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                           ordinal = 1, shift = At.Shift.AFTER), cancellable = true)
    public void onCharSecondPost(long long_1, int int_1, int int_2, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.CHAR_TYPED_POST.invoker().charTyped(minecraft, minecraft.screen, (char) int_1, int_2);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "keyPress", at = @At(value = "INVOKE",
                                          target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                          ordinal = 0), cancellable = true)
    public void onKey(long long_1, int int_1, int int_2, int int_3, int int_4, CallbackInfo info) {
        if (!info.isCancelled()) {
            if (int_3 != 1 && (int_3 != 2 || !this.sendRepeatsToGui)) {
                if (int_3 == 0) {
                    InteractionResult result = ClientScreenInputEvent.KEY_RELEASED_PRE.invoker().keyReleased(minecraft, minecraft.screen, int_1, int_2, int_4);
                    if (result != InteractionResult.PASS)
                        info.cancel();
                }
            } else {
                InteractionResult result = ClientScreenInputEvent.KEY_PRESSED_PRE.invoker().keyPressed(minecraft, minecraft.screen, int_1, int_2, int_4);
                if (result != InteractionResult.PASS)
                    info.cancel();
            }
        }
    }
    
    @Inject(method = "keyPress", at = @At(value = "INVOKE",
                                          target = "Lnet/minecraft/client/gui/screens/Screen;wrapScreenError(Ljava/lang/Runnable;Ljava/lang/String;Ljava/lang/String;)V",
                                          ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    public void onKeyAfter(long long_1, int int_1, int int_2, int int_3, int int_4, CallbackInfo info, ContainerEventHandler containerEventHandler, boolean bls[]) {
        if (!info.isCancelled() && !bls[0]) {
            InteractionResult result;
            if (int_3 != 1 && (int_3 != 2 || !this.sendRepeatsToGui)) {
                result = ClientScreenInputEvent.KEY_RELEASED_POST.invoker().keyReleased(minecraft, minecraft.screen, int_1, int_2, int_4);
            } else {
                result = ClientScreenInputEvent.KEY_PRESSED_POST.invoker().keyPressed(minecraft, minecraft.screen, int_1, int_2, int_4);
            }
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
}
