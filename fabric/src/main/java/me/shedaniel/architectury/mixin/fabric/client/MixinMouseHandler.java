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

import me.shedaniel.architectury.event.events.client.ClientRawInputEvent;
import me.shedaniel.architectury.event.events.client.ClientScreenInputEvent;
import me.shedaniel.architectury.impl.fabric.ScreenInputDelegate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @Shadow
    private int activeButton;
    
    @Shadow
    private double xpos;
    
    @Shadow
    private double ypos;
    
    @Inject(method = "onScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDD)Z",
                     ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void onMouseScrolled(long handle, double xOffset, double yOffset, CallbackInfo info, double amount, double x, double y) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(minecraft, minecraft.screen, x, y, amount);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "onScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDD)Z",
                     ordinal = 0, shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void onMouseScrolledPost(long handle, double xOffset, double yOffset, CallbackInfo info, double amount, double x, double y) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(minecraft, minecraft.screen, x, y, amount);
        }
    }
    
    @Inject(method = "onScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z",
                     ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void onRawMouseScrolled(long handle, double xOffset, double yOffset, CallbackInfo info, double amount) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(minecraft, amount);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = {"lambda$onPress$0", "method_1611"}, at = @At("HEAD"), cancellable = true, remap = false)
    public void onGuiMouseClicked(boolean[] bls, double d, double e, int button, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, minecraft.screen, d, e, button);
            if (result != InteractionResult.PASS) {
                bls[0] = true;
                info.cancel();
            }
        }
    }
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = {"lambda$onPress$0", "method_1611"}, at = @At("RETURN"), cancellable = true, remap = false)
    public void onGuiMouseClickedPost(boolean[] bls, double d, double e, int button, CallbackInfo info) {
        if (!info.isCancelled() && !bls[0]) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, minecraft.screen, d, e, button);
            if (result != InteractionResult.PASS) {
                bls[0] = true;
                info.cancel();
            }
        }
    }
    
    @Inject(method = "onPress", at = @At(value = "FIELD",
                                         target = "Lnet/minecraft/client/Minecraft;overlay:Lnet/minecraft/client/gui/screens/Overlay;",
                                         ordinal = 0), cancellable = true)
    public void onRawMouseClicked(long handle, int button, int action, int mods, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, button, action, mods);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @Inject(method = "onPress", at = @At("RETURN"), cancellable = true)
    public void onRawMouseClickedPost(long handle, int button, int action, int mods, CallbackInfo info) {
        if (handle == this.minecraft.getWindow().getWindow()) {
            InteractionResult result = ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, button, action, mods);
            if (result != InteractionResult.PASS)
                info.cancel();
        }
    }
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = {"lambda$onPress$1", "method_1605"}, at = @At("HEAD"), cancellable = true, remap = false)
    public void onGuiMouseReleased(boolean[] bls, double d, double e, int button, CallbackInfo info) {
        if (!info.isCancelled()) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(minecraft, minecraft.screen, d, e, button);
            if (result != InteractionResult.PASS) {
                bls[0] = true;
                info.cancel();
            }
        }
    }
    
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = {"lambda$onPress$1", "method_1605"}, at = @At("RETURN"), cancellable = true, remap = false)
    public void onGuiMouseReleasedPost(boolean[] bls, double d, double e, int button, CallbackInfo info) {
        if (!info.isCancelled() && !bls[0]) {
            InteractionResult result = ClientScreenInputEvent.MOUSE_RELEASED_POST.invoker().mouseReleased(minecraft, minecraft.screen, d, e, button);
            if (result != InteractionResult.PASS) {
                bls[0] = true;
                info.cancel();
            }
        }
    }
    
    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyVariable(method = {"method_1602", "lambda$onMove$11"}, at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private GuiEventListener wrapMouseDragged(GuiEventListener screen) {
        if (screen instanceof ScreenInputDelegate) {
            return ((ScreenInputDelegate) screen).architectury_delegateInputs();
        }
        return screen;
    }
}
