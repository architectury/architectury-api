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
import me.shedaniel.architectury.event.events.LifecycleEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class MixinServerLevel {
    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerChunkCache;save(Z)V"))
    private void save(ProgressListener progressListener, boolean bl, boolean bl2, CallbackInfo ci) {
        LifecycleEvent.SERVER_WORLD_SAVE.invoker().act((ServerLevel) (Object) this);
    }
    
    @Inject(method = "addEntity", at = @At(value = "INVOKE",
                                           target = "Lnet/minecraft/server/level/ServerLevel;getChunk(IILnet/minecraft/world/level/chunk/ChunkStatus;Z)Lnet/minecraft/world/level/chunk/ChunkAccess;"),
            cancellable = true)
    private void addEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (EntityEvent.ADD.invoker().add(entity, (ServerLevel) (Object) this) == InteractionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "addPlayer", at = @At("HEAD"), cancellable = true)
    private void addPlayer(ServerPlayer serverPlayer, CallbackInfo ci) {
        if (EntityEvent.ADD.invoker().add(serverPlayer, (ServerLevel) (Object) this) == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
    
    @Inject(method = "loadFromChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;add(Lnet/minecraft/world/entity/Entity;)V"),
            cancellable = true)
    private void loadFromChunk(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (EntityEvent.ADD.invoker().add(entity, (ServerLevel) (Object) this) == InteractionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }
}
