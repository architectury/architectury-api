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
