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

package dev.architectury.mixin.fabric;

import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.utils.value.DoubleValue;
import dev.architectury.utils.value.FloatValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void hurtServer(ServerLevel level, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Player) return;
        if (EntityEvent.LIVING_HURT.invoker().hurt((LivingEntity) (Object) this, damageSource, f).isFalse()) {
            cir.setReturnValue(false);
        }
    }
    
    @WrapMethod(method = "causeFallDamage")
    private boolean livingFall(double distance, float damageMultiplier, DamageSource damageSource, Operation<Boolean> original) {
        double[] newDistance = {distance};
        float[] newMultiplier = {damageMultiplier};
        var result = EntityEvent.LIVING_FALL.invoker().fall((LivingEntity) (Object) this, new DoubleValue() {
            @Override
            public double getAsDouble() {
                return newDistance[0];
            }
            
            @Override
            public void accept(double value) {
                newDistance[0] = value;
            }
        }, new FloatValue() {
            @Override
            public float getAsFloat() {
                return newMultiplier[0];
            }
            
            @Override
            public void accept(float value) {
                newMultiplier[0] = value;
            }
        });
        if (result.isFalse()) {
            return false;
        }
        return original.call(newDistance[0], newMultiplier[0], damageSource);
    }
}
