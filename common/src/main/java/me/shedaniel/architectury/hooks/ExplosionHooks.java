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

import me.shedaniel.architectury.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class ExplosionHooks {
    private ExplosionHooks() {}
    
    @ExpectPlatform
    public static Vec3 getPosition(Explosion explosion) {
        throw new AssertionError();
    }
    
    @Nullable
    @ExpectPlatform
    public static Entity getSource(Explosion explosion) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static float getRadius(Explosion explosion) {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    public static void setRadius(Explosion explosion, float radius) {
        throw new AssertionError();
    }
}
