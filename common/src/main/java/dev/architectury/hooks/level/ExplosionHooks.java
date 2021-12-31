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

package dev.architectury.hooks.level;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class ExplosionHooks {
    private ExplosionHooks() {
    }
    
    @ExpectPlatform
    public static Vec3 getPosition(Explosion explosion) {
        throw new AssertionError();
    }
    
    /**
     * @deprecated Use the field directly.
     */
    @Nullable
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static Entity getSource(Explosion explosion) {
        return explosion.source;
    }
    
    /**
     * @deprecated Use the field directly.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static float getRadius(Explosion explosion) {
        return explosion.radius;
    }
    
    /**
     * @deprecated Use the field directly.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public static void setRadius(Explosion explosion, float radius) {
        explosion.radius = radius;
    }
}
