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

import me.shedaniel.architectury.event.EventResult;
import me.shedaniel.architectury.event.events.EntityEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.NaturalSpawner.AfterSpawnCallback;
import net.minecraft.world.level.NaturalSpawner.SpawnPredicate;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
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

import java.util.List;
import java.util.Random;

@Mixin(NaturalSpawner.class)
public abstract class MixinNaturalSpawner {
    @Shadow
    private static boolean isValidPositionForMob(ServerLevel serverLevel, Mob mob, double d) {
        return false;
    }
    
    @Redirect(
            method = "spawnCategoryForPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z",
                    ordinal = 0
            )
    )
    private static boolean overrideNaturalSpawnCondition(ServerLevel level, Mob entity, double f) {
        EventResult result = EntityEvent.CHECK_SPAWN.invoker().canSpawn(entity, level, entity.xOld, entity.yOld, entity.zOld, MobSpawnType.NATURAL, null);
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
        EventResult result = EntityEvent.CHECK_SPAWN.invoker().canSpawn(mob, level, mob.xOld, mob.yOld, mob.zOld, MobSpawnType.CHUNK_GENERATION, null);
        if (result.value() != null) {
            return result.value();
        } else {
            return mob.checkSpawnRules(level, type);
        }
    }
    
}
