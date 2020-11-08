/*
 * Copyright 2020 shedaniel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.shedaniel.architectury.mixin.fabric.client;

import me.shedaniel.architectury.event.events.InteractionEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow @Nullable public LocalPlayer player;
    
    @Shadow @Nullable public HitResult hitResult;
    
    @Inject(method = "clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/chat/NarratorChatListener;clear()V"))
    private void handleLogin(Screen screen, CallbackInfo ci) {
        PlayerEvent.CLIENT_PLAYER_QUIT.invoker().quit(player);
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
}
