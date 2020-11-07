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

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.PlayerEvent;
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
}
