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

package me.shedaniel.architectury.registry;

import me.shedaniel.architectury.ArchitecturyPopulator;
import me.shedaniel.architectury.Populatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public final class CreativeTabs {
    public CreativeTabs() {}
    
    @Populatable
    private static final Impl IMPL = null;
    
    // I am sorry, fabric wants a resource location instead of the translation key for whatever reason
    public static CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon) {
        return IMPL.create(name, icon);
    }
    
    public interface Impl {
        CreativeModeTab create(ResourceLocation name, Supplier<ItemStack> icon);
    }
    
    static {
        ArchitecturyPopulator.populate(CreativeTabs.class);
    }
}
