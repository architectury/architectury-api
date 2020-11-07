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

import me.shedaniel.architectury.event.events.ExplosionEvent;
import me.shedaniel.architectury.hooks.fabric.ExplosionHooksImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Set;

@Mixin(Explosion.class)
public class MixinExplosion implements ExplosionHooksImpl.ExplosionExtensions {
    @Shadow @Final private Level level;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;
    @Shadow @Final @Nullable private Entity source;
    @Unique Vec3 position;
    
    @Inject(method = "explode", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;<init>(DDD)V", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void explodePost(CallbackInfo ci, Set<BlockPos> set, float q, int r, int s, int t, int u, int v, int w, List<Entity> list) {
        ExplosionEvent.DETONATE.invoker().explode(level, (Explosion) (Object) this, list);
    }
    
    @Override
    public Vec3 architectury_getPosition() {
        if (position == null) {
            return position = new Vec3(x, y, z);
        }
        return position;
    }
    
    @Override
    public Entity architectury_getSource() {
        return source;
    }
}
