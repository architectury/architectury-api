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
import me.shedaniel.architectury.utils.IntValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.item.ItemEntity;

@Environment(EnvType.CLIENT)
public final class ItemEntityHooks {
    private ItemEntityHooks() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    /**
     * The lifespan of an {@link ItemEntity}.
     * Fabric: Since it doesn't have this, the value will be a readable-only value of 6000.
     * Forge: Value of lifespan of the forge hook.
     */
    public static IntValue lifespan(ItemEntity entity) {
        return IMPL.lifespan(entity);
    }
    
    public interface Impl {
        IntValue lifespan(ItemEntity entity);
    }
    
    static {
        ArchitecturyPopulator.populate(ItemEntityHooks.class);
    }
}