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

import me.shedaniel.architectury.event.events.EntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.NaturalSpawner.AfterSpawnCallback;
import net.minecraft.world.level.NaturalSpawner.SpawnPredicate;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.ref.WeakReference;

@Mixin(NaturalSpawner.class)
public abstract class MixinNaturalSpawner {
    
    @Unique
    private static WeakReference<InteractionResult> arch$checkSpawn = new WeakReference<>(null);
    
    @Shadow
    private static boolean isValidPositionForMob(ServerLevel serverLevel, Mob mob, double d) {
        return false;
    }
    
    @Inject(
            method = "spawnCategoryForPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void checkSpawn(MobCategory cat, ServerLevel level, ChunkAccess ca, BlockPos pos, SpawnPredicate pred, AfterSpawnCallback cb, CallbackInfo ci,
            StructureFeatureManager sfm, ChunkGenerator cg, int i, BlockPos.MutableBlockPos mutablePos,
            int j, int k, int l, int m, int n, SpawnerData spawnerData, SpawnGroupData sgd,
            int o, int p, int q, double d, double e, Player player, double f, Mob entity) {
        arch$checkSpawn = new WeakReference<>(EntityEvent.CHECK_SPAWN.invoker().canSpawn(entity, level, d, i, e, MobSpawnType.NATURAL, null));
    }
    
    @Redirect(
            method = "spawnCategoryForPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
                    ordinal = 0
            )
    )
    private static boolean overrideCondition(ServerLevel level, Mob entity, double f) {
        switch (arch$checkSpawn.get()) {
            case FAIL:
                return false;
            case SUCCESS:
            case CONSUME:
                return true;
            default:
                return isValidPositionForMob(level, entity, f);
        }
    }
    
}
