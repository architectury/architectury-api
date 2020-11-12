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

import com.google.common.collect.Maps;
import me.shedaniel.architectury.ExpectPlatform;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Supplier;

public final class ToolType {
    public static final ToolType PICKAXE = create("pickaxe", ToolType::pickaxeTag);
    public static final ToolType AXE = create("axe", ToolType::axeTag);
    public static final ToolType HOE = create("hoe", ToolType::hoeTag);
    public static final ToolType SHOVEL = create("shovel", ToolType::shovelTag);
    
    @ExpectPlatform
    private static Tag<Item> pickaxeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> axeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> hoeTag() {
        throw new AssertionError();
    }
    
    @ExpectPlatform
    private static Tag<Item> shovelTag() {
        throw new AssertionError();
    }
    
    private static final Map<String, ToolType> TYPES = Maps.newConcurrentMap();
    public final String forgeName;
    public final Supplier<Tag<Item>> fabricTag;
    private Object obj;
    
    private ToolType(String forgeName, Supplier<Tag<Item>> fabricTag) {
        this.forgeName = forgeName;
        this.fabricTag = fabricTag;
    }
    
    public static ToolType create(String forgeName, Supplier<Tag<Item>> fabricTag) {
        return TYPES.computeIfAbsent(forgeName, s -> new ToolType(s, fabricTag));
    }
    
    public static ToolType byName(String forgeName) {
        return TYPES.get(forgeName);
    }
}
