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

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NaturalSpawner.class)
public abstract class MixinNaturalSpawner {
    @Shadow
    private static boolean isValidPositionForMob(ServerLevel serverLevel, Mob mob, double d) {
        return false;
    }
    
    @Redirect(
            method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
                    ordinal = 0
            )
    )
    private static boolean overrideNaturalSpawnCondition(ServerLevel level, Mob entity, double f) {
        var result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(entity, level, entity.xOld, entity.yOld, entity.zOld, MobSpawnType.NATURAL, null);
        if (result.value() != null) {
            return result.value();
        } else {
            return isValidPositionForMob(level, entity, f);
        }
    }
    
    @Redirect(
            method = "spawnMobsForChunkGeneration",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z",
                    ordinal = 0
            )
    )
    private static boolean overrideChunkGenSpawnCondition(Mob mob, LevelAccessor level, MobSpawnType type) {
        var result = EntityEvent.LIVING_CHECK_SPAWN.invoker().canSpawn(mob, level, mob.xOld, mob.yOld, mob.zOld, MobSpawnType.CHUNK_GENERATION, null);
        if (result.value() != null) {
            return result.value();
        } else {
            return mob.checkSpawnRules(level, type);
        }
    }
    
}
