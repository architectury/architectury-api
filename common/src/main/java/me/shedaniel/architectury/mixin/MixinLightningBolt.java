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

package me.shedaniel.architectury.mixin;

import me.shedaniel.architectury.event.events.LightningEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(LightningBolt.class)
public abstract class MixinLightningBolt extends Entity {
    
    public MixinLightningBolt(EntityType<?> type, Level level) {
        super(type, level);
        throw new IllegalStateException();
    }
    
    @Inject(method = "tick", at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;",
            ordinal = 0,
            shift = At.Shift.BY,
            by = 1
    ), locals = LocalCapture.CAPTURE_FAILHARD)
    public void handleLightning(CallbackInfo ci, double d0, List<Entity> list) {
        if (this.removed || this.level.isClientSide) {
            return;
        }
        
        LightningEvent.STRIKE.invoker().onStrike((LightningBolt) (Object) this, this.level, this.position(), list);
    }
    
}
