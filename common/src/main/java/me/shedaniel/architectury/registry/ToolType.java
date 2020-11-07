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

import me.shedaniel.architectury.Populatable;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public final class ToolType {
    public static final ToolType PICKAXE = create("pickaxe", ToolType::pickaxeTag);
    public static final ToolType AXE = create("axe", ToolType::axeTag);
    public static final ToolType HOE = create("hoe", ToolType::hoeTag);
    public static final ToolType SHOVEL = create("shovel", ToolType::shovelTag);
    @Populatable
    private static final Impl IMPL = null;
    
    private static Tag<Item> pickaxeTag() {
        return IMPL.pickaxeTag();
    }
    
    private static Tag<Item> axeTag() {
        return IMPL.axeTag();
    }
    
    private static Tag<Item> hoeTag() {
        return IMPL.hoeTag();
    }
    
    private static Tag<Item> shovelTag() {
        return IMPL.shovelTag();
    }
    
    public final String forgeName;
    public final Supplier<Tag<Item>> fabricTag;
    private Object obj;
    
    private ToolType(String forgeName, Supplier<Tag<Item>> fabricTag) {
        this.forgeName = forgeName;
        this.fabricTag = fabricTag;
    }
    
    public static ToolType create(String forgeName, Supplier<Tag<Item>> fabricTag) {
        return new ToolType(forgeName, fabricTag);
    }
    
    public interface Impl {
        Tag<Item> pickaxeTag();
        
        Tag<Item> axeTag();
        
        Tag<Item> hoeTag();
        
        Tag<Item> shovelTag();
    }
}
