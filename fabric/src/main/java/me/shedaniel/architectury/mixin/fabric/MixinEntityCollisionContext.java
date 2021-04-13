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

import me.shedaniel.architectury.hooks.fabric.EntityHooksImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityCollisionContext.class)
public abstract class MixinEntityCollisionContext implements CollisionContext, EntityHooksImpl.CollisionContextExtension {
    
    @Unique
    private Entity entity = null;
    
    @Inject(method = "<init>(Lnet/minecraft/world/entity/Entity;)V",
            at = @At("RETURN"))
    public void saveEntity(Entity entity, CallbackInfo ci) {
        this.entity = entity;
    }
    
    @Nullable
    @Override
    public Entity arch$getEntity() {
        return entity;
    }
    
}
