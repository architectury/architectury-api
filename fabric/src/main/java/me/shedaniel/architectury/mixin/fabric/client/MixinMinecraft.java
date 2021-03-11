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

import me.shedaniel.architectury.event.events.GuiEvent;
import me.shedaniel.architectury.event.events.InteractionEvent;
import me.shedaniel.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Unique
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow @Nullable public LocalPlayer player;
    
    @Shadow @Nullable public HitResult hitResult;
    
    @Shadow
    public abstract void setScreen(@Nullable Screen screen);
    
    @Unique
    private ThreadLocal<Boolean> setScreenCancelled = new ThreadLocal<>();
    
    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/chat/NarratorChatListener;clear()V"))
    private void handleLogin(Screen screen, CallbackInfo ci) {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(player);
    }
    
    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void rightClickAir(CallbackInfo ci, InteractionHand var1[], int var2, int var3, InteractionHand interactionHand, ItemStack itemStack) {
        if (itemStack.isEmpty() && (this.hitResult == null || this.hitResult.getType() == HitResult.Type.MISS)) {
            InteractionEvent.CLIENT_RIGHT_CLICK_AIR.invoker().click(player, interactionHand);
        }
    }
    
    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V", ordinal = 0))
    private void leftClickAir(CallbackInfo ci) {
        InteractionEvent.CLIENT_LEFT_CLICK_AIR.invoker().click(player, InteractionHand.MAIN_HAND);
    }
    
    @ModifyVariable(
            method = "setScreen",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/player/LocalPlayer;respawn()V",
                     shift = At.Shift.BY,
                     by = 2),
            argsOnly = true
    )
    public Screen modifyScreen(Screen screen) {
        Screen old = screen;
        InteractionResultHolder<Screen> event = GuiEvent.SET_SCREEN.invoker().modifyScreen(screen);
        switch (event.getResult()) {
            case FAIL:
                setScreenCancelled.set(true);
                return old;
            case SUCCESS:
                screen = event.getObject();
                if (old != null && screen != old) {
                    old.removed();
                }
            default:
                setScreenCancelled.set(false);
                return screen;
        }
    }
    
    @Inject(
            method = "setScreen",
            at = @At(value = "FIELD",
                     opcode = Opcodes.PUTFIELD,
                     target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
                     shift = At.Shift.BY,
                     by = -1),
            cancellable = true
    )
    public void cancelSetScreen(@Nullable Screen screen, CallbackInfo ci) {
        if (setScreenCancelled.get()) {
            ci.cancel();
            setScreenCancelled.set(false);
        }
    }
}
