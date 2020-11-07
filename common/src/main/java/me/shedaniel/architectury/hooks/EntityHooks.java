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

@Environment(EnvType.CLIENT)
public final class EntityHooks {
    private EntityHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    public static String getEncodeId(Entity entity) {
        return IMPL.getEncodeId(entity);
    }
    
    public interface Impl {
        String getEncodeId(Entity entity);
    }
    
    static {
        ArchitecturyPopulator.populate(ItemEntityHooks.class);
    }
}
