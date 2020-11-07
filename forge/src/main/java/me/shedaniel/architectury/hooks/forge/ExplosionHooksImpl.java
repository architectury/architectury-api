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

package me.shedaniel.architectury.hooks.forge;

import me.shedaniel.architectury.hooks.ExplosionHooks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class ExplosionHooksImpl implements ExplosionHooks.Impl {
    @Override
    public Vector3d getPosition(Explosion explosion) {
        return explosion.getPosition();
    }
    
    @Override
    public Entity getSource(Explosion explosion) {
        return explosion.getExploder();
    }
    
    @Override
    public float getRadius(Explosion explosion) {
        try {
            return (float) ObfuscationReflectionHelper.findField(Explosion.class, "field_77280_f").get(explosion);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
