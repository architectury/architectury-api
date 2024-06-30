/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021, 2022 architectury
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

package dev.architectury.core.item;

import dev.architectury.platform.Platform;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

public class ArchitecturyMobBucketItem extends MobBucketItem {
    public ArchitecturyMobBucketItem(Supplier<? extends EntityType<?>> entity, Supplier<? extends Fluid> fluid, Supplier<? extends SoundEvent> sound, Properties properties) {
        super(checkPlatform(entity).get(), fluid.get(), sound.get(), properties);
    }
    
    private static <T> T checkPlatform(T obj) {
        if (Platform.isForgeLike()) {
            throw new IllegalStateException("This class should've been replaced on Forge!");
        }
        
        return obj;
    }
}
