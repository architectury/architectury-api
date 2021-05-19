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

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Inject(method = "restoreFrom", at = @At("RETURN"))
    private void restoreFrom(ServerPlayer serverPlayer, boolean bl, CallbackInfo ci) {
        PlayerEvent.PLAYER_CLONE.invoker().clone((ServerPlayer) (Object) this, serverPlayer, bl);
    }
    
    @Inject(method = "openMenu", at = @At("RETURN"))
    private void openMenu(MenuProvider menuProvider, CallbackInfoReturnable<OptionalInt> cir) {
        if (cir.getReturnValue().isPresent()) {
            PlayerEvent.OPEN_MENU.invoker().open((ServerPlayer) (Object) this, ((ServerPlayer) (Object) this).containerMenu);
        }
    }
    
    @Inject(method = "openHorseInventory", at = @At("RETURN"))
    private void openHorseInventory(AbstractHorse abstractHorse, Container container, CallbackInfo ci) {
        PlayerEvent.OPEN_MENU.invoker().open((ServerPlayer) (Object) this, ((ServerPlayer) (Object) this).containerMenu);
    }
    
    @Inject(method = "doCloseContainer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;removed(Lnet/minecraft/world/entity/player/Player;)V",
                    shift = At.Shift.AFTER))
    private void doCloseContainer(CallbackInfo ci) {
        PlayerEvent.CLOSE_MENU.invoker().close((ServerPlayer) (Object) this, ((ServerPlayer) (Object) this).containerMenu);
    }
    
    @Inject(method = "triggerDimensionChangeTriggers", at = @At("HEAD"))
    private void changeDimension(ServerLevel serverLevel, CallbackInfo ci) {
        PlayerEvent.CHANGE_DIMENSION.invoker().change((ServerPlayer) (Object) this, serverLevel.dimension(), ((ServerPlayer) (Object) this).level.dimension());
    }
}
