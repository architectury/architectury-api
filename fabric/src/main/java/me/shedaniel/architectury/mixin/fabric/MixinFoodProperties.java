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

package dev.architectury.mixin.fabric;

import com.mojang.datafixers.util.Pair;
import dev.architectury.impl.fabric.FoodPropertiesAccess;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(FoodProperties.class)
public class MixinFoodProperties implements FoodPropertiesAccess {
    @Unique
    private List<Pair<Supplier<? extends MobEffectInstance>, Float>> suppliedEffects;
    @Unique
    private List<Pair<MobEffectInstance, Float>> suppliedEffectCache;
    
    @Override
    public void setSuppliedEffects(List<Pair<Supplier<? extends MobEffectInstance>, Float>> list) {
        this.suppliedEffects = list;
        suppliedEffectCache = null;
    }
    
    @Inject(method = "getEffects", at = @At("RETURN"))
    private void addSuppliedEffects(CallbackInfoReturnable<List<Pair<MobEffectInstance, Float>>> cir) {
        if (suppliedEffectCache == null) {
            suppliedEffectCache = new ArrayList<>();
            for (Pair<Supplier<? extends MobEffectInstance>, Float> suppliedEffect : suppliedEffects) {
                suppliedEffectCache.add(suppliedEffect.mapFirst(Supplier::get));
            }
        }
        cir.getReturnValue().addAll(suppliedEffectCache);
    }
}
