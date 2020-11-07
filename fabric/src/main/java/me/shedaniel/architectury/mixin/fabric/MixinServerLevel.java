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
