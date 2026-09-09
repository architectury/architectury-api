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

package dev.architectury.mixin.neoforge;

import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.mixin.neoforge.client.KeyMappingAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Only the pre-attack hook lives here. Client start/stop are fired from NeoForge's own
 * {@code ClientStartedEvent}/{@code ClientStoppingEvent} in {@code EventHandlerImplClient} instead.
 */
@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    @Final
    public Options options;
    
    @Shadow
    @Nullable
    public LocalPlayer player;
    
    @Shadow
    @Nullable
    public MultiPlayerGameMode gameMode;
    
    @Unique
    private boolean architectury$attackCancelled;
    
    /**
     * Fabric exposes this natively as {@code ClientPreAttackCallback}; NeoForge has no equivalent, so the same three
     * injection points Fabric uses are replicated here.
     */
    @Inject(method = "handleKeybinds", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0))
    private void preAttack(CallbackInfo ci) {
        int clickCount = ((KeyMappingAccessor) options.keyAttack).architectury$getClickCount();
        architectury$attackCancelled = (options.keyAttack.isDown() || clickCount != 0)
                && InteractionEvent.CLIENT_PRE_ATTACK.invoker().preAttack(player, clickCount).isFalse();
    }
    
    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void cancelStartAttack(CallbackInfoReturnable<Boolean> cir) {
        if (architectury$attackCancelled) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void cancelContinueAttack(boolean breaking, CallbackInfo ci) {
        if (architectury$attackCancelled) {
            if (gameMode != null) {
                gameMode.stopDestroyBlock();
            }
            ci.cancel();
        }
    }
}
