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

package me.shedaniel.architectury.hooks;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ExplosionHooks {
    private ExplosionHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static Vec3 getPosition(Explosion explosion) {
        return IMPL.getPosition(explosion);
    }
    
    @Nullable
    public static Entity getSource(Explosion explosion) {
        return IMPL.getSource(explosion);
    }
    
    public static float getRadius(Explosion explosion) {
        return IMPL.getRadius(explosion);
    }
    
    public interface Impl {
        Vec3 getPosition(Explosion explosion);
        
        Entity getSource(Explosion explosion);
        
        float getRadius(Explosion explosion);
    }
    
    static {
        ArchitecturyPopulator.populate(ExplosionHooks.class);
    }
}
