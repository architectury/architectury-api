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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    
    @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z", ordinal = 0))
    private boolean onGuiMouseClicked(Screen instance, double mouseX, double mouseY, double amountX, double amountY, Operation<Boolean> original) {
        var minecraft = Minecraft.getInstance();
        var result = ClientScreenInputEvent.MOUSE_SCROLLED_PRE.invoker().mouseScrolled(minecraft, minecraft.screen, mouseX, mouseY, amountX, amountY);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(instance, mouseX, mouseY, amountX, amountY)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_SCROLLED_POST.invoker().mouseScrolled(minecraft, minecraft.screen, mouseX, mouseY, amountX, amountY);
        return result.isPresent();
    }
    
    @Inject(method = "onScroll",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z",
                    ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    public void onRawMouseScrolled(long handle, double xOffset, double yOffset, CallbackInfo info, boolean discreteMouseScroll, double mouseWheelSensitivity, double amountX, double doubleY) {
        if (!info.isCancelled()) {
            var result = ClientRawInputEvent.MOUSE_SCROLLED.invoker().mouseScrolled(minecraft, amountX, doubleY);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @WrapOperation(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(DDI)Z", ordinal = 0))
    private boolean onGuiMouseClicked(Screen instance, double mouseX, double mouseY, int b, Operation<Boolean> original) {
        var minecraft = Minecraft.getInstance();
        var result = ClientScreenInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, minecraft.screen, mouseX, mouseY, b);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(instance, mouseX, mouseY, b)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, minecraft.screen, mouseX, mouseY, b);
        return result.isPresent();
    }
    
    @Inject(method = "onPress", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;getOverlay()Lnet/minecraft/client/gui/screens/Overlay;",
            ordinal = 0), cancellable = true)
    public void onRawMouseClicked(long handle, int button, int action, int mods, CallbackInfo info) {
        if (!info.isCancelled()) {
            var result = ClientRawInputEvent.MOUSE_CLICKED_PRE.invoker().mouseClicked(minecraft, button, action, mods);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @Inject(method = "onPress", at = @At("RETURN"), cancellable = true)
    public void onRawMouseClickedPost(long handle, int button, int action, int mods, CallbackInfo info) {
        if (handle == this.minecraft.getWindow().getWindow()) {
            var result = ClientRawInputEvent.MOUSE_CLICKED_POST.invoker().mouseClicked(minecraft, button, action, mods);
            if (result.isPresent())
                info.cancel();
        }
    }
    
    @WrapOperation(method = "onPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(DDI)Z", ordinal = 0))
    private boolean onGuiMouseReleased(Screen instance, double mouseX, double mouseY, int b, Operation<Boolean> original) {
        var minecraft = Minecraft.getInstance();
        var result = ClientScreenInputEvent.MOUSE_RELEASED_PRE.invoker().mouseReleased(minecraft, minecraft.screen, mouseX, mouseY, b);
        if (result.isPresent()) {
            return true;
        }
        if (original.call(instance, mouseX, mouseY, b)) {
            return true;
        }
        result = ClientScreenInputEvent.MOUSE_RELEASED_POST.invoker().mouseReleased(minecraft, minecraft.screen, mouseX, mouseY, b);
        return result.isPresent();
    }
    
    @Inject(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z", ordinal = 0), cancellable = true)
    private void onGuiMouseDraggedPre(CallbackInfo ci, @Local(ordinal = 2) double mouseX, @Local(ordinal = 3) double mouseY, @Local(ordinal = 4) double deltaX, @Local(ordinal = 5) double deltaY) {
        if (ClientScreenInputEvent.MOUSE_DRAGGED_PRE.invoker().mouseDragged(Minecraft.getInstance(), Minecraft.getInstance().screen, mouseX, mouseY, this.activeButton, deltaX, deltaY).isPresent()) {
            ci.cancel();
        }
    }
    
    @SuppressWarnings({"UnresolvedMixinReference", "DefaultAnnotationParam"})
    @WrapOperation(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(DDIDD)Z"))
    private boolean onGuiMouseDraggedPost(Screen screen, double mouseX, double mouseY, int button, double deltaX, double deltaY, Operation<Boolean> original) {
        if (original.call(screen, mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        
        return ClientScreenInputEvent.MOUSE_DRAGGED_POST.invoker().mouseDragged(Minecraft.getInstance(), screen, mouseX, mouseY, button, deltaX, deltaY).isPresent();
    }
}
