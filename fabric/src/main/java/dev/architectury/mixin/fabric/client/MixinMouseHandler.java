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
import com.llamalad7.mixinextras.sugar.Local;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientScreenInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Shadow
    @Final
    private Minecraft minecraft;
    
    @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z", ordinal = 0))
    private boolean onGuiMouseClicked(Screen instance, double mouseX, double mouseY, double amountX, double amountY, Operation<Boolean> original) {
        var minecraft = Minecraft.getInstance();
        var result = ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(minecraft, minecraft.gui.screen(), mouseX, mouseY, amountX, amountY);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(instance, mouseX, mouseY, amountX, amountY)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(minecraft, minecraft.gui.screen(), mouseX, mouseY, amountX, amountY);
        return result.isPresent();
    }
    
    @Inject(method = "onScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z",
                    ordinal = 0), cancellable = true)
    public void onRawMouseScrolled(long handle, double xOffset, double yOffset, CallbackInfo info, @Local(ordinal = 3) double amountX, @Local(ordinal = 4) double doubleY) {
        if (!info.isCancelled()) {
            var result = ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(minecraft, amountX, doubleY);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @WrapOperation(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"))
    private boolean wrapMouseClicked(Screen screen, MouseButtonEvent buttonEvent, boolean doubleClick, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, minecraft.gui.screen(), buttonEvent, doubleClick);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(screen, buttonEvent, doubleClick)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, minecraft.gui.screen(), buttonEvent, doubleClick);
        return result.isPresent();
    }
    
    @Inject(method = "onButton", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;overlay()Lnet/minecraft/client/gui/screens/Overlay;",
            ordinal = 0), cancellable = true)
    public void onRawMouseClicked(long handle, MouseButtonInfo buttonInfo, int action, CallbackInfo info) {
        if (!info.isCancelled()) {
            var result = ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, buttonInfo, action);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @Inject(method = "onButton", at = @At("RETURN"), cancellable = true)
    public void onRawMouseClickedPost(long handle, MouseButtonInfo buttonInfo, int action, CallbackInfo info) {
        if (handle == this.minecraft.getWindow().handle()) {
            var result = ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, buttonInfo, action);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @WrapOperation(method = "onButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z"))
    private boolean wrapMouseReleased(Screen screen, MouseButtonEvent buttonEvent, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(minecraft, minecraft.gui.screen(), buttonEvent);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(screen, buttonEvent)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_RELEASED_POST.invoker().mouseReleased(minecraft, minecraft.gui.screen(), buttonEvent);
        return result.isPresent();
    }
    
    @WrapOperation(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z"))
    private boolean wrapMouseDragged(Screen screen, MouseButtonEvent event, double deltaX, double deltaY, Operation<Boolean> original) {
        var result = ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(minecraft, screen, event, deltaX, deltaY);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(screen, event, deltaX, deltaY)) {
            return true;
        }
        
        return ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(Minecraft.getInstance(), screen, event, deltaX, deltaY).isPresent();
    }
}
