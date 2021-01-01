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

package me.shedaniel.architectury.hooks.fabric;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public class ExplosionHooksImpl {
    public static Vec3 getPosition(Explosion explosion) {
        return ((ExplosionExtensions) explosion).architectury_getPosition();
    }
    
    public static Entity getSource(Explosion explosion) {
        return ((ExplosionExtensions) explosion).architectury_getSource();
    }
    
    public static float getRadius(Explosion explosion) {
        return ((ExplosionExtensions) explosion).architectury_getRadius();
    }
    
    public static void setRadius(Explosion explosion, float radius) {
        ((ExplosionExtensions) explosion).architectury_setRadius(radius);
    }
    
    public interface ExplosionExtensions {
        Vec3 architectury_getPosition();
        
        Entity architectury_getSource();
        
        float architectury_getRadius();
        
        void architectury_setRadius(float v);
    }
}
