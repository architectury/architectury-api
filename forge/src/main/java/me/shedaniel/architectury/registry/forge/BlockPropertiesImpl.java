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

import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

import java.util.function.Function;

public class BlockPropertiesImpl implements BlockProperties.Impl {
    @Override
    public BlockProperties of(Material material, MaterialColor materialColor) {
        return new Impl(material, (state) -> materialColor);
    }
    
    @Override
    public BlockProperties of(Material material, Function<BlockState, MaterialColor> function) {
        return new Impl(material, function);
    }
    
    @Override
    public BlockProperties copy(AbstractBlock abstractBlock) {
        return copy(abstractBlock.properties);
    }
    
    @Override
    public BlockProperties copy(AbstractBlock.Properties old) {
        BlockProperties properties = of(old.material, old.materialColor);
        properties.material = old.material;
        properties.destroyTime = old.destroyTime;
        properties.explosionResistance = old.explosionResistance;
        properties.hasCollision = old.hasCollision;
        properties.isRandomlyTicking = old.isRandomlyTicking;
        properties.lightEmission = old.lightEmission;
        properties.materialColor = old.materialColor;
        properties.soundType = old.soundType;
        properties.friction = old.friction;
        properties.speedFactor = old.speedFactor;
        properties.dynamicShape = old.dynamicShape;
        properties.canOcclude = old.canOcclude;
        properties.isAir = old.isAir;
        properties.requiresCorrectToolForDrops = old.requiresCorrectToolForDrops;
        return properties;
    }
    
    private static final class Impl extends BlockProperties {
        public Impl(Material material, Function<BlockState, MaterialColor> function) {
            super(material, function);
        }
        
        @Override
        public BlockProperties tool(ToolType type, int level) {
            harvestTool(net.minecraftforge.common.ToolType.get(type.forgeName));
            harvestLevel(level);
            return this;
        }
    }
}
