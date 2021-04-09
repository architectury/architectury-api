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

package me.shedaniel.architectury.mixin.fabric;

import me.shedaniel.architectury.event.events.EntityEvent;
import me.shedaniel.architectury.hooks.fabric.PersistentEntitySectionManagerHooks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.ref.WeakReference;

@Mixin(PersistentEntitySectionManager.class)
public class MixinPersistentEntitySectionManager<T extends EntityAccess> implements PersistentEntitySectionManagerHooks {
    @Unique
    private WeakReference<ServerLevel> levelRef;
    
    @Override
    public void architectury_attachLevel(ServerLevel level) {
        levelRef = new WeakReference<>(level);
    }
    
    @Inject(method = "addEntity", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/core/SectionPos;asLong(Lnet/minecraft/core/BlockPos;)J"),
            cancellable = true)
    private void addEntity(T entityAccess, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        // TODO: Check if other classes implements EntityAccess as well
        if (entityAccess instanceof Entity && levelRef != null) {
            ServerLevel level = levelRef.get();
            levelRef = null;
            
            if (level != null) {
                if (EntityEvent.ADD.invoker().add((Entity) entityAccess, level) == InteractionResult.FAIL) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
