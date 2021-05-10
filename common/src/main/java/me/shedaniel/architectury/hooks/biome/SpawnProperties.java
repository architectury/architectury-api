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

package me.shedaniel.architectury.hooks.biome;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;

public interface SpawnProperties {
    float getCreatureProbability();
    
    Map<MobCategory, List<MobSpawnSettings.SpawnerData>> getSpawners();
    
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();
    
    boolean isPlayerSpawnFriendly();
    
    interface Mutable extends SpawnProperties {
        Mutable setCreatureProbability(float probability);
        
        Mutable addSpawn(MobCategory category, MobSpawnSettings.SpawnerData data);
        
        boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate);
        
        Mutable setSpawnCost(EntityType<?> entityType, MobSpawnSettings.MobSpawnCost cost);
        
        Mutable setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit);
        
        Mutable clearSpawnCost(EntityType<?> entityType);
        
        Mutable setPlayerSpawnFriendly(boolean friendly);
    }
}
