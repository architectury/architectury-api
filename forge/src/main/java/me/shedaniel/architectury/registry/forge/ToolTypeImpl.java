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

package me.shedaniel.architectury.registry.forge;


import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

public class ToolTypeImpl implements ToolType.Impl {
    @Override
    public ITag<Item> pickaxeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> axeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> hoeTag() {
        return null;
    }
    
    @Override
    public ITag<Item> shovelTag() {
        return null;
    }
}
