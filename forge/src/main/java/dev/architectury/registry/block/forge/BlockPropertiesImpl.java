/*
 * This file is part of architectury.
 * Copyright (C) 2020, 2021 architectury
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package dev.architectury.registry.block.forge;

import dev.architectury.registry.block.BlockProperties;
import dev.architectury.registry.block.ToolType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;

public class BlockPropertiesImpl {
    public static BlockProperties of(Material material, MaterialColor materialColor) {
        return new Impl(material, (state) -> materialColor);
    }
    
    public static BlockProperties of(Material material, Function<BlockState, MaterialColor> function) {
        return new Impl(material, function);
    }
    
    public static BlockProperties copy(BlockBehaviour abstractBlock) {
        return copy(abstractBlock.properties);
    }
    
    public static BlockProperties copy(BlockBehaviour.Properties old) {
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
