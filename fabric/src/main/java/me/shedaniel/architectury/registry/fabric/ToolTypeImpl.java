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

package me.shedaniel.architectury.registry.fabric;

import me.shedaniel.architectury.registry.ToolType;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class ToolTypeImpl implements ToolType.Impl {
    @Override
    public Tag<Item> pickaxeTag() {
        return FabricToolTags.PICKAXES;
    }
    
    @Override
    public Tag<Item> axeTag() {
        return FabricToolTags.AXES;
    }
    
    @Override
    public Tag<Item> hoeTag() {
        return FabricToolTags.HOES;
    }
    
    @Override
    public Tag<Item> shovelTag() {
        return FabricToolTags.SHOVELS;
    }
}
